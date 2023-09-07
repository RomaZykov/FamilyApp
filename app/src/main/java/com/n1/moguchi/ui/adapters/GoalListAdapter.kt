package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Task
import com.n1.moguchi.databinding.ZMockMainGoalCardBinding

class GoalListAdapter : RecyclerView.Adapter<GoalListAdapter.CardViewHolder>() {

    val goalsList = mutableListOf<View>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val goalCard = ZMockMainGoalCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(goalCard)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val goal = goalsList[position]
//        holder.binding.goalProgressbar
        holder.binding.goalTitle.text
        holder.binding.tasksContainerLl.apply {
            val tasksList = mutableListOf<Task>(
                Task(title = "Прибраться в комнате 5 дней подряд", height = 2),
                Task(title = "Сходить в магазин в пятницу", height = 1),
                Task(title = "Написать контрольную минимум на 4", height = 3),
                Task(title = "Прибраться в комнате 5 дней подряд", height = 2)
            )
            val taskSmallItem = LayoutInflater.from(context).inflate(
                R.layout.small_task_item,
                this,
                false
            )
            for (i in 0 until 3) {
                this.addView(taskSmallItem, 0)
            }
        }
        holder.binding.allTasksButton.setOnClickListener {
            TODO()
        }
    }

    override fun getItemCount(): Int {
        return goalsList.size
    }

    inner class CardViewHolder(val binding: ZMockMainGoalCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}