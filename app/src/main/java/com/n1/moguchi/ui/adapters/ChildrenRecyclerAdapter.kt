package com.n1.moguchi.ui.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
            notifyDataSetChanged()
        }
    var onNewChildAddClicked: (() -> Unit)? = null
    var onChildRemoveClicked: ((Child) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.child_creation_card,
                    parent,
                    false
                )
                ChildViewHolder(view)
            }

            1 -> {
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
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder.itemViewType == 0) {
            (holder as ChildViewHolder).bind()
        } else {
            (holder as BottomViewHolder).bind()
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
                    }
                    Log.d("ChildrenRecycler", "ChildrenNames = $children, size = ${children.size}")
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

            binding.avatars.setOnClickListener(
                object : View.OnClickListener {
                    override fun onClick(view: View) {
                        when (view) {
                            binding.avatarMale1,
                            binding.avatarMale2,
                            binding.avatarFemale2,
                            binding.avatarFemale3 -> {

                            }
                        }
                    }
                })
            Log.d("ChildrenRecycler", "ChildrenNames = $children, size = ${children.size}")
        }
    }

    inner class BottomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CreationChildSectionFooterBinding.bind(itemView)
        var context: Context = itemView.context

        fun bind() {
            if (children.all { it.childName.isNotEmpty() } && children.size == itemCount - 1) {
                binding.addChildButton.isEnabled = true
                binding.addChildButton.backgroundTintList = context.getColorStateList(R.color.white)
                itemView.setOnClickListener {
                    onNewChildAddClicked?.invoke()
                }
            } else {
                binding.addChildButton.isEnabled = false
                binding.addChildButton.backgroundTintList =
                    context.getColorStateList(R.color.white_opacity_70)
            }
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