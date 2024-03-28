package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class FriendsAdd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_add_friend)

        val backBtn = findViewById<ImageButton>(R.id.backToFriendPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }

        val editText = findViewById<TextInputEditText>(R.id.friendnumber)
        val addBtn = findViewById<Button>(R.id.addFriendButton)
        addBtn.setOnClickListener {
            // if editText ID not in database, reject
            // else add friend
            sendFriendRequest(editText.toString(), CurrentUserManager.getCurrentUser()!!.phone)
        }

        }
    fun sendFriendRequest(friendID: String?, currentUserID: String?) {
        // Add request to friendID in database
    }}