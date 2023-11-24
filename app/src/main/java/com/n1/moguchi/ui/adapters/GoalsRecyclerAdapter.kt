package com.n1.moguchi.ui.adapters

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
import com.n1.moguchi.databinding.ZMockGoalCardBinding
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.fragments.TasksFragment

class GoalsRecyclerAdapter : RecyclerView.Adapter<GoalsRecyclerAdapter.CardViewHolder>() {

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
    private val goalsList = mutableListOf(
        Goal(
            goalId = null,
            parentOwnerId = null,
            childOwnerId = null,
            taskList = tasksList,
            title = "Велосипед",
            height = 5, false
        ),
        Goal(
            goalId = null,
            parentOwnerId = null,
            childOwnerId = null,
            taskList = tasksList,
            title = "Приставка",
            height = 3, false
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.z_mock_goal_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val goal: Goal = goalsList[position]
        holder.bind(goal)
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val binding = ZMockGoalCardBinding.bind(itemView)
        private var goal: Goal? = null
        var context: Context = itemView.context

        fun bind(goal: Goal) {
            this.goal = goal
            binding.goalTitle.text = "Велосипед"
            binding.tasksContainerLl.apply {
                for (i in 0 until 3) {
                    val taskSmallItem =
                        LayoutInflater.from(context).inflate(R.layout.small_task_item, this, false)
                    taskSmallItem.setBackgroundColor(resources.getColor(R.color.white_opacity_90))
                    taskSmallItem.findViewById<TextView>(R.id.task_title).text = goal.taskList!![i].title
                    addView(taskSmallItem, 0)
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