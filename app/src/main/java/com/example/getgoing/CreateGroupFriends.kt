package com.example.getgoing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CreateGroupFriends : AppCompatActivity() {

    private lateinit var newRecyclerview: RecyclerView
    private lateinit var newArrayList: ArrayList<Friend>
    lateinit var imageId: Array<Int>
    lateinit var friendNames: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group_friend_list)

        imageId = arrayOf(
            R.drawable.bob,
            R.drawable.roy,
            R.drawable.tino
        )

        friendNames = arrayOf(
            "Bob",
            "Roy",
            "Valentino"
        )

        newRecyclerview = findViewById(R.id.groupFriendsRecyclerView)
        newRecyclerview.layoutManager = LinearLayoutManager(this)
        newRecyclerview.setHasFixedSize(true)

        newArrayList = arrayListOf<Friend>()
        getUserdata()

        val backBtn = findViewById<Button>(R.id.cancelToGroupsPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, GroupsPage::class.java)
            startActivity(intent)
            finish()
        }

        val doneBtn = findViewById<Button>(R.id.doneToGroupsPage)
        doneBtn.setOnClickListener {
            val intent = Intent(this, GroupsPage::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun getUserdata() {
        for (i in imageId.indices) {
            val friends = Friend(friendNames[i], imageId[i])
            newArrayList.add(friends)
        }

        newRecyclerview.adapter = GroupFriendsAdapter(newArrayList)
    }



}