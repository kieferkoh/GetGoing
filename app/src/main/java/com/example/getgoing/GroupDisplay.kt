package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupDisplay : AppCompatActivity() {
    lateinit var groupChatRecyclerView: RecyclerView
    lateinit var groupList: ArrayList<Group>
    lateinit var GroupDisplayAdapter: GroupDisplayAdapter
    //lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_group_display)

        val backBtn = findViewById<ImageButton>(R.id.backToGroupsPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, GroupsPage::class.java)
            startActivity(intent)
            finish()
        }
        val currentUser = CurrentUserManager.currentUser
        val currentPhone = currentUser!!.phone.toString()
        groupChatRecyclerView = findViewById(R.id.group_chats_recycler)
//        CoroutineScope(Dispatchers.Main).launch {
//            // Call the suspend function within the coroutine scope
//            groupList = CurrentUserManager.getGroupList(currentPhone)
//
//            // Update UI or perform further operations here with groupList
//        }

        groupList = ArrayList()
        groupList.add(Group(
            "test_group",
            "232ddf1e-49da-40f1-b91b-e12c05b6beda",
            R.drawable.bob,
            arrayListOf("979797", "989898"),
            arrayListOf(Message("test", "989898"), Message("test2", "979797"))
        ))
        GroupDisplayAdapter = GroupDisplayAdapter(groupList)
        groupChatRecyclerView.adapter = GroupDisplayAdapter
        groupChatRecyclerView.layoutManager = LinearLayoutManager(this)
//        newRecyclerview.setHasFixedSize(true)

        GroupDisplayAdapter.notifyDataSetChanged()






    }}