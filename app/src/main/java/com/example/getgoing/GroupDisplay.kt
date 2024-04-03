package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupDisplay : AppCompatActivity() {
    lateinit var groupChatRecyclerView: RecyclerView
    lateinit var groupList: ArrayList<Group>
    lateinit var GroupDisplayAdapter: GroupDisplayAdapter
    lateinit var mDbRef: DatabaseReference

    //lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_group_display)

        supportActionBar?.hide()
        val backBtn = findViewById<ImageButton>(R.id.backToGroupsPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, GroupsPage::class.java)
            startActivity(intent)
            finish()
        }
        val myUser = CurrentUserManager.currentUser
        val currentPhone = myUser?.phone.toString()
        groupChatRecyclerView = findViewById(R.id.group_chats_recycler)
        groupChatRecyclerView.layoutManager = LinearLayoutManager(this)
        groupList = ArrayList()
        mDbRef = FirebaseDatabase.getInstance().getReference("Groups")


        var groupIDs = myUser?.groups
        groupList.clear()
        for (id in groupIDs!!) {
            mDbRef.child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val groupImage = snapshot.child("image").getValue(Int::class.java)

                        val groupName = snapshot.child("name").getValue(String::class.java)
                        groupList.add(Group(id, groupImage,groupName))
                        GroupDisplayAdapter = GroupDisplayAdapter(groupList)
                        groupChatRecyclerView.adapter = GroupDisplayAdapter
                        groupChatRecyclerView.layoutManager = LinearLayoutManager(this@GroupDisplay)

                        GroupDisplayAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

        }





//        CoroutineScope(Dispatchers.Main).launch {
//            // Call the suspend function within the coroutine scope
//            var groupIDs = myUser?.groups
//            Log.d("GroupIDs", "Group IDs: $groupIDs")
//            groupList = GroupManager.getGroups(groupIDs!!)
//            Log.d("GroupIDs", "Group IDs: $groupList")
//            GroupDisplayAdapter = GroupDisplayAdapter(groupList)
//            groupChatRecyclerView.adapter = GroupDisplayAdapter
//            groupChatRecyclerView.layoutManager = LinearLayoutManager(this@GroupDisplay)
//
//            GroupDisplayAdapter.notifyDataSetChanged()
//
//            // Update UI or perform further operations here with groupList
//        }


    }
}