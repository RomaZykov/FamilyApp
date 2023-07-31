package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.TaskCreationCardBinding

class TaskListAdapter() : RecyclerView.Adapter<TaskListAdapter.CardViewHolder>() {

    private val tasksList: List<Task>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = TaskCreationCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val tasksContainer = tasksList?.get(position)
        holder.binding.taskNameEditText
        holder.binding.increaseButton
        holder.binding.decreaseButton
    }

    override fun getItemCount(): Int {
        return tasksList?.size ?: 0
    }

    inner class CardViewHolder(val binding: TaskCreationCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}