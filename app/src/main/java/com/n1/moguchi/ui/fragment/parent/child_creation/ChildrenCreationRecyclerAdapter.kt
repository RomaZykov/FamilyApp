package com.n1.moguchi.ui.fragment.parent.child_creation

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.models.Child
import com.n1.moguchi.databinding.ChildCreationCardBinding
import com.n1.moguchi.databinding.CreationSectionFooterBinding

private const val FOOTER_ADD_CHILD_BUTTON = 1

class ChildrenCreationRecyclerAdapter(
    private val editChildOptionEnable: Boolean,
    private val isFromParentProfile: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(editChildOptionEnable: Boolean) : this(
        editChildOptionEnable, isFromParentProfile = false
    )

    var children: MutableList<Child> = ArrayList()
    var onNewChildAddClicked: (() -> Unit)? = null
    var onChildUpdate: ((Child) -> Unit)? = null
    var onChildRemoveClicked: ((Child, Int) -> Unit)? = null
    var onChildRemoveForBottomSheetClicked: ((Child, Int) -> Unit)? = null
    var onCardsStatusUpdate: ((Boolean) -> Unit)? = null

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
                    R.layout.creation_section_footer,
                    parent,
                    false
                )
                FooterViewHolder(view)
            }

            else -> {
                TODO()
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
            VIEW_TYPE_CHILD_CARD -> {
                val child = children[position]
                (holder as ChildViewHolder).bind(child)
            }

            VIEW_TYPE_FOOTER -> (holder as FooterViewHolder).bind()
            else -> throw RuntimeException("Unknown viewType: ${holder.itemViewType}")
        }
    }

    override fun getItemCount(): Int {
        return children.size + FOOTER_ADD_CHILD_BUTTON
    }

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ChildCreationCardBinding.bind(itemView)
        private val context = itemView.context
        private val childAvatars: Map<Int, Int> = mapOf(
            binding.avatarMale1.id to R.drawable.avatar_male_1,
            binding.avatarMale2.id to R.drawable.avatar_male_2,
            binding.avatarFemale2.id to R.drawable.avatar_female_2,
            binding.avatarFemale3.id to R.drawable.avatar_female_3
        )

        fun bind(child: Child) {
            if (child.childName != null && child.imageResourceId != null) {
                binding.childNameEditText.setText(child.childName)
                val checkedId = childAvatars.entries.find { it.value == child.imageResourceId }?.key
                if (checkedId != null) {
                    binding.avatars.check(checkedId)
                }
            }
            binding.childNameEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(childName: Editable?) {
                    children[adapterPosition].childName = childName.toString()
                    val regex = "^[a-zA-Zа-яА-Я]+$".toRegex()
                    if (childName.toString().isNotBlank() && childName.toString().matches(regex)) {
                        onChildUpdate?.invoke(child)
                    } else {
                        binding.childNameEditText.error =
                            getString(context, R.string.child_name_edit_text_error)
                    }
                    notifyItemChanged(itemCount - FOOTER_ADD_CHILD_BUTTON)
                }
            })

            if (children.size > 1 && editChildOptionEnable) {
                binding.deleteChildButton.visibility = View.VISIBLE
                binding.deleteChildButton.setOnClickListener {
                    if (isFromParentProfile) {
                        onChildRemoveForBottomSheetClicked?.invoke(child, adapterPosition)
                        children.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        notifyItemChanged(itemCount - FOOTER_ADD_CHILD_BUTTON)
                    } else {
                        onChildRemoveClicked?.invoke(child, adapterPosition)
                        children.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        notifyItemChanged(itemCount - FOOTER_ADD_CHILD_BUTTON)
                    }
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
                        children[adapterPosition].imageResourceId = childAvatars[checkedId]
                        onChildUpdate?.invoke(children[adapterPosition])
                        notifyItemChanged(itemCount - FOOTER_ADD_CHILD_BUTTON)
                    }
                }
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CreationSectionFooterBinding.bind(itemView)
        var context: Context = itemView.context

        fun bind() {
            val regex = "^[a-zA-Zа-яА-Я]+$".toRegex()
            if (children.all {
                    it.childName != null
                            && it.imageResourceId != null
                            && it.childName.toString().matches(regex)
                } && children.size == itemCount - FOOTER_ADD_CHILD_BUTTON) {
                onCardsStatusUpdate?.invoke(true)
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
                onCardsStatusUpdate?.invoke(false)
                with(binding.addChildButton) {
                    isEnabled = false
                    backgroundTintList = context.getColorStateList(R.color.white_opacity_70)
                    setTextColor(context.getColorStateList(R.color.black_opacity_70))
                    iconTint = context.getColorStateList(R.color.black_opacity_70)
                }
            }
        }
    }

    companion object {
        const val MAX_POOL_SIZE = 0
        const val VIEW_TYPE_CHILD_CARD = 100
        const val VIEW_TYPE_FOOTER = 101
    }
}