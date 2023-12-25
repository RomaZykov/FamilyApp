package com.n1.moguchi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.MediumChildItemBinding
import com.n1.moguchi.databinding.SmallAddChildButtonBinding
import com.n1.moguchi.databinding.SmallChildItemBinding

private const val ADD_CHILD_BUTTON = 1

class ChildrenRecyclerAdapter(
    private val childrenList: List<Child>,
    private val selectedChildIndex: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(childrenList: List<Child>) : this(childrenList, selectedChildIndex = -1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SMALL_CHILD_CARD -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.small_child_item,
                    parent,
                    false
                )
                SmallChildViewHolder(view)
            }

            VIEW_TYPE_MEDIUM_CHILD_CARD -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.medium_child_item,
                    parent,
                    false
                )
                MediumChildViewHolder(view)
            }

            VIEW_TYPE_BUTTON -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.small_add_child_button,
                    parent,
                    false
                )
                SmallButtonViewHolder(view)
            }

            else -> {
                TODO()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_SMALL_CHILD_CARD -> {
                val child: Child = childrenList[position]
                (holder as SmallChildViewHolder).bind(child, position)
            }

            VIEW_TYPE_MEDIUM_CHILD_CARD -> {
                val child: Child = childrenList[position]
                (holder as MediumChildViewHolder).bind(child)
            }

            VIEW_TYPE_BUTTON -> (holder as SmallButtonViewHolder).bind()

            else -> throw RuntimeException("Unknown viewType: ${holder.itemViewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == childrenList.size) {
            VIEW_TYPE_BUTTON
        } else {
            if (selectedChildIndex != -1) {
                VIEW_TYPE_SMALL_CHILD_CARD
            } else {
                VIEW_TYPE_MEDIUM_CHILD_CARD
            }
        }
    }

    override fun getItemCount(): Int {
        return if (selectedChildIndex != -1) {
            childrenList.size + ADD_CHILD_BUTTON
        } else {
            childrenList.size
        }
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

        fun bind(child: Child) {
            binding.childName.text = child.childName
            binding.mediumChildAvatar.setImageResource(child.imageResourceId!!)
        }
    }

    inner class SmallButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SmallAddChildButtonBinding.bind(itemView)

        fun bind() {
        }
    }

    companion object {
        private const val VIEW_TYPE_MEDIUM_CHILD_CARD = 100
        private const val VIEW_TYPE_SMALL_CHILD_CARD = 101
        private const val VIEW_TYPE_BUTTON = 102
    }
}