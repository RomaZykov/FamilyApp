package com.n1.moguchi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.GoalCardBinding
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.fragment.TasksFragment
import com.n1.moguchi.ui.views.CustomShapesView

class GoalsRecyclerAdapter(
    private val goalsList: List<Goal>,
    private val tasksByGoalList: List<Task>
) : RecyclerView.Adapter<GoalsRecyclerAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.goal_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val goal: Goal = goalsList[position]
        holder.bind(goal, tasksByGoalList)
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val binding = GoalCardBinding.bind(itemView)
        private val context: Context = itemView.context
        private val customDrawableView: CustomShapesView = CustomShapesView(context)

        init {
            binding.root.background = customDrawableView
        }

        fun bind(goal: Goal, tasksByGoalList: List<Task>) {
            binding.goalTitle.text = goal.title
            val buttonText: TextView = binding.allTasksButton.getChildAt(0) as TextView
            buttonText.text = "Смотреть 5 задач(у)"
            binding.tasksContainerLl.apply {
                for (i in 1..3) {
                    if (tasksByGoalList.size - i >= 0) {
                        val taskSmallItem =
                            LayoutInflater.from(context)
                                .inflate(R.layout.small_task_item, this, false)
                        taskSmallItem.setBackgroundColor(resources.getColor(R.color.white_opacity_90))
                        taskSmallItem.findViewById<TextView>(R.id.task_title).text =
                            tasksByGoalList[i - 1].title
                        addView(taskSmallItem)
                    }
                }
            }
            binding.allTasksButton.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val mainActivity = v.context as MainActivity
            mainActivity.supportFragmentManager.commit {
                replace(android.R.id.content, TasksFragment())
                setReorderingAllowed(true)
            }
        }
    }

    companion object {
        const val TAG = "GoalItem"
    }
}