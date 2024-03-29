package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import kotlin.math.sign

class MainScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val friendsButton = findViewById<ImageButton>(R.id.friendPageBut)
        val groupsButton = findViewById<ImageButton>(R.id.groupPageBut)
        val signOutButton = findViewById<Button>(R.id.SignOut)

        friendsButton.setOnClickListener {
//             Navigate to the friends page
            val intent = Intent(this, FriendsPage::class.java)
            startActivity(intent)
        }

        groupsButton.setOnClickListener {
//             Navigate to the groups page
            val intent = Intent(this, GroupsPage::class.java)
            startActivity(intent)
        }

        signOutButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

    }
}