package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.renderscript.Sampler.Value
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VotingPage : AppCompatActivity() {


    lateinit var VotingView: RecyclerView
    lateinit var VotingAdapter: VotingAdapter
    lateinit var VotingList: ArrayList<VotingItem>
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDbRef = FirebaseDatabase.getInstance().getReference()
//      FRIEND LIST PAGE
        setContentView(R.layout.activity_voting_page)

        supportActionBar?.hide()

        val gid: String? = intent.getStringExtra("GID")
        val backBtn = findViewById<ImageButton>(R.id.backToChatPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }


        CoroutineScope(Dispatchers.Main).launch {
            VotingList = ArrayList()
            val reference = mDbRef.child("Vote").child(gid!!)
            val totalUsers =
                DatabaseManager.fetchDataFromFirebase(reference.child("Size"), Int::class.java)
            val addrList =
                DatabaseManager.fetchDataListFromFirebase(reference.child("AddressList"), String::class.java)
            for (addr in addrList) {
                val itemName = DatabaseManager.fetchDataFromFirebase(
                    reference.child(addr).child("Name"),
                    String::class.java
                )
                val itemAddr = DatabaseManager.fetchDataFromFirebase(
                    reference.child(addr).child("Address"),
                    String::class.java
                )
                val itemUserList = DatabaseManager.fetchDataListFromFirebase(
                    reference.child(addr).child("userList"), String::class.java
                )
                for (user in itemUserList){
                    Log.d("item user list", user)
                }
                VotingList.add(VotingItem(itemName, itemAddr, itemUserList))
            }
                VotingView = findViewById(R.id.recyclableListVotes)
                VotingAdapter =
                    VotingAdapter(VotingList, gid, totalUsers!!, this@VotingPage)
                VotingView.adapter = VotingAdapter
                VotingAdapter.notifyDataSetChanged()

        }
    }
        fun closeVoting(item: VotingItem) {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("Activity", item.address)
            startActivity(intent)
            finish()
        }
}
