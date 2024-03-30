package com.example.getgoing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    class SentViewHolder(itemView: View) : ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)

    }

    class ReceiveViewHolder(itemView: View) : ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)
    }
    class EmptyViewHolder(itemView: View) : ViewHolder(itemView) {
        // No need to define any views or perform any operations in this ViewHolder
        // It simply acts as a placeholder for cases where no view needs to be displayed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            1 -> {
                // Inflate receive layout
                val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
                ReceiveViewHolder(view)
            }
            2 -> {
                // Inflate sent layout
                val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
                SentViewHolder(view)
            }
            else -> {
                // Create an empty view
                val emptyView = View(context)
                EmptyViewHolder(emptyView)
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (currentMessage.senderId == CurrentUserManager.currentUser?.phone) {
            (holder as? SentViewHolder)?.sentMessage?.text = currentMessage.message
        } else {
            (holder as? ReceiveViewHolder)?.receiveMessage?.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (CurrentUserManager.currentUser?.phone.equals(currentMessage.senderId)) {
            return ITEM_SENT
        } else if (currentMessage.senderId == "0") {
            return 3
        } else {
            return ITEM_RECEIVE
        }
    }
}