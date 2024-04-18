package com.n1.moguchi.ui.fragment.parent.task_creation

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.databinding.CreationSectionFooterBinding
import com.n1.moguchi.databinding.TaskCardBinding

private const val FOOTER_ADD_TASK_BUTTON = 1

class TaskCreationRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var tasksCardList: MutableList<Task> = ArrayList()
    var onTaskSettingsClicked: (() -> Unit)? = null
    var onTaskUpdate: ((Task, Boolean) -> Unit)? = null
    var onNewTaskAddClicked: (() -> Unit)? = null
    var onTaskDeleteClicked: ((Task, Int) -> Unit)? = null
    var onCardsStatusUpdate: ((Boolean) -> Unit)? = null

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
            if (tasksCardList.size == 1) {
                binding.deleteTaskButton.visibility = View.GONE
            } else {
                binding.deleteTaskButton.visibility = View.VISIBLE
            }
            binding.taskHeight.text = task.height.toString()
            binding.taskNameEditText.setText(task.title)
            binding.taskNameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(taskName: Editable?) {
                    tasksCardList[adapterPosition].title = taskName.toString()
                    if (taskName.toString().isEmpty()) {
                        binding.taskNameEditText.error =
                            context.getString(R.string.not_all_conditions_is_done)
                    }
                    notifyItemChanged(itemCount - FOOTER_ADD_TASK_BUTTON)
                }
            })

            binding.deleteTaskButton.setOnClickListener {
                onTaskDeleteClicked?.invoke(task, adapterPosition)
                tasksCardList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                notifyItemChanged(itemCount - 1)
            }

//            binding.taskSettingsButton.setOnClickListener {
//                onTaskSettingsClicked?.invoke()
//            }

            binding.increaseButton.setOnClickListener {
                if (task.height < MAX_TASK_HEIGHT) {
                    onTaskUpdate?.invoke(task, true)
                    binding.taskHeight.text = (++task.height).toString()
                }
            }
            binding.decreaseButton.setOnClickListener {
                if (task.height > MIN_TASK_HEIGHT) {
                    onTaskUpdate?.invoke(task, false)
                    binding.taskHeight.text = (--task.height).toString()
                }
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CreationSectionFooterBinding.bind(itemView)
        var context: Context = itemView.context

        init {
            binding.addChildButton.text = context.getString(R.string.add_task_text_button)
        }

        fun bind() {
            if (tasksCardList.all {
                    it.title.isNotEmpty() && tasksCardList.size == itemCount - FOOTER_ADD_TASK_BUTTON
                }) {
                onCardsStatusUpdate?.invoke(true)
                with(binding.addChildButton) {
                    isEnabled = true
                    setTextColor(context.getColorStateList(R.color.black))
                    iconTint = context.getColorStateList(R.color.black)
                    backgroundTintList = context.getColorStateList(R.color.white)
                }
                itemView.setOnClickListener {
                    onNewTaskAddClicked?.invoke()
                }
            } else {
                onCardsStatusUpdate?.invoke(false)
                with(binding.addChildButton) {
                    isEnabled = false
                    backgroundTintList = context.getColorStateList(R.color.white_opacity_70)
                    setTextColor(context.getColorStateList(R.color.black_opacity_70))
                    iconTint = context.getColorStateList(R.color.black_opacity_70)
                }
            }
        }
    }

    companion object {
        const val MAX_POOL_SIZE = 0
        const val VIEW_TYPE_TASK_CARD = 100
        const val VIEW_TYPE_FOOTER = 101

        private const val MIN_TASK_HEIGHT = 1
        private const val MAX_TASK_HEIGHT = 3
    }
}