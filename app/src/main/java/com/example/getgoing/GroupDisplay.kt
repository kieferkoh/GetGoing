package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        val currentUser = CurrentUserManager.getCurrentUser()
        val currentPhone = currentUser!!.phone.toString()
        groupChatRecyclerView = findViewById(R.id.group_chats_recycler)
        CoroutineScope(Dispatchers.Main).launch {
            // Call the suspend function within the coroutine scope
            groupList = CurrentUserManager.getGroupList(currentPhone)

            // Update UI or perform further operations here with groupList
        }
        GroupDisplayAdapter = GroupDisplayAdapter(groupList)
        groupChatRecyclerView.adapter = GroupDisplayAdapter

        GroupDisplayAdapter.notifyDataSetChanged()






    }}