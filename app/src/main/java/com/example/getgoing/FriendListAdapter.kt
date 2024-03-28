package com.example.getgoing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendListAdapter(
    private var FriendList: ArrayList<Friend>,
) : RecyclerView.Adapter<FriendListAdapter.FriendsViewHolder>() {
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
        holder.friendNameTV.text = FriendList[position].name
        holder.friendIV.setImageResource(FriendList[position].image)
    }

    override fun getItemCount(): Int {
        return FriendList.size
    }

    class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendNameTV: TextView = itemView.findViewById(R.id.idTVCourse)
        val friendIV: ImageView = itemView.findViewById(R.id.idIVCourse)
    }
}
