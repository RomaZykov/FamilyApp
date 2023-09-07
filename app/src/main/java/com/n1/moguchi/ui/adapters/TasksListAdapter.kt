package com.n1.moguchi.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.databinding.ZMockMainGoalCardBinding

class TasksListAdapter(private val tasksCard: MutableList<View>) :
    RecyclerView.Adapter<TasksListAdapter.SmallTaskCardViewHolder>() {

    inner class SmallTaskCardViewHolder(val binding: ZMockMainGoalCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallTaskCardViewHolder {
        TODO()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: SmallTaskCardViewHolder, position: Int) {
        val taskSmallCard = tasksCard[position]
        holder.binding.tasksContainerLl
    }

    override fun getItemCount(): Int {
        return tasksCard.size
    }
}