package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.n1.moguchi.R
import com.n1.moguchi.databinding.ChildCreationCardBinding

class ChildrenRecyclerAdapter(private val childrenCard: MutableList<View>) :
    RecyclerView.Adapter<ChildrenRecyclerAdapter.ChildViewHolder>() {

    private var childrenNames: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.child_creation_card,
            parent,
            false
        )
        return ChildViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val childCard = childrenCard[position]
        holder.bind(childCard, position)
    }

    override fun getItemCount(): Int {
        return childrenCard.size
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
            if (childName.isEmpty()) {
                TODO()
            }
            childrenNames.add(childName)

            binding.deleteChildButton.setOnClickListener {
                childrenCard.removeAt(position)
                childrenNames.removeIf { it == childName }
                notifyItemRemoved(position)
            }
        }
    }
}
