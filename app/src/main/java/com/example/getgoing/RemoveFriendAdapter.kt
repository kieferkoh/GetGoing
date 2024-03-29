package com.example.getgoing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope

public interface FriendItemClickListener {
    fun onAcceptButtonClick(position: Int)
    fun onDeclineButtonClick(position: Int)
}

class RemoveFriendAdapter(private var FriendList: ArrayList<Friend>,
                          private val itemClickListener: FriendItemClickListener)
                          : RecyclerView.Adapter<RemoveFriendAdapter.FriendViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemoveFriendAdapter.FriendViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.friend_request_items, parent, false)
        return FriendViewHolder(itemView)
    }

    fun filterList(filterlist: ArrayList<Friend>) {
        FriendList = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RemoveFriendAdapter.FriendViewHolder, position: Int) {
        holder.friendNameTV.text = FriendList.get(position).name
        holder.friendImgIV.setImageResource(FriendList.get(position).image)
    }

    override fun getItemCount(): Int {
        return FriendList.size
    }

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val acceptButton: Button = itemView.findViewById(R.id.buttonAccept)
        val declineButton: Button = itemView.findViewById(R.id.buttonDecline)
        var friendNameTV: TextView = itemView.findViewById(R.id.idTVCourse)
        var friendImgIV: ImageView = itemView.findViewById(R.id.idIVCourse)

        init {
            acceptButton.setOnClickListener{
                itemClickListener.onAcceptButtonClick(adapterPosition)
            }

            declineButton.setOnClickListener{
                itemClickListener.onDeclineButtonClick(adapterPosition)
            }
        }

        fun bind(friend: Friend){
            friendNameTV.text = friend.name
            friendImgIV.setImageResource(friend.image)
        }

    }
}
