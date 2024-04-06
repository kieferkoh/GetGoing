package com.example.getgoing

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    class SentViewHolder(itemView: View) : ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
        val timeNow = itemView.findViewById<TextView>(R.id.text_gchat_timestamp_me)

    }

    class ReceiveViewHolder(itemView: View) : ViewHolder(itemView) {
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_receive_message)
        val timeNow = itemView.findViewById<TextView>(R.id.text_gchat_timestamp_other)
        val name = itemView.findViewById<TextView>(R.id.text_gchat_user_other)
    }

    class EmptyViewHolder(itemView: View) : ViewHolder(itemView) {
        // No need to define any views or perform any operations in this ViewHolder
        // It simply acts as a placeholder for cases where no view needs to be displayed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            1 -> {
                // Inflate receive layout
                val view: View =
                    LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
                ReceiveViewHolder(view)
            }

            2 -> {
                // Inflate sent layout
                val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
                SentViewHolder(view)
            }

            4 -> {
                // Inflate sent layout
                // add new xml for this
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMessage = messageList[position]
        if (currentMessage.senderId == CurrentUserManager.currentUser?.phone) {
            (holder as? SentViewHolder)?.sentMessage?.text = currentMessage.message
            (holder as? SentViewHolder)?.timeNow?.text = currentMessage.time

        } else if (currentMessage.senderId == "Voting") {
            //if someone click on a message where sender id == voting i want to add code herre
            (holder as? ReceiveViewHolder)?.itemView?.setOnClickListener {
                //val intent = Intent(this, voting kt)
            }
        } else {
            (holder as? ReceiveViewHolder)?.receiveMessage?.text = currentMessage.message
            (holder as? ReceiveViewHolder)?.timeNow?.text = currentMessage.time
            CoroutineScope(Dispatchers.Main).launch {
                val name = getName(currentMessage.senderId!!)
                (holder as? ReceiveViewHolder)?.name?.text = name
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (CurrentUserManager.currentUser?.phone.equals(currentMessage.senderId)) {
            return ITEM_SENT
        } else if (currentMessage.senderId == "0") {
            return 3
        } else if (currentMessage.senderId == "Voting") {
            return 4
        } else {
            return ITEM_RECEIVE
        }
    }

    private suspend fun getName(phone: String): String? {
        return withContext(Dispatchers.Main) {
            CurrentUserManager.getName(phone)
        }
    }
}