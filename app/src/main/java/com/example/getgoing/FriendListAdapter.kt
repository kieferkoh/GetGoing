package com.example.getgoing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendListAdapter(
    private var FriendList: ArrayList<Friend>,
) : RecyclerView.Adapter<FriendListAdapter.FriendsViewHolder>() {

    private val checkedFriendsPositions = mutableListOf<Int>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.friend_list_item,
            parent, false
        )
        return FriendsViewHolder(itemView)
    }

    // method for filtering our recyclerview items.
    fun filterList(filterlist: ArrayList<Friend>) {
        FriendList = filterlist
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val friend = FriendList[position]
        holder.friendNameTV.text = friend.name
        holder.friendIV.setImageResource(friend.image)

        holder.checkBox.isChecked = checkedFriendsPositions.contains(position)
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkedFriendsPositions.add(position)
            } else {
                checkedFriendsPositions.remove(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return FriendList.size
    }

    fun getCheckedFriends(): List<Friend> {
        return checkedFriendsPositions.map { FriendList[it] }
    }

    inner class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val friendNameTV: TextView = itemView.findViewById(R.id.idTVCourse)
        val friendIV: ImageView = itemView.findViewById(R.id.idIVCourse)

    }
}
