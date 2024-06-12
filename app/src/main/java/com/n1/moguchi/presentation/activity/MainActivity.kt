package com.n1.moguchi.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.ktx.appCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.ProfileMode
import com.n1.moguchi.databinding.ActivityMainBinding
import com.n1.moguchi.presentation.ViewModelFactory
import com.n1.moguchi.presentation.fragment.parent.PrimaryContainerBottomSheetFragment
import com.n1.moguchi.presentation.fragment.switch_to_user.SwitchToChildBottomSheetFragment
import com.n1.moguchi.presentation.fragment.switch_to_user.SwitchToParentBottomSheetFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var auth: FirebaseAuth
    private var currentProfileMode: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as MoguchiBaseApplication).appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        Firebase.appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )

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

        lifecycleScope.launch {
            viewModel.getUserPrefs().collect {
                currentProfileMode = it.currentProfileMode
                when (currentProfileMode) {
                    "parent_mode" -> {
                        graph.setStartDestination(R.id.home_parent_fragment)
                    }

                    "child_mode" -> {
                        graph.setStartDestination(R.id.home_child_fragment)
                    }

                    "undefined" -> {
                        graph.setStartDestination(R.id.registrationFragment)
                    }
                }
                navController.graph = graph
                setupBottomNavigationView(currentProfileMode!!)
            }
        }

        supportFragmentManager.setFragmentResultListener(
            "changeProfileModeRequestKey",
            this
        ) { _, _ ->
            lifecycleScope.launch {
                viewModel.getUserPrefs().collect {
                    currentProfileMode = it.currentProfileMode
                }
                if (currentProfileMode == "parent_mode") {
                    viewModel.updateUserPrefs(ProfileMode.PARENT_MODE)
                } else {
                    viewModel.updateUserPrefs(ProfileMode.CHILD_MODE)
                }
            }
            currentProfileMode?.let { setupBottomNavigationView(it) }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home_parent_fragment,
                R.id.home_child_fragment -> {
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

    private fun setupBottomNavigationView(currentProfileMode: String) {
        binding.bottomNavigationView.setupWithNavController(navController)
        if (binding.bottomNavigationView.menu.isNotEmpty()) {
            binding.bottomNavigationView.menu.clear()
        }

        if (currentProfileMode == "parent_mode") {
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