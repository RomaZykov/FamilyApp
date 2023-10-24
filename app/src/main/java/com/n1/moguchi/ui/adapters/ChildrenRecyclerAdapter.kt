package com.n1.moguchi.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.ChildCreationCardBinding
import com.n1.moguchi.databinding.CreationChildSectionFooterBinding
import com.n1.moguchi.databinding.MediumChildItemBinding

class ChildrenRecyclerAdapter(private val childrenCards: MutableList<Child>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val childrenNames = mutableListOf<String>()
    private var cardCompletedMap: MutableMap<Int, Boolean> = mutableMapOf()
    var onNewChildAddClicked: (() -> Unit)? = null

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
        return if (position == childrenCards.size) {
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
            (holder as ChildViewHolder).bind(position)
        } else {
            (holder as BottomViewHolder).bind()
        }
//        else {
//            (holder as MediumChildViewHolder).bind(child as String)
//        }
    }

    override fun getItemCount(): Int {
        return childrenCards.size + 1
    }

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ChildCreationCardBinding.bind(itemView)

        fun bind(position: Int) {
            binding.childNameEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    if (binding.childNameEditText.text?.isNotEmpty() == true) {
                        val childName = binding.childNameEditText.text.toString().replace(" ", "")
                        childrenNames.add(childName)
                        cardCompletedMap[position] = true
                        notifyItemRangeChanged(itemCount - 1, itemCount)
                    }
                }
                false
            }

            if (childrenCards.size != 1) {
                binding.deleteChildButton.setOnClickListener {
                    childrenCards.removeAt(position)
//                    childrenNames.removeIf { it == childName }
                    notifyItemRemoved(position)
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
        }
    }

    inner class BottomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CreationChildSectionFooterBinding.bind(itemView)
        var context: Context = itemView.context

        fun bind() {
            if (cardCompletedMap.values.all { true }) {
                binding.addChildButton.backgroundTintList = context.getColorStateList(R.color.white)
                itemView.setOnClickListener {
                    onNewChildAddClicked?.invoke()
                }
            } else {
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