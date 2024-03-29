package com.example.getgoing

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler

class GroupDisplayAdapter(
    var GroupList: ArrayList<Group>,
) : RecyclerView.Adapter<GroupDisplayAdapter.GroupChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.groups_list,
            parent, false)
        return GroupChatViewHolder(itemView)
    }
    // add filter for future

    override fun onBindViewHolder(holder: GroupChatViewHolder, position: Int) {
        val context = holder.itemView.context
        val currentGroup = GroupList[position]

        holder.groupNameTV.text = GroupList[position].name
        GroupList[position].image?.let { holder.groupIV.setImageResource(it) }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            GroupManager.currentGroup = currentGroup
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return GroupList.size
    }


    class GroupChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupNameTV: TextView = itemView.findViewById(R.id.GroupDisplay_name)
        val groupIV: ImageView = itemView.findViewById(R.id.GroupDisplay_image)
    }

}