package com.n1.moguchi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.n1.moguchi.R
import com.n1.moguchi.databinding.ActivityMainBinding
import com.n1.moguchi.ui.fragments.parent.PrimaryBottomSheetFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.homeFragment, R.id.chooseChildrenFragment),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
//        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.menu.findItem(R.id.addGoal).setOnMenuItemClickListener {
            showGoalAndTaskCreationBottomSheet()
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    showUi()
                }
                R.id.chooseChildrenFragment -> {
                    showUi()
                }
                else -> hideUi()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun showGoalAndTaskCreationBottomSheet() {
        val fragmentManager = supportFragmentManager
        val modalBottomSheet = PrimaryBottomSheetFragment()
        val mainActivityTag = TAG
        fragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to mainActivityTag))
        modalBottomSheet.show(fragmentManager, TAG)
    }

    private fun showUi() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideUi() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}