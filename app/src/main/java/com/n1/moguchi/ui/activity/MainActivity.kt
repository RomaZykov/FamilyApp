package com.n1.moguchi.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isNotEmpty
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.ProfileMode
import com.n1.moguchi.databinding.ActivityMainBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.fragment.child.home.HomeChildFragment
import com.n1.moguchi.ui.fragment.parent.PrimaryContainerBottomSheetFragment
import com.n1.moguchi.ui.fragment.parent.home.HomeParentFragment
import com.n1.moguchi.ui.fragment.switch_to_user.SwitchToChildBottomSheetFragment
import com.n1.moguchi.ui.fragment.switch_to_user.SwitchToParentBottomSheetFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as MoguchiBaseApplication).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        var currentProfileMode = viewModel.getProfileMode()
        navController.setGraph(graph, bundleCurrentProfileMode(currentProfileMode))

        when (currentProfileMode) {
            ProfileMode.PARENT_MODE -> {
                graph.setStartDestination(R.id.home_parent_fragment)
            }

            ProfileMode.CHILD_MODE -> {
                graph.setStartDestination(R.id.home_child_fragment)
            }

            ProfileMode.UNDEFINED -> {
                graph.setStartDestination(R.id.registrationFragment)
            }
        }

        supportFragmentManager.setFragmentResultListener(
            "changeProfileModeRequestKey",
            this
        ) { _, _ ->
            currentProfileMode = if (currentProfileMode == ProfileMode.PARENT_MODE) {
                with(viewModel) {
                    setProfileMode(ProfileMode.CHILD_MODE)
                    getProfileMode()
                }
            } else {
                with(viewModel) {
                    setProfileMode(ProfileMode.PARENT_MODE)
                    getProfileMode()
                }
            }
            setupBottomNavigationView(currentProfileMode)
            bundleCurrentProfileMode(currentProfileMode)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeParentFragment,
                R.id.homeChildFragment -> {
                    showUi()
                }

                else -> hideUi()
            }
        }

        var bundleForHomeFragment = Bundle()
        supportFragmentManager.setFragmentResultListener(
            "refreshRecyclerViewRequestKey",
            this
        ) { _, innerBundle ->
            bundleForHomeFragment = innerBundle
        }
        supportFragmentManager.setFragmentResult(
            "refreshGoalsListRequestKey",
            bundleForHomeFragment
        )
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val currentProfileMode = viewModel.getProfileMode()
        if (currentUser != null && currentProfileMode != ProfileMode.UNDEFINED) {
            if (currentProfileMode == ProfileMode.PARENT_MODE) {
                supportFragmentManager.commit {
                    replace(
                        R.id.fragment_container_view,
                        HomeParentFragment()
                    )
                }
            } else {
                supportFragmentManager.commit {
                    replace(
                        R.id.fragment_container_view,
                        HomeChildFragment()
                    )
                }
            }
        }
    }

    private fun bundleCurrentProfileMode(currentProfileMode: ProfileMode): Bundle? {
        val bundle = Bundle().apply {
            when (currentProfileMode) {
                ProfileMode.PARENT_MODE -> {
                    putString("profileMode", "parentMode")
                }

                ProfileMode.CHILD_MODE -> {
                    putString("profileMode", "childMode")
                }

                ProfileMode.UNDEFINED -> {
                }
            }
        }
        val intent = intent.putExtras(bundle)
        intent.putExtras(bundle)
        return intent.extras
    }

    private fun setupBottomNavigationView(currentProfileMode: ProfileMode) {
        binding.bottomNavigationView.setupWithNavController(navController)
        if (binding.bottomNavigationView.menu.isNotEmpty()) {
            binding.bottomNavigationView.menu.clear()
        }

        if (currentProfileMode == ProfileMode.PARENT_MODE) {
            binding.bottomNavigationView.inflateMenu(R.menu.bottom_menu_parent)
            binding.bottomNavigationView.menu.findItem(R.id.addGoal).setOnMenuItemClickListener {
                showBottomSheet(PrimaryContainerBottomSheetFragment(), GOAL_CREATION_INTENT_TAG)
                true
            }
            binding.bottomNavigationView.menu.findItem(R.id.switch_to_child)
                .setOnMenuItemClickListener {
                    showBottomSheet(
                        SwitchToChildBottomSheetFragment(),
                        SWITCH_TO_USER_INTENT_TAG,
                    )
                    true
                }
        } else {
            binding.bottomNavigationView.inflateMenu(R.menu.bottom_menu_child)
            binding.bottomNavigationView.menu.findItem(R.id.switch_to_parent)
                .setOnMenuItemClickListener {
                    showBottomSheet(
                        SwitchToParentBottomSheetFragment(),
                        SWITCH_TO_USER_INTENT_TAG
                    )
                    true
                }
        }
    }

    private fun showBottomSheet(
        bottomSheetFragment: BottomSheetDialogFragment,
        tag: String
    ) {
        val fragmentManager = supportFragmentManager
        fragmentManager.setFragmentResult(
            "primaryBottomSheetRequestKey",
            bundleOf("primaryBundleKey" to tag)
        )
        val childId = this.intent.getStringExtra("childId")
        Bundle().apply {
            putString("childId", childId)
            bottomSheetFragment.arguments = this
        }
        bottomSheetFragment.show(fragmentManager, tag)
    }

    private fun showUi() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideUi() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    companion object {
        private const val GOAL_CREATION_INTENT_TAG = "GoalCreationIntentMainActivity"
        const val SWITCH_TO_USER_INTENT_TAG = "SwitchToUserIntentMainActivity"
    }
}