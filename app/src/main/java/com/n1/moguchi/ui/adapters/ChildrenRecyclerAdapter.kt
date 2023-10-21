package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.ChildCreationCardBinding
import com.n1.moguchi.databinding.MediumChildItemBinding

class ChildrenRecyclerAdapter(val childrenCards: MutableList<Child>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var childName: String = ""
    var onNewChildAddClicked: (() -> Unit)? = null

    fun retrieveData(): String {
        return childName
    }

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
        return 0
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (holder.itemViewType == 0) {
            (holder as ChildViewHolder).bind(position)
        }
//        else {
//            (holder as MediumChildViewHolder).bind(child as String)
//        }
    }

    override fun getItemCount(): Int {
        return childrenCards.size
    }

    inner class ChildViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = ChildCreationCardBinding.bind(itemView)

        var deleteCardEvent: (() -> Unit)? = null

        fun bind(position: Int) {
            val childName = binding.childNameEditText.text.toString().trim { it <= ' ' }
            if (childName.isNotEmpty()) {
                retrieveData()
            } else {
                binding.childNameEditText.error = "Добавьте имя ребёнка"
            }

            if (position > 0) {
                binding.deleteChildButton.setOnClickListener {
                    deleteCardEvent?.invoke()
//                    childrenNames.removeIf { it == childName }
                    notifyItemRemoved(position)
                }
            } else {
                binding.deleteChildButton.visibility = View.GONE
            }

            binding.avatars.setOnClickListener(object : View.OnClickListener {
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
