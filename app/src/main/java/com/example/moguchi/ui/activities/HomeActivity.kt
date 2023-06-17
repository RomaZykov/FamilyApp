package com.example.moguchi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.moguchi.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.background = null

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
}