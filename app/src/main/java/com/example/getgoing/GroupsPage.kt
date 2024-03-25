package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class GroupsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_groups)

        val backBtn = findViewById<ImageButton>(R.id.backToMainScreen)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }

        val createBtn = findViewById<ImageButton>(R.id.goToCreateGroupName)
        createBtn.setOnClickListener {
            val intent = Intent(this, CreateGroupName::class.java)
            startActivity(intent)
            finish()
        }

        val displayBtn = findViewById<ImageButton>(R.id.goToGroupDisplay)
        displayBtn.setOnClickListener {
            val intent = Intent(this, GroupDisplay::class.java)
            startActivity(intent)
            finish()
        }
    }}