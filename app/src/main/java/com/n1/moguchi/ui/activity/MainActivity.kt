package com.n1.moguchi.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.MoguchiBaseApplication
import com.n1.moguchi.R
import com.n1.moguchi.data.models.ProfileMode
import com.n1.moguchi.databinding.ActivityMainBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.fragment.parent.PrimaryBottomSheetFragment
import com.n1.moguchi.ui.fragment.switch_to_user.SwitchToChildBottomSheetFragment
import com.n1.moguchi.ui.fragment.switch_to_user.SwitchToParentBottomSheetFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

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

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]

        var currentProfileMode = viewModel.getProfileMode()
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
        }
        setupBottomNavigationView(currentProfileMode)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeParentFragment,
                R.id.homeChildFragment -> {
                    showUi()
                }

                else -> hideUi()
            }
        }
    }

    private fun setupBottomNavigationView(currentProfileMode: ProfileMode) {
        binding.bottomNavigationView.setupWithNavController(navController)
        if (binding.bottomNavigationView.menu.isNotEmpty()) {
            binding.bottomNavigationView.menu.clear()
        }

        if (currentProfileMode == ProfileMode.PARENT_MODE) {
            binding.bottomNavigationView.inflateMenu(R.menu.bottom_menu_parent)
            binding.bottomNavigationView.menu.findItem(R.id.addGoal).setOnMenuItemClickListener {
                showBottomSheet(PrimaryBottomSheetFragment(), GOAL_CREATION_INTENT_TAG)
                true
            }
            binding.bottomNavigationView.menu.findItem(R.id.switch_to_child)
                .setOnMenuItemClickListener {
                    showBottomSheet(
                        SwitchToChildBottomSheetFragment(),
                        SWITCH_TO_USER_INTENT_TAG
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
        fragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to tag))
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