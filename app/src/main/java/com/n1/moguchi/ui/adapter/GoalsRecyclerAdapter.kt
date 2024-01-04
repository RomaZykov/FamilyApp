package com.n1.moguchi.ui.adapter

import android.content.Context
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
    private val tasksByGoalList: List<Task>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onGoalButtonClicked: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.goal_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val goal: Goal = goalsList[position]
        (holder as CardViewHolder).bind(goal, tasksByGoalList)
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

        fun bind(goal: Goal, tasksByGoalList: List<Task>) {
            binding.goalTitle.text = goal.title
            binding.goalPointsLayout.root.findViewById<TextView>(R.id.goal_points).text =
                context.getString(
                    R.string.current_total_goal_points,
                    goal.currentPoints,
                    goal.totalPoints
                )

            val buttonText: TextView = binding.allTasksButton.root.getChildAt(0) as TextView
            if (tasksByGoalList.size - 3 >= 0) {
                buttonText.text = context.getString(R.string.see_all_tasks_button_text, tasksByGoalList.size)
            } else {
                buttonText.text = context.getString(R.string.go_to_tasks_button_text)
            }

            binding.allTasksButton.root.setOnClickListener {
                onGoalButtonClicked?.invoke(goal.goalId!!)
            }

            binding.tasksContainerLl.apply {
                for (i in 1..3) {
                    if (tasksByGoalList.size - i >= 0) {
                        val taskSmallItem =
                            LayoutInflater.from(context)
                                .inflate(R.layout.task_item, this, false)
                        taskSmallItem.setBackgroundColor(resources.getColor(R.color.white_opacity_90))
                        taskSmallItem.findViewById<TextView>(R.id.task_title).text =
                            tasksByGoalList[i - 1].title
                        taskSmallItem.rootView.findViewById<TextView>(R.id.task_points).text =
                            tasksByGoalList[i - 1].height.toString()
                        addView(taskSmallItem)
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "GoalItem"
    }
}