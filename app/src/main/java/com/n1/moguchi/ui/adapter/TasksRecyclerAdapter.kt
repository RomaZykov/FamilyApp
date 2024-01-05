package com.n1.moguchi.ui.adapter

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

class TasksRecyclerAdapter(private val relatedTasksList: List<Task>, isActive: Boolean) : RecyclerView.Adapter<TasksRecyclerAdapter.EditableTaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditableTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.editable_task_item, parent, false)
        return EditableTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditableTaskViewHolder, position: Int) {
        val task: Task = relatedTasksList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return relatedTasksList.size
    }

    inner class EditableTaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), PopupMenu.OnMenuItemClickListener, View.OnClickListener {

        private val binding = EditableTaskItemBinding.bind(itemView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(task: Task) {
            binding.taskTitle.text = task.title
            binding.taskPoints.text = task.height.toString()
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

//        private fun showOptionsPopup() {
//            val popup = PopupMenu(itemView.context, binding.taskSettingsButton)
//            popup.setOnMenuItemClickListener(this)
//            val inflater = popup.menuInflater
//            inflater.inflate(R.menu.menu_child_completed_task_settings, popup.menu)
//            popup.setForceShowIcon(true)
//            popup.show()
//        }
//
//        override fun onMenuItemClick(item: MenuItem?): Boolean {
//            when (item?.itemId) {
//                R.id.not_done -> {
//                    TODO("Not yet implemented")
//                }
//            }
//            return true
//        }
    }

    companion object {
        private const val TAG = "TasksRecyclerAdapter"
    }
}