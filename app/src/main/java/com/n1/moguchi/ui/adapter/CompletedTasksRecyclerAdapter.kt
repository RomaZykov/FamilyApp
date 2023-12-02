package com.n1.moguchi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.EditableTaskItemBinding

class CompletedTasksRecyclerAdapter :
    RecyclerView.Adapter<CompletedTasksRecyclerAdapter.CompletedTaskViewHolder>() {

    private val tasksList = mutableListOf(
        Task(title = "Прибраться в комнате 5 дней подряд", height = 2),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Сходить в магазин в пятницу", height = 1),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedTaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.editable_task_item, parent, false)
        return CompletedTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedTaskViewHolder, position: Int) {
        val task: Task = tasksList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class CompletedTaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener {

        private val binding = EditableTaskItemBinding.bind(itemView)
        private var task: Task? = null
        var context: Context = itemView.context

        fun bind(task: Task) {
            this.task = task
            binding.taskTitle.text = task.title
            binding.taskSettingsButton.visibility = View.VISIBLE
            binding.taskSettingsButton.setOnClickListener {
                showOptionsPopup()
            }
        }

        private fun showOptionsPopup() {
            val popup = PopupMenu(itemView.context, binding.taskSettingsButton)
            popup.setOnMenuItemClickListener(this)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.menu_child_completed_task_settings, popup.menu)
            popup.setForceShowIcon(true)
            popup.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.not_done -> {
                    TODO("Not yet implemented")
                }
            }
            return true
        }
    }

}
