package com.example.familyapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.familyapp.R
import com.example.familyapp.data.remote.model.Goal
import com.example.familyapp.databinding.CompletedGoalCardBinding

class CompletedGoalsRecyclerAdapter(private val completedGoalsList: List<Goal>) :
    RecyclerView.Adapter<ViewHolder>() {

    var onTasksHistoryClicked: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.completed_goal_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val completedGoal: Goal = completedGoalsList[position]
        (holder as CardViewHolder).bind(completedGoal)
    }

    override fun getItemCount(): Int {
        return completedGoalsList.size
    }

    inner class CardViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = CompletedGoalCardBinding.bind(itemView)
        private var goal: Goal? = null

        fun bind(goal: Goal) {
            this.goal = goal
            binding.goalTitle.text = goal.title
            binding.goalPointsLayout.goalPoints.text = itemView.context.getString(
                R.string.current_total_goal_points,
                goal.currentPoints,
                goal.totalPoints
            )
            binding.tasksHistoryButton.setOnClickListener {
                onTasksHistoryClicked.invoke(goal.goalId!!)
            }
        }
    }
}