package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.TaskCreationCardBinding

private const val FOOTER_ADD_TASK_BUTTON = 1

class TaskSetupRecyclerAdapter :
    RecyclerView.Adapter<TaskSetupRecyclerAdapter.TaskCardViewHolder>() {

    var tasksCard: MutableList<Task> = ArrayList()
        set(value) {
            field = value
            if (field.size == 1) {
                notifyItemChanged(0)
            }
        }
    var onTaskSettingsClicked: (() -> Unit)? = null
    var onNewTaskAddClicked: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.task_creation_card,
            parent,
            false
        )
        return TaskCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskCardViewHolder, position: Int) {
        val taskEmptyCard = tasksCard[position]
        holder.bind(taskEmptyCard, position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return tasksCard.size + FOOTER_ADD_TASK_BUTTON
    }

    inner class TaskCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = TaskCreationCardBinding.bind(itemView)

        fun bind(task: Task, position: Int) {
            binding.deleteTaskButton.setOnClickListener {
                tasksCard.removeAt(position)
                notifyItemRemoved(position)
            }
            binding.taskSettingsButton.setOnClickListener {
                onTaskSettingsClicked?.invoke()
            }
        }
    }
}