package com.n1.moguchi.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Goal
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.ZMockCompletedGoalCardBinding
import com.n1.moguchi.ui.fragments.parent.TasksFragment

class CompletedGoalsListRecyclerAdapter :
    RecyclerView.Adapter<CompletedGoalsListRecyclerAdapter.CardViewHolder>() {

    private val tasksList = mutableListOf(
        Task(title = "Прибраться в комнате 5 дней подряд", height = 2),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Сходить в магазин в пятницу", height = 1),
        Task(title = "Написать контрольную минимум на 4", height = 3),
        Task(title = "Сходить в магазин в пятницу", height = 1)
    )
    private val goalsList = mutableListOf(
        Goal(
            goalId = null,
            parentOwnerId = null,
            childOwnerId = null,
            taskList = tasksList,
            title = "Новая игра на компьютер или приставку",
            height = 5, false
        ),
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
            title = "Сноуборд",
            height = 10, false
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.z_mock_completed_goal_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val completedGoal: Goal = goalsList[position]
        holder.bind(completedGoal)
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val binding = ZMockCompletedGoalCardBinding.bind(itemView)
        private var goal: Goal? = null
        var context: Context = itemView.context

        fun bind(goal: Goal) {
            this.goal = goal
            binding.goalTitle.text = goal.title
            binding.allCompletedTasksButton.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val fragmentActivity = v.context as FragmentActivity
            fragmentActivity.supportFragmentManager.commit {
                replace(android.R.id.content, TasksFragment())
                setReorderingAllowed(true)
                addToBackStack(TAG)
            }
        }
    }

    companion object {
        const val TAG = "GoalItem"
    }
}