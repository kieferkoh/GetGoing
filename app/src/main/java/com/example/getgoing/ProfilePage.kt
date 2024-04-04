package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_personal_profile)
        supportActionBar?.hide()

        val user = CurrentUserManager.currentUser

        // Username
        val username = findViewById<EditText>(R.id.editName)
        username.hint = CurrentUserManager.currentUser?.name


        // Profile picture
        val profilePicture = findViewById<ImageButton>(R.id.setDP)
        profilePicture.setImageResource(user?.image!!)
        profilePicture.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }
        // Location
//        val currentLocation = findViewById<TextView>(R.id.curLocation)
//        currentLocation.text = user.location

        // Navigation buttons
        val backBtn = findViewById<ImageButton>(R.id.backToMainScreen)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }

        val doneBtn = findViewById<Button>(R.id.doneToMainScreen)
        doneBtn.setOnClickListener {
            // Input checks
            user.name = username.text.toString()
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }

    }
}