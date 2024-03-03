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
    private var selectedChildIndex: Int,
    private val addChildButtonEnable: Boolean,
    private val childSelectionEnable: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onChildClicked: ((Int, String?) -> Unit)? = null
    var onChildAddClicked: (() -> Unit)? = null

    constructor(childrenList: List<Child>) : this(
        childrenList,
        selectedChildIndex = -1,
        addChildButtonEnable = false,
        childSelectionEnable = false
    )

    constructor(
        childrenList: List<Child>,
        selectedChildIndex: Int
    ) : this(
        childrenList,
        selectedChildIndex,
        addChildButtonEnable = false,
        childSelectionEnable = true
    )

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
                AddChildButtonViewHolder(view)
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
                (holder as SmallChildViewHolder).bind(child)
            }

            VIEW_TYPE_MEDIUM_CHILD_CARD -> {
                val child: Child = childrenList[position]
                (holder as MediumChildViewHolder).bind(child)
            }

            VIEW_TYPE_BUTTON -> (holder as AddChildButtonViewHolder).bind()

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
        return if ((selectedChildIndex != -1 && childSelectionEnable) xor addChildButtonEnable) {
            childrenList.size + ADD_CHILD_BUTTON
        } else {
            childrenList.size
        }
    }

    inner class MediumChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val binding = MediumChildItemBinding.bind(itemView)
        private var child: Child? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(child: Child) {
            this.child = child
            binding.childName.text = child.childName
            binding.mediumChildAvatar.setImageResource(child.imageResourceId!!)
        }

        override fun onClick(v: View) {
            onChildClicked?.invoke(adapterPosition, child?.childId)
        }
    }

    inner class SmallChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val binding = SmallChildItemBinding.bind(itemView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(child: Child) {
            binding.smallChildName.text = child.childName
            binding.smallChildAvatar.setImageResource(child.imageResourceId!!)
            binding.root.isSelected = selectedChildIndex == adapterPosition
            if (binding.root.isSelected || !childSelectionEnable) {
                changeClickable(false)
            } else {
                changeClickable(true)
            }
        }

        private fun changeClickable(enable: Boolean) {
            binding.root.isClickable = enable
            binding.root.isFocusable = enable
        }

        override fun onClick(v: View?) {
            val oldPosition = selectedChildIndex
            val newPosition = adapterPosition
            selectedChildIndex = newPosition
            onChildClicked?.invoke(selectedChildIndex, null)
            notifyItemChanged(oldPosition)
            notifyItemChanged(newPosition)
        }
    }

    inner class AddChildButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SmallAddChildButtonBinding.bind(itemView)

        fun bind() {
            binding.root.setOnClickListener {
                onChildAddClicked?.invoke()
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_MEDIUM_CHILD_CARD = 100
        private const val VIEW_TYPE_SMALL_CHILD_CARD = 101
        private const val VIEW_TYPE_BUTTON = 102
    }
}