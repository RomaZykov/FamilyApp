package com.n1.moguchi.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isNotEmpty
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.ActivityMainBinding
import com.n1.moguchi.ui.fragment.parent.PrimaryBottomSheetFragment
import com.n1.moguchi.ui.fragment.parent.SwitchToChildBottomSheetFragment

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        setupBottomNavigationView()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.parentHomeFragment,
                R.id.homeChildFragment -> {
                    showUi()
                }
                else -> hideUi()
            }
        }
    }

    fun setupBottomNavigationView() {
        binding.bottomNavigationView.setupWithNavController(navController)
        if (binding.bottomNavigationView.menu.isNotEmpty()) {
            binding.bottomNavigationView.menu.clear()
        }
        if (isParentProfile) {
            binding.bottomNavigationView.inflateMenu(R.menu.bottom_menu_parent)
            binding.bottomNavigationView.menu.findItem(R.id.addGoal).setOnMenuItemClickListener {
                showBottomSheet(PrimaryBottomSheetFragment())
                true
            }
            binding.bottomNavigationView.menu.findItem(R.id.switch_to_child)
                .setOnMenuItemClickListener {
                    showBottomSheet(SwitchToChildBottomSheetFragment())
                    true
                }
        } else {
            binding.bottomNavigationView.inflateMenu(R.menu.bottom_menu_child)
        }
    }

    private fun showBottomSheet(bottomSheetFragment: BottomSheetDialogFragment) {
        val fragmentManager = supportFragmentManager
        val mainActivityTag = TAG
        fragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to mainActivityTag))
        bottomSheetFragment.show(fragmentManager, TAG)
    }

    private fun showUi() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideUi() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    companion object {
        const val TAG = "MainActivity"
        var isParentProfile = true
    }
}

