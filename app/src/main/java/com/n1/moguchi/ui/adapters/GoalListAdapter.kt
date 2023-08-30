package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.databinding.GoalsTasksCardBinding

class GoalListAdapter : RecyclerView.Adapter<GoalListAdapter.CardViewHolder>() {

    private val goalsList: List<Goal>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = GoalsTasksCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val goalContainer = goalsList?.get(position)
//        holder.binding.goalProgressbar
        holder.binding.counter.text
        holder.binding.goalTitle.text
        holder.binding.linearLayoutTasksContainer
        holder.binding.allTasksButton
    }

    override fun getItemCount(): Int {
        return goalsList?.size ?: 0
    }

    inner class CardViewHolder(val binding: GoalsTasksCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}