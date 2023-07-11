package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.databinding.FragmentGoalsTasksContainerBinding
import com.n1.moguchi.data.models.Goal

class CardListAdapter() : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {

    private val goalsList: List<Goal>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = FragmentGoalsTasksContainerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val goalContainer = goalsList?.get(position)
        holder.binding.goalTitle.text
        holder.binding.taskTitle.text
        holder.binding.emptyTasksContainer.text
    }

    override fun getItemCount(): Int {
        return goalsList?.size ?: 0
    }

    inner class CardViewHolder(val binding: FragmentGoalsTasksContainerBinding) :
        RecyclerView.ViewHolder(binding.root)
}