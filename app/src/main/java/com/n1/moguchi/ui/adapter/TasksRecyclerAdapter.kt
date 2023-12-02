package com.n1.moguchi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.EditableTaskItemBinding
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.fragment.child.TaskConfirmationBottomSheetFragment

class TasksRecyclerAdapter : RecyclerView.Adapter<TasksRecyclerAdapter.EditableTaskViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditableTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.editable_task_item, parent, false)
        return EditableTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditableTaskViewHolder, position: Int) {
        val task: Task = tasksList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class EditableTaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener, View.OnClickListener {

        private val binding = EditableTaskItemBinding.bind(itemView)
        private var task: Task? = null
        var context: Context = itemView.context

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            this.task = task

            binding.taskTitle.text = task.title
            if (MainActivity.isParentProfile) {
                binding.taskSettingsButton.visibility = View.VISIBLE
                binding.taskSettingsButton.setOnClickListener {
                    showOptionsPopup()
                }
            } else {
                binding.taskSettingsButton.visibility = View.GONE
            }
        }

        private fun showOptionsPopup() {
            val popup = PopupMenu(itemView.context, binding.taskSettingsButton)
            popup.setOnMenuItemClickListener(this)
            val inflater = popup.menuInflater
            inflater.inflate(R.menu.menu_task_settings, popup.menu)
            popup.setForceShowIcon(true)
            popup.show()
        }

        override fun onClick(v: View) {
            val fragmentActivity = v.context as FragmentActivity
            val fragmentManager = fragmentActivity.supportFragmentManager
            val modalBottomSheet = TaskConfirmationBottomSheetFragment()
            modalBottomSheet.show(fragmentManager, TAG)
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.done -> {
                    TODO("Not yet implemented")
                }

                R.id.delete -> {
                    TODO("Not yet implemented")
                }
            }
            return true
        }
    }

    companion object {
        private const val TAG = "TasksRecyclerAdapter"
    }
}
