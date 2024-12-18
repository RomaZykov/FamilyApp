package com.example.familyapp.ui.fragment.parent.task_creation

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familyapp.R
import com.example.familyapp.data.remote.model.Task
import com.example.familyapp.databinding.CreationSectionFooterBinding
import com.example.familyapp.databinding.TaskCardBinding

class TaskCreationRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tasksCardList: MutableList<Task> = mutableListOf()
    var onTaskSettingsClicked: () -> Unit = {}
    var onTaskUpdate: (Task, Boolean) -> Unit = { _, _ -> }
    var onNewTaskAddClicked: () -> Unit = {}
    var onTaskDeleteClicked: (Task, Int) -> Unit = { _, _ -> }
    var onCardsStatusUpdate: (Boolean) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TASK_CARD -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.task_card,
                    parent,
                    false
                )
                TaskCardViewHolder(view)
            }

            VIEW_TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.creation_section_footer,
                    parent,
                    false
                )
                FooterViewHolder(view)
            }

            else -> {
                TODO()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_TASK_CARD -> (holder as TaskCardViewHolder).bind(tasksCardList[position])
            VIEW_TYPE_FOOTER -> (holder as FooterViewHolder).bind()
            else -> throw RuntimeException("Unknown viewType: ${holder.itemViewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == tasksCardList.size) {
            VIEW_TYPE_FOOTER
        } else {
            VIEW_TYPE_TASK_CARD
        }
    }

    override fun getItemCount(): Int {
        return tasksCardList.size + FOOTER_ADD_TASK_BUTTON
    }

    inner class TaskCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context = itemView.context
        private val binding = TaskCardBinding.bind(itemView)

        fun bind(task: Task) {
            tasksCardList[absoluteAdapterPosition] = task
            if (tasksCardList.size == 1) {
                binding.deleteTaskButton.visibility = View.GONE
            } else {
                binding.deleteTaskButton.visibility = View.VISIBLE
            }
            binding.taskHeight.text = tasksCardList[absoluteAdapterPosition].height.toString()
            binding.taskNameEditText.setText(tasksCardList[absoluteAdapterPosition].title)
            binding.taskNameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(taskName: Editable?) {
                    tasksCardList[absoluteAdapterPosition] = task.copy(title = taskName.toString())
                    if (taskName.toString().isEmpty()) {
                        binding.taskNameEditText.error =
                            context.getString(R.string.not_all_conditions_is_done)
                    }
                    notifyItemChanged(itemCount - FOOTER_ADD_TASK_BUTTON)
                }
            })

            binding.deleteTaskButton.setOnClickListener {
                onTaskDeleteClicked.invoke(tasksCardList[absoluteAdapterPosition], absoluteAdapterPosition)
                tasksCardList.removeAt(absoluteAdapterPosition)
                notifyItemRemoved(absoluteAdapterPosition)
                notifyItemChanged(itemCount - 1)
            }

            binding.increaseButton.setOnClickListener {
                if (tasksCardList[absoluteAdapterPosition].height < MAX_TASK_HEIGHT) {
                    updateTask(tasksCardList[absoluteAdapterPosition], true)
                }
            }
            binding.decreaseButton.setOnClickListener {
                if (tasksCardList[absoluteAdapterPosition].height > MIN_TASK_HEIGHT) {
                    updateTask(tasksCardList[absoluteAdapterPosition], false)
                }
            }
        }

        private fun updateTask(task: Task, shouldIncreasePoints: Boolean) {
            val newTaskHeight = if (shouldIncreasePoints) task.height + 1 else task.height - 1
            tasksCardList[absoluteAdapterPosition] = task.copy(height = newTaskHeight)
            onTaskUpdate.invoke(tasksCardList[absoluteAdapterPosition], shouldIncreasePoints)
            binding.taskHeight.text = (tasksCardList[absoluteAdapterPosition].height).toString()
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CreationSectionFooterBinding.bind(itemView)
        private val context: Context = itemView.context

        init {
            binding.addChildButton.text = context.getString(R.string.add_task_text_button)
        }

        fun bind() {
            if (tasksCardList.all { allTasksValid(it) }) {
                onCardsStatusUpdate.invoke(true)
                with(binding.addChildButton) {
                    isEnabled = true
                    setTextColor(context.getColorStateList(R.color.black))
                    iconTint = context.getColorStateList(R.color.black)
                    backgroundTintList = context.getColorStateList(R.color.white)
                }
                itemView.setOnClickListener {
                    onNewTaskAddClicked.invoke()
                }
            } else {
                onCardsStatusUpdate.invoke(false)
                with(binding.addChildButton) {
                    isEnabled = false
                    backgroundTintList = context.getColorStateList(R.color.white_opacity_70)
                    setTextColor(context.getColorStateList(R.color.black_opacity_70))
                    iconTint = context.getColorStateList(R.color.black_opacity_70)
                }
            }
        }

        private fun allTasksValid(it: Task) =
            it.title.isNotEmpty() && tasksCardList.size == itemCount - FOOTER_ADD_TASK_BUTTON
    }

    companion object {
        const val MAX_POOL_SIZE = 0
        const val VIEW_TYPE_TASK_CARD = 100
        const val VIEW_TYPE_FOOTER = 101

        private const val MIN_TASK_HEIGHT = 1
        private const val MAX_TASK_HEIGHT = 3
        private const val FOOTER_ADD_TASK_BUTTON = 1
    }
}