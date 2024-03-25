package com.example.getgoing
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class GroupFriendsAdapter(private val friendsList : ArrayList<Friend>) :
    RecyclerView.Adapter<GroupFriendsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ShapeableImageView = itemView.findViewById(R.id.friend_image)
        val name : TextView = itemView.findViewById(R.id.friend_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.group_friend_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = friendsList[position]
        holder.image.setImageResource(currentItem.image)
        holder.name.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return friendsList.size
    }
}
