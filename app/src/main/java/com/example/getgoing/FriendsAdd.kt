package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendsAdd : AppCompatActivity() {
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_add_friend)
        supportActionBar?.hide()

        val backBtn = findViewById<ImageButton>(R.id.backToFriendPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }

        val editText = findViewById<TextInputEditText>(R.id.friendnumber) // need to create textcheck
        val addBtn = findViewById<Button>(R.id.addFriendButton)
        addBtn.setOnClickListener {
        CoroutineScope(Dispatchers.Main).launch {
                sendFriendRequest(editText.text.toString())
            }
        }
        }
    private suspend fun sendFriendRequest(phone: String?) {
        if (phone != null) {
            Toast.makeText(this, "Friend request sent to $phone", Toast.LENGTH_SHORT).show()
            CurrentUserManager.sendFriendReq(phone)
        }
    }}