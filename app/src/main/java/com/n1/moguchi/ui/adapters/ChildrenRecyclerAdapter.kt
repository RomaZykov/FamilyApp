package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.n1.moguchi.R
import com.n1.moguchi.databinding.ChildCreationCardBinding
import com.n1.moguchi.databinding.MediumChildItemBinding
import com.n1.moguchi.ui.activity.MainActivity

class ChildrenRecyclerAdapter(
    private val children: MutableList<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var childrenNames: MutableList<String> = mutableListOf()

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
        return if (children[position] is View) {
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val child = children[position]
        if (holder.itemViewType == 0) {
            (holder as ChildViewHolder).bind(child as View, position)
        } else {
            (holder as MediumChildViewHolder).bind(child as String)
        }
    }

    override fun getItemCount(): Int {
        return children.size
    }

    fun retrieveChildrenNames(): List<String> {
        return childrenNames
    }

    inner class ChildViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = ChildCreationCardBinding.bind(itemView)

        fun bind(view: View, position: Int) {
            val childName =
                view.findViewById<TextInputEditText>(R.id.child_name_edit_text).text.toString()
                    .trim { it <= ' ' }
            if (childName.isNotEmpty()) {
                childrenNames.add(childName)
            } else {
                binding.childNameEditText.error = "Добавьте имя ребёнка"
            }

            if (children.size > 1) {
                binding.deleteChildButton.setOnClickListener {
                    children.removeAt(position)
                    childrenNames.removeIf { it == childName }
                    notifyItemRemoved(position)
                }
            } else {
                binding.deleteChildButton.visibility = View.GONE
            }

            binding.avatars.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View) {
                    when (view) {
                        binding.avatarMale1 -> {
                            TODO()
                        }

                        binding.avatarMale2 -> {
                        }

                        binding.avatarFemale2 -> {
                        }

                        binding.avatarFemale3 -> {
                        }
                    }
                }
            })
        }
    }

    inner class MediumChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val binding = MediumChildItemBinding.bind(itemView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(childName: String) {
            binding.childName.text = childName
            if (childName == "Максимка") {
                binding.mediumChildAvatar.setImageResource(R.drawable.avatar_male_2)
            } else {
                binding.mediumChildAvatar.setImageResource(R.drawable.avatar_female_3)
            }
        }

        override fun onClick(v: View) {
            val mainActivityContext = v.context as MainActivity
            mainActivityContext.navController.navigate(R.id.action_switchToChildFragment_to_onBoardingChildFragment)
        }
    }
}