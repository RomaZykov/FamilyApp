package com.n1.moguchi.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.GoalCardBinding
import com.n1.moguchi.ui.views.CustomShapesView

class GoalsRecyclerAdapter(
    private val goalsList: List<Goal>,
    private val tasksList: List<Task>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onGoalButtonClicked: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.goal_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val goal: Goal = goalsList[position]
        (holder as CardViewHolder).bind(goal)
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = GoalCardBinding.bind(itemView)
        private val context: Context = itemView.context
        private val customDrawableView: CustomShapesView = CustomShapesView(context)

        init {
            binding.root.background = customDrawableView
        }

        fun bind(goal: Goal) {
            binding.goalTitle.text = goal.title

            setProgression(goal.currentPoints, goal.totalPoints)

            val buttonText: TextView = binding.allTasksButton.root.getChildAt(0) as TextView
            val relatedTasks =
                tasksList.filter { it.goalOwnerId == goal.goalId && !it.taskCompleted }
            if (relatedTasks.size - 3 > 0) {
                buttonText.text =
                    context.getString(R.string.see_all_tasks_button_text, relatedTasks.size - 3)
            } else {
                buttonText.text = context.getString(R.string.go_to_tasks_button_text)
            }

            binding.tasksContainerLl.apply {
                for (i in 1..3) {
                    if (relatedTasks.size - i >= 0) {
                        val taskSmallItem =
                            LayoutInflater.from(context)
                                .inflate(R.layout.task_item, this, false)
                        taskSmallItem.backgroundTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.white_opacity_90)) // R.color.white_opacity_90
                        taskSmallItem.findViewById<TextView>(R.id.task_title).text =
                            relatedTasks[i - 1].title
                        taskSmallItem.rootView.findViewById<TextView>(R.id.task_points).text =
                            relatedTasks[i - 1].height.toString()
                        addView(taskSmallItem)
                    }
                }
            }

            binding.allTasksButton.root.setOnClickListener {
                onGoalButtonClicked?.invoke(goal.goalId!!)
            }
        }

        private fun setProgression(currentPoints: Int, totalPoints: Int) {
            binding.goalPointsLayout.root.findViewById<TextView>(R.id.goal_points).text =
                context.getString(
                    R.string.current_total_goal_points,
                    currentPoints,
                    totalPoints
                )
            binding.goalProgressBar.max = totalPoints
            binding.goalProgressBar.progress = currentPoints
        }
    }
}
