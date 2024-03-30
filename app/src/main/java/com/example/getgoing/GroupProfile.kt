package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GroupProfile : AppCompatActivity() {

    private lateinit var setGroupName: ImageButton
    private lateinit var addMemberGroupProfile: ImageButton
    private lateinit var groupNameTitle: TextView
    private lateinit var groupNameBanner: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_group_profile)

        val backBtn = findViewById<ImageButton>(R.id.backToGroupDisplay)
        backBtn.setOnClickListener {
            val intent = Intent(this, GroupDisplay::class.java)
            startActivity(intent)
            finish()
        }


        addMemberGroupProfile = findViewById(R.id.add_member_group_profile)
        setGroupName = findViewById(R.id.setGroupName)
        groupNameBanner = findViewById(R.id.group_name_group_profile)
        groupNameTitle = findViewById(R.id.GroupProfileTitle)

        groupNameTitle.setText(GroupManager.currentGroup?.name)
        groupNameBanner.setText(GroupManager.currentGroup?.name)

        addMemberGroupProfile.setOnClickListener {
            val intent = Intent(this@GroupProfile, CreateGroupFriends::class.java)
            startActivity(intent)
        }

        setGroupName.setOnClickListener {
            val id = GroupManager.currentGroup?.groupID
            Log.d("GroupIDs", "Group IDs: $id")
            val intent = Intent(this@GroupProfile,GroupEdit::class.java)
            startActivity(intent)
        }

    }}