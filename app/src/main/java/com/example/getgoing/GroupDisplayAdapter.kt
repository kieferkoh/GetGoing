package com.example.getgoing

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
        holder.groupNameTV.text = GroupList[position].name
        GroupList[position].image?.let { holder.groupIV.setImageResource(it) }
    }

    override fun getItemCount(): Int {
        return GroupList.size
    }


    class GroupChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupNameTV: TextView = itemView.findViewById(R.id.GroupDisplay_name)
        val groupIV: ImageView = itemView.findViewById(R.id.GroupDisplay_image)
    }

}