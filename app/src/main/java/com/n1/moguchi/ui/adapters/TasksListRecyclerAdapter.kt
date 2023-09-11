package com.n1.moguchi.ui.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.EditableTaskItemBinding

class TasksListRecyclerAdapter : RecyclerView.Adapter<TasksListRecyclerAdapter.EditableTaskViewHolder>() {

    private val tasksList = mutableListOf(
        Task(title = "Прибраться в комнате 5 дней подряд", height = 2),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Прибраться в комнате 5 дней подряд", height = 2)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditableTaskViewHolder {
        TODO()
    }

    override fun onBindViewHolder(holder: EditableTaskViewHolder, position: Int) {
        TODO()
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class EditableTaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = EditableTaskItemBinding.bind(itemView)
        private var task: Task? = null
        var context: Context = itemView.context

//        init {
//            binding.taskSettingsButton.setOnClickListener(this)
//        }

        fun bind(task: Task) {
            this.task = task

            binding.taskSettingsButton.setOnClickListener {
                // childFragmentManager?
            }
        }
    }
}
