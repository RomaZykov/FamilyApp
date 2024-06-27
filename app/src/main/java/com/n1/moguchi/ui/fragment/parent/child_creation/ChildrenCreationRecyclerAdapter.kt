package com.n1.moguchi.ui.fragment.parent.child_creation

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.databinding.ChildCreationCardBinding
import com.n1.moguchi.databinding.CreationSectionFooterBinding

class ChildrenCreationRecyclerAdapter(
    private val editChildOptionEnable: Boolean,
    private val addChildButtonEnable: Boolean,
    private val removeChildFastProcessEnable: Boolean,
    private val passwordEnable: Boolean,
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    constructor(editChildOptionEnable: Boolean) : this(
        editChildOptionEnable,
        addChildButtonEnable = false,
        removeChildFastProcessEnable = false,
        passwordEnable = false
    )

    var children: MutableList<Child> = mutableListOf()
    var onNewChildAddClicked: () -> Unit = {}
    var onChildRemoveClicked: (Child, Int) -> Unit = { _, _ -> }
    var onChildRemoveViaBottomSheetClicked: (Child, Int) -> Unit = { _, _ -> }
    var onCardsStatusUpdate: (Boolean) -> Unit = {}
    var onUpdateChildrenCards: (List<Child>, Boolean) -> Unit = { _, _ -> }

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
                throw IllegalArgumentException("Invalid view type $viewType")
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
        return if (addChildButtonEnable) {
            children.size + FOOTER_ADD_CHILD_BUTTON
        } else {
            children.size
        }
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
            if (child.childName.isNotEmpty() && child.imageResourceId != null) {
                binding.childNameEditText.setText(child.childName)
                val checkedId = childAvatars.entries.find { it.value == child.imageResourceId }?.key
                if (checkedId != null) {
                    binding.avatars.check(checkedId)
                }
            } else {
                val updatedChild =
                    children[absoluteAdapterPosition].copy(imageResourceId = childAvatars[binding.avatarMale1.id])
                binding.avatarMale1.isChecked = true
                children[absoluteAdapterPosition] = updatedChild
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
                    val updatedChild =
                        children[absoluteAdapterPosition].copy(childName = childName.toString())
                    children[absoluteAdapterPosition] = updatedChild
                    onUpdateChildrenCards.invoke(
                        children,
                        if (updatedChild.childName.isEmpty()) false else true
                    )
                    if (childName.toString().isBlank() && !childName.toString()
                            .matches(regex())
                    ) {
                        binding.childNameEditText.error =
                            getString(context, R.string.child_name_edit_text_error)
                    }
                    if (addChildButtonEnable) {
                        notifyItemChanged(itemCount - FOOTER_ADD_CHILD_BUTTON)
                    }
                }
            })

            if (children.size > 1 && editChildOptionEnable) {
                binding.deleteChildButton.visibility = View.VISIBLE
                binding.deleteChildButton.setOnClickListener {
                    if (removeChildFastProcessEnable) {
                        onChildRemoveClicked.invoke(child, absoluteAdapterPosition)
                        children.removeAt(absoluteAdapterPosition)
                        notifyItemRemoved(absoluteAdapterPosition)
                        notifyItemChanged(itemCount - FOOTER_ADD_CHILD_BUTTON)
                    } else {
                        onChildRemoveViaBottomSheetClicked.invoke(child, absoluteAdapterPosition)
                        if (children.size == 1) {
                            notifyItemChanged(0)
                        }
                    }
                }
            } else {
                binding.deleteChildButton.visibility = View.GONE
            }

            if (passwordEnable) {
                binding.passwordTitle.visibility = View.VISIBLE
                binding.setPasswordInputLayout.visibility = View.VISIBLE
                binding.setPasswordEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(password: Editable?) {
                        binding.setPasswordInputLayout.isEndIconVisible =
                            password.toString().isNotBlank()
                        val updatedChild = children[absoluteAdapterPosition].copy(
                            passwordFromParent = if (password.isNullOrBlank()) -1
                            else password.toString().toInt()
                        )
                        children[absoluteAdapterPosition] = updatedChild
                        if (password.toString().isBlank()) {
                            binding.setPasswordEditText.error =
                                getString(context, R.string.password_edit_text_error)
                        }
                        if (addChildButtonEnable) {
                            notifyItemChanged(itemCount - FOOTER_ADD_CHILD_BUTTON)
                        }
                    }
                })
            } else {
                binding.setPasswordEditText.visibility = View.GONE
                binding.passwordTitle.visibility = View.GONE
            }

            binding.avatars.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    binding.avatarMale1.id,
                    binding.avatarMale2.id,
                    binding.avatarFemale2.id,
                    binding.avatarFemale3.id -> {
                        val updatedChild =
                            children[absoluteAdapterPosition].copy(imageResourceId = childAvatars[checkedId])
                        children[absoluteAdapterPosition] = updatedChild
                        onUpdateChildrenCards.invoke(
                            children,
                            if (updatedChild.childName.isEmpty()) false else true
                        )
                        if (addChildButtonEnable) {
                            notifyItemChanged(itemCount - FOOTER_ADD_CHILD_BUTTON)
                        }
                    }
                }
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CreationSectionFooterBinding.bind(itemView)

        fun bind() {
            val allChildCardCreationWithPasswordIsValid = children.all {
                (it.passwordFromParent != null && it.passwordFromParent != -1)
                        && it.passwordFromParent.toString().isNotEmpty()
                        && it.childName.isNotEmpty()
                        && it.childName.matches(regex())
            }
            val allChildCardCreationIsValid = children.all {
                it.childName.isNotEmpty() && it.childName.matches(regex())
            }
            if ((passwordEnable && allChildCardCreationWithPasswordIsValid)
                || (!passwordEnable && allChildCardCreationIsValid
                        && children.size == itemCount - FOOTER_ADD_CHILD_BUTTON)
            ) {
                changeCardStatusUI(true)
                itemView.setOnClickListener {
                    onNewChildAddClicked.invoke()
                }
                return
            }
            changeCardStatusUI(false)
        }

        private fun changeCardStatusUI(buttonEnable: Boolean) {
            onCardsStatusUpdate.invoke(buttonEnable)
            with(binding.addChildButton) {
                isEnabled = buttonEnable
                setTextColor(context.getColorStateList(if (buttonEnable) R.color.black else R.color.black_opacity_70))
                iconTint =
                    context.getColorStateList(if (buttonEnable) R.color.black else R.color.black_opacity_70)
                backgroundTintList =
                    context.getColorStateList(if (buttonEnable) R.color.white else R.color.white_opacity_70)
            }
        }
    }

    private fun regex() = "^[a-zA-Zа-яА-Я]+$".toRegex()

    companion object {
        const val MAX_POOL_SIZE = 0
        const val VIEW_TYPE_CHILD_CARD = 100
        const val VIEW_TYPE_FOOTER = 101

        const val FOOTER_ADD_CHILD_BUTTON = 1
    }
}
