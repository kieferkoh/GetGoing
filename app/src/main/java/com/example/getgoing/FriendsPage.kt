package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FriendsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_friend_page)

        val backBtn = findViewById<ImageButton>(R.id.backToMainScreen)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }

        val addBtn = findViewById<ImageButton>(R.id.goToAddFriend)
        addBtn.setOnClickListener {
            val intent = Intent(this, FriendsAdd::class.java)
            startActivity(intent)
            finish()
        }

        val viewBtn = findViewById<ImageButton>(R.id.goToFriendList)
        viewBtn.setOnClickListener {
            val intent = Intent(this, FriendList::class.java)
            startActivity(intent)
            finish()
        }
}}