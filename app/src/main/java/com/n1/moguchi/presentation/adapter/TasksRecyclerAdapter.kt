package com.n1.moguchi.presentation.adapter

import android.os.Build
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.databinding.EditableTaskItemBinding
import com.n1.moguchi.presentation.fragment.tasks.TasksMode

class TasksRecyclerAdapter(
    private val relatedTasksList: MutableList<Task>,
    private val tasksMode: TasksMode,
    private val isActiveTasks: Boolean
) : RecyclerView.Adapter<TasksRecyclerAdapter.EditableTaskViewHolder>() {

    // TODO - Dangerous code
    var updateTasksList: MutableList<Task> = mutableListOf()
        set(value) {
            field = value
            value.forEach {
                relatedTasksList.add(it)
            }
        }

    var onTaskStatusChangedClicked: (Task, Boolean?) -> Unit = { _, _ -> }
    var onTaskDeleteClicked: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditableTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.editable_task_item, parent, false)
        return EditableTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditableTaskViewHolder, position: Int) {
        val task: Task = relatedTasksList[position]
        holder.bind(task, tasksMode)
    }

    override fun getItemCount(): Int {
        return relatedTasksList.size
    }

    inner class EditableTaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener {
        private val binding = EditableTaskItemBinding.bind(itemView)
        private val context = itemView.context
        private var task: Task? = null

        fun bind(task: Task, tasksMode: TasksMode) {
            this.task = task
            binding.taskTitle.text = task.title
            binding.taskPoints.text = task.height.toString()
            if (task.onCheck) {
                binding.taskOnCheckStatusIcon.visibility = View.VISIBLE
            } else {
                binding.taskOnCheckStatusIcon.visibility = View.GONE
            }

            when (tasksMode) {
                TasksMode.ACTIVE_EDITABLE -> {
                    binding.taskSettingsButton.visibility = View.VISIBLE
                    binding.taskSettingsButton.setOnClickListener {
                        showOptionsPopup(tasksMode)
                    }
                }

                TasksMode.COMPLETED_EDITABLE -> {
                    binding.taskSettingsButton.visibility = View.VISIBLE
                    binding.taskSettingsButton.setOnClickListener {
                        showOptionsPopup(tasksMode)
                    }
                }

                TasksMode.CHECKABLE -> {
                    binding.taskSettingsButton.visibility = View.VISIBLE
                    binding.taskSettingsButton.setOnClickListener {
                        showOptionsPopup(tasksMode)
                    }
                }

                TasksMode.NON_EDITABLE -> {
                    binding.taskSettingsButton.visibility = View.GONE
                    if (task.taskCompleted) {
                        binding.taskCompletedIcon.visibility = View.VISIBLE
                    } else {
                        binding.taskCompletedIcon.visibility = View.GONE
                    }
                }
            }

            binding.taskOnCheckStatusIcon.setOnClickListener {
                // TODO - Add for old api versions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.tooltipText = context.getString(R.string.on_check_status)
                }
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.task_completed -> {
                    onTaskStatusChangedClicked.invoke(task!!, isActiveTasks)
                    relatedTasksList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }

                R.id.task_not_completed -> {
                    onTaskStatusChangedClicked.invoke(task!!, isActiveTasks)
                    relatedTasksList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }

                R.id.check_completed_task -> {
                    onTaskStatusChangedClicked.invoke(task!!, null)
                    notifyItemChanged(adapterPosition)
                }

                R.id.delete -> {
                    onTaskDeleteClicked.invoke(task!!)
                    relatedTasksList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
            }
            return true
        }

        private fun showOptionsPopup(tasksMode: TasksMode) {
            val popUpMenu = PopupMenu(
                itemView.context,
                binding.taskSettingsButton,
                0,
                0,
                R.style.PopupMenuStyle
            )
            popUpMenu.setOnMenuItemClickListener(this)
            val inflater = popUpMenu.menuInflater
            when (tasksMode) {
                TasksMode.ACTIVE_EDITABLE -> {
                    inflater.inflate(R.menu.menu_task_settings_done, popUpMenu.menu)
                }

                TasksMode.COMPLETED_EDITABLE -> {
                    inflater.inflate(R.menu.menu_task_settings_not_done, popUpMenu.menu)
                }

                TasksMode.CHECKABLE -> {
                    inflater.inflate(R.menu.menu_child_completed_task, popUpMenu.menu)
                }

                TasksMode.NON_EDITABLE -> {
                    binding.taskSettingsButton.visibility = View.GONE
                }
            }
            if (popUpMenu.menu.size > 1) {
                val spanTitle = SpannableString(popUpMenu.menu[1].title.toString())
                spanTitle.setSpan(
                    ForegroundColorSpan(itemView.context.getColor(R.color.red)),
                    0,
                    spanTitle.length,
                    0
                )
                popUpMenu.menu[1].title = spanTitle
            }
            popUpMenu.setForceShowIcon(true)
            popUpMenu.show()
        }
    }
}