package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.databinding.TaskCreationCardBinding
import com.n1.moguchi.ui.TaskSettingsClickListener

class TaskSetupAdapter(private val tasksCard: MutableList<View>, private val taskSettingsClickListener: TaskSettingsClickListener) :
    RecyclerView.Adapter<TaskSetupAdapter.TaskCardViewHolder>() {

    inner class TaskCardViewHolder(val binding: TaskCreationCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCardViewHolder {
        val taskCard = TaskCreationCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskCardViewHolder(taskCard)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: TaskCardViewHolder, position: Int) {
        val taskEmptyCard = tasksCard[position]
        holder.binding.deleteTaskButton.setOnClickListener {
            tasksCard.removeAt(position)
            this.notifyItemRemoved(position)
        }
        holder.binding.taskSettingsButton.setOnClickListener {
            taskSettingsClickListener.onTaskSettingsItemClick(taskEmptyCard)
        }
    }

    override fun getItemCount(): Int {
        return tasksCard.size
    }
}