package com.n1.moguchi.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.databinding.ChildCreationCardBinding

class ChildrenListAdapter(private val childrenCard: MutableList<View>) :
    RecyclerView.Adapter<ChildrenListAdapter.ChildViewHolder>() {

    private var childrenNames: MutableList<String> = mutableListOf()

    class ChildViewHolder(val binding: ChildCreationCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        interface Listener {
            fun getChildrenNamesList(list: ArrayList<String>);
        }
    }

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
        childrenNames.add(
            holder.binding.childNameEditText.editText?.text.toString().trim { it <= ' ' })
    }

    override fun getItemCount(): Int {
        return childrenCard.size
    }

    fun getItem(position: Int): String {
        return childrenNames[position]
    }
}