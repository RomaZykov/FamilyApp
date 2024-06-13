package com.n1.moguchi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.n1.moguchi.R
import com.n1.moguchi.databinding.NotificationItemBinding

class NotificationsRecyclerAdapter(private val notificationList: MutableList<String>) :
    RecyclerView.Adapter<NotificationsRecyclerAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val notification = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(notification)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.bind(notification, position)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    inner class NotificationViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val binding = NotificationItemBinding.bind(itemView)
//        private var sendNotification: SendNotification? = null

        fun bind(mockMessage: String, position: Int) {
//            if (position == 0) {
//                TODO()
//            }
            binding.notificationTitle.text = mockMessage
        }
    }
}
