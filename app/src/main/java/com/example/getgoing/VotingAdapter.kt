package com.example.getgoing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VotingAdapter(
    private var votingList: ArrayList<VotingItem>,
    private var GID: String,
    private var totalMembers: Int,
    private var votingPage: VotingPage
) : RecyclerView.Adapter<VotingAdapter.VotingViewHolder>() {
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.voting_item, parent, false)
        return VotingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VotingViewHolder, position: Int) {
        val votingItem = votingList[position]
        holder.bind(votingItem)
    }

    override fun getItemCount(): Int {
        return votingList.size
    }

    inner class VotingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val locationNameTextView: TextView = itemView.findViewById(R.id.locationText)
        private val voteCountTextView: TextView = itemView.findViewById(R.id.votes)
        private val voteButton: Button = itemView.findViewById(R.id.voteButton)

        fun bind(item: VotingItem) {
            if (item.address == "Singapore"){
                locationNameTextView.text = item.name
            }
            else {
                locationNameTextView.text = item.address
            }
            if (!item.userList?.isEmpty()!!) {
                if (item.userList?.get(0) == "") {
                    item.userList!!.remove("")
                }
            }
            voteCountTextView.text = item.userList?.size.toString()


            val hasVoted = (item.userList?.contains(CurrentUserManager.currentUser?.phone.toString()) == true)
            voteButton.text = if (hasVoted) "Unvote" else "Vote"

            voteButton.setOnClickListener {
                if (hasVoted) {
                    // Unvote
                    item.userList?.remove(CurrentUserManager.currentUser?.phone!!)


                } else {
                    // Vote
                    item.userList?.add(CurrentUserManager.currentUser?.phone!!)
                }
                if (item.userList?.isEmpty() == true){
                    item.userList?.add("")
                    voteCountTextView.text = "0"
                }
                else {
                    voteCountTextView.text = item.userList?.size.toString()
                }
                notifyDataSetChanged()
                CoroutineScope(Dispatchers.Main).launch {
                    DatabaseManager.createDataFirebase(
                        item.userList,
                        mDbRef.child("Vote").child(GID).child(item.address!!).child("userList")
                    )
                }


            }
            if (item.userList?.size == totalMembers){
                votingPage.closeVoting(item)
            }
        }
    }
}
