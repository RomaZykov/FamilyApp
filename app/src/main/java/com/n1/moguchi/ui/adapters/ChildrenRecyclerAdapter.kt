package com.n1.moguchi.ui.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.ChildCreationCardBinding
import com.n1.moguchi.databinding.CreationChildSectionFooterBinding
import com.n1.moguchi.databinding.MediumChildItemBinding

class ChildrenRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var children: MutableList<Child> = ArrayList()
        set(value) {
            field = value
            if (field.size == 1) {
                notifyItemChanged(0)
            }
        }
    var onNewChildAddClicked: (() -> Unit)? = null
    var onChildUpdate: ((Child) -> Unit)? = null
    var onChildRemoveClicked: ((Child) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CHILD_CARD -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.child_creation_card,
                    parent,
                    false
                )
                ChildViewHolder(view)
            }

            VIEW_TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.creation_child_section_footer,
                    parent,
                    false
                )
                BottomViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.medium_child_item,
                    parent,
                    false
                )
                MediumChildViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == children.size) {
            VIEW_TYPE_FOOTER
        } else {
            VIEW_TYPE_CHILD_CARD
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder.itemViewType) {
            VIEW_TYPE_CHILD_CARD -> (holder as ChildViewHolder).bind()
            VIEW_TYPE_FOOTER -> (holder as BottomViewHolder).bind()
            else -> throw RuntimeException("Unknown viewType: ${holder.itemViewType}")
        }
//        else {
//            (holder as MediumChildViewHolder).bind(child as String)
//        }
    }

    override fun getItemCount(): Int {
        return children.size + 1
    }

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ChildCreationCardBinding.bind(itemView)

        fun bind() {
            binding.childNameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(childName: Editable?) {
                    children[adapterPosition].childName = childName.toString()
                    if (childName.toString().isEmpty()) {
                        binding.childNameEditText.error = "Выполните все условия"
                    } else {
                        onChildUpdate?.invoke(children[adapterPosition])
                    }
                    notifyItemChanged(itemCount - 1)
                }
            })

            if (children.size > 1) {
                binding.deleteChildButton.visibility = View.VISIBLE
                binding.deleteChildButton.setOnClickListener {
                    onChildRemoveClicked?.invoke(children[adapterPosition])
                    children.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    notifyItemChanged(itemCount - 1)
                }
            } else {
                binding.deleteChildButton.visibility = View.GONE
            }

            binding.avatars.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    binding.avatarMale1.id,
                    binding.avatarMale2.id,
                    binding.avatarFemale2.id,
                    binding.avatarFemale3.id -> {
                        children[adapterPosition].imageResourceId = checkedId
                        onChildUpdate?.invoke(children[adapterPosition])
                        notifyItemChanged(itemCount - 1)
                    }
                }
            }
        }
    }

    inner class BottomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CreationChildSectionFooterBinding.bind(itemView)
        var context: Context = itemView.context

        fun bind() {
            if (children.all {
                    it.childName.toString().isNotEmpty() && it.imageResourceId != null
                } && children.size == itemCount - 1) {
                with(binding.addChildButton) {
                    isEnabled = true
                    setTextColor(context.getColorStateList(R.color.black))
                    iconTint = context.getColorStateList(R.color.black)
                    backgroundTintList = context.getColorStateList(R.color.white)
                }
                itemView.setOnClickListener {
                    onNewChildAddClicked?.invoke()
                }
            } else {
                with(binding.addChildButton) {
                    isEnabled = false
                    backgroundTintList = context.getColorStateList(R.color.white_opacity_70)
                    setTextColor(context.getColorStateList(R.color.black_opacity_70))
                    iconTint = context.getColorStateList(R.color.black_opacity_70)
                }
            }
        }
    }

    // Need to remove
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

    companion object {
        const val MAX_POOL_SIZE = 0
        const val VIEW_TYPE_CHILD_CARD = 100
        const val VIEW_TYPE_FOOTER = 101
    }
}