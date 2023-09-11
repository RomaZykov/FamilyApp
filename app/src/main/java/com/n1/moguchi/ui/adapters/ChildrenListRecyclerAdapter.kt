package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.databinding.ChildCreationCardBinding

class ChildrenListRecyclerAdapter(private val childrenCard: MutableList<View>) :
    RecyclerView.Adapter<ChildrenListRecyclerAdapter.ChildViewHolder>() {

    private var childrenNames: MutableList<String> = mutableListOf()

    inner class ChildViewHolder(val binding: ChildCreationCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val childCard = ChildCreationCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChildViewHolder(childCard)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.binding.deleteChildButton.setOnClickListener {
            childrenCard.removeAt(position)
            this.notifyItemRemoved(position)
        }
        val childName = holder.binding.childNameEditText.text.toString().trim { it <= ' ' }
        childrenNames.add(childName)
    }


    override fun getItemCount(): Int {
        return childrenCard.size
    }

    fun retrieveChildrenNames(): List<String> {
        return childrenNames
    }
}