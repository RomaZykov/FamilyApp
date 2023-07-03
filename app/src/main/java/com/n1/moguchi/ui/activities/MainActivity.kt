package com.n1.moguchi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.n1.moguchi.R
import com.n1.moguchi.databinding.ActivityMainBinding
import com.n1.moguchi.ui.adapters.CardListAdapter
import com.n1.moguchi.ui.fragments.HomeFragment
import com.n1.moguchi.ui.fragments.SettingsFragment
import com.n1.moguchi.ui.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CardListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: MainActivityViewModel by viewModels()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        addFabClickListener()

        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.settingsFragment
                -> {
                    binding.bottomAppBarLayout.visibility = View.VISIBLE
                    binding.topAppBarLayout.visibility = View.VISIBLE
                    binding.addFab.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomAppBarLayout.visibility = View.GONE
                    binding.topAppBarLayout.visibility = View.GONE
                    binding.addFab.visibility = View.GONE
                }
            }
        }
        binding.bottomNavigationView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.homeFragment -> launchFragment(HomeFragment())
                R.id.settingsFragment -> launchFragment(SettingsFragment())
            }
        }
    }

    private fun addFabClickListener() {
        var isAllTabsVisible = false
        binding.addFab.setOnClickListener {
            if (!isAllTabsVisible) {
                binding.addTaskButton.visibility = View.VISIBLE
                binding.addTask.visibility = View.VISIBLE
                binding.addGoalButton.visibility = View.VISIBLE
                binding.addGoal.visibility = View.VISIBLE
                isAllTabsVisible = true
            } else {
                binding.addTaskButton.visibility = View.GONE
                binding.addTask.visibility = View.GONE
                binding.addGoalButton.visibility = View.GONE
                binding.addGoal.visibility = View.GONE
                isAllTabsVisible = false
            }
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}