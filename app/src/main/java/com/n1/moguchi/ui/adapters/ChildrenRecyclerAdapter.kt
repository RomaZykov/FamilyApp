package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.MediumChildItemBinding
import com.n1.moguchi.databinding.SmallChildItemBinding

class ChildrenRecyclerAdapter(private val childrenList: List<Child>, private val selectedChildIndex: Int) :
    RecyclerView.Adapter<ChildrenRecyclerAdapter.SmallChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallChildViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.small_child_item, parent, false)
        return SmallChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: SmallChildViewHolder, position: Int) {
        val child: Child = childrenList[position]
        holder.bind(child, position)

//        else -> {
//            val view = LayoutInflater.from(parent.context).inflate(
//                R.layout.medium_child_item,
//                parent,
//                false
//            )
//            MediumChildViewHolder(view)
//        }
    }

    override fun getItemCount(): Int {
        return childrenList.size
    }

    inner class SmallChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SmallChildItemBinding.bind(itemView)

        fun bind(child: Child, position: Int) {
            binding.smallChildName.text = child.childName
            binding.smallChildAvatar.setImageResource(child.imageResourceId!!)
            binding.root.isSelected = selectedChildIndex == position
        }
    }

    inner class MediumChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = MediumChildItemBinding.bind(itemView)

        fun bind(childName: String) {
            binding.childName.text = childName
            if (childName == "Максимка") {
                binding.mediumChildAvatar.setImageResource(R.drawable.avatar_male_2)
            } else {
                binding.mediumChildAvatar.setImageResource(R.drawable.avatar_female_3)
            }
//            binding.root.setOnClickListener {
//                childClickListener?.onChildItemClick(itemView)
//            }
        }
    }
}