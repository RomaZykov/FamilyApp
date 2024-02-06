package com.n1.moguchi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.databinding.CompletedGoalCardBinding

class CompletedGoalsRecyclerAdapter(private val goalsList: List<Goal>) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.completed_goal_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val completedGoal: Goal = goalsList[position]
        (holder as CardViewHolder).bind(completedGoal)
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    inner class CardViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = CompletedGoalCardBinding.bind(itemView)
        private var goal: Goal? = null
        var context: Context = itemView.context

        fun bind(goal: Goal) {
            this.goal = goal
            binding.goalTitle.text = goal.title
//            binding.allCompletedTasksButton.setOnClickListener(this)
        }
    }
}