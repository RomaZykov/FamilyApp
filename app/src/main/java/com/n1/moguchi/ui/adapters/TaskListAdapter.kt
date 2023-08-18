package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.databinding.TaskCreationCardBinding

class TaskListAdapter(private val tasksCard: MutableList<View>) :
    RecyclerView.Adapter<TaskListAdapter.CardViewHolder>() {

    inner class CardViewHolder(val binding: TaskCreationCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val taskCard = TaskCreationCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(taskCard)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val task = tasksCard[position]
        holder.binding.deleteTaskButton.setOnClickListener {
            tasksCard.removeAt(position)
            this.notifyItemRemoved(position)
        }
        holder.binding.taskSettingsButton.setOnClickListener {view ->
//            view.findNavController().navigate(R.id.)
        }
    }

    override fun getItemCount(): Int {
        return tasksCard.size
    }
}