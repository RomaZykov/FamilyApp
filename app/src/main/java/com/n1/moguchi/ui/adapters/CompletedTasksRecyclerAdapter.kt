package com.n1.moguchi.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.CompletedTaskItemBinding

class CompletedTasksRecyclerAdapter :
    RecyclerView.Adapter<CompletedTasksRecyclerAdapter.CompletedTaskViewHolder>() {

    private val tasksList = mutableListOf(
        Task(title = "Прибраться в комнате 5 дней подряд", height = 2),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Прибраться в комнате 5 дней подряд", height = 2)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedTaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.completed_task_item, parent, false)
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
        RecyclerView.ViewHolder(itemView) {

        private val binding = CompletedTaskItemBinding.bind(itemView)
        private var task: Task? = null
        var context: Context = itemView.context

        fun bind(task: Task) {
            this.task = task
            binding.completedIc.visibility = View.VISIBLE
            binding.completedTaskTitle.text = task.title
        }
    }
}
