package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GroupDisplay : AppCompatActivity() {
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

    }}