package com.n1.moguchi.ui.adapter

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.EditableTaskItemBinding

class TasksRecyclerAdapter(
    private val relatedTasksList: List<Task>,
    private val isActiveTasks: Boolean,
    private val profileMode: String,
) : RecyclerView.Adapter<TasksRecyclerAdapter.EditableTaskViewHolder>() {

    var onTaskStatusChangedClicked: ((Task, Boolean) -> Unit)? = null
    var onTaskDeleteClicked: ((Task, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditableTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.editable_task_item, parent, false)
        return EditableTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditableTaskViewHolder, position: Int) {
        val task: Task = relatedTasksList[position]
        holder.bind(task, isActiveTasks)
    }

    override fun getItemCount(): Int {
        return relatedTasksList.size
    }

    inner class EditableTaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener {
        private val binding = EditableTaskItemBinding.bind(itemView)
        private var task: Task? = null

        fun bind(task: Task, isActiveTasks: Boolean) {
            this.task = task
            binding.taskTitle.text = task.title
            binding.taskPoints.text = task.height.toString()

            if (profileMode == "parentMode") {
                binding.taskSettingsButton.visibility = View.VISIBLE
                binding.taskSettingsButton.setOnClickListener {
                    showOptionsPopup(isActiveTasks)
                }
            } else {
                binding.taskSettingsButton.visibility = View.GONE
            }
        }

        private fun showOptionsPopup(isActiveTasks: Boolean) {
            val popUpMenu = PopupMenu(
                itemView.context,
                binding.taskSettingsButton,
                0,
                0,
                R.style.PopupMenuStyle
            )
            popUpMenu.setOnMenuItemClickListener(this)
            val inflater = popUpMenu.menuInflater
            if (isActiveTasks) {
                inflater.inflate(R.menu.menu_task_settings_done, popUpMenu.menu)
            } else {
                inflater.inflate(R.menu.menu_task_settings_not_done, popUpMenu.menu)
            }
            val spanTitle = SpannableString(popUpMenu.menu[1].title.toString())
            spanTitle.setSpan(
                ForegroundColorSpan(itemView.context.getColor(R.color.red)),
                0,
                spanTitle.length,
                0
            )
            popUpMenu.menu[1].title = spanTitle
            popUpMenu.setForceShowIcon(true)
            popUpMenu.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.task_done -> {
                    onTaskStatusChangedClicked?.invoke(task!!, isActiveTasks)
                    notifyItemRemoved(adapterPosition)
                }

                R.id.task_not_done -> {
                    onTaskStatusChangedClicked?.invoke(task!!, isActiveTasks)
                    notifyItemRemoved(adapterPosition)
                }

                R.id.delete -> {
                    onTaskDeleteClicked?.invoke(task!!, isActiveTasks)
                    notifyItemRemoved(adapterPosition)
                }
            }
            return true
        }
    }

    companion object {
        private const val PARENT_MODE = "parentMode"
        private const val CHILD_MODE = "childMode"
    }
}