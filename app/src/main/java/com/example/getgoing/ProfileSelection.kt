package com.example.getgoing

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileSelection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_select_profile_image)
        supportActionBar?.hide()

        val backBtn = findViewById<ImageButton>(R.id.backToPersonProfile)
        backBtn.setOnClickListener {
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
        }

        val avatar1 = findViewById<ImageButton>(R.id.profilePT1)
        avatar1.setOnClickListener{
            setUserProfilePicture(R.drawable.avatar1)
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
        }
        val avatar2 = findViewById<ImageButton>(R.id.profilePT2)
        avatar2.setOnClickListener{
            setUserProfilePicture(R.drawable.avatar2)
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
        }
        val avatar3 = findViewById<ImageButton>(R.id.profilePT3)
        avatar3.setOnClickListener{
            setUserProfilePicture(R.drawable.avatar3)
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
        }
        val avatar4 = findViewById<ImageButton>(R.id.profilePT4)
        avatar4.setOnClickListener{
            setUserProfilePicture(R.drawable.avatar4)
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
        }
        val avatar5 = findViewById<ImageButton>(R.id.profilePT5)
        avatar5.setOnClickListener{
            setUserProfilePicture(R.drawable.avatar5)
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
        }
        val avatar6 = findViewById<ImageButton>(R.id.profilePT6)
        avatar6.setOnClickListener{
            setUserProfilePicture(R.drawable.avatar7)
            val intent = Intent(this, ProfilePage::class.java)
            startActivity(intent)
            finish()
        }


    }
    private fun setUserProfilePicture(drawable: Int){
        CoroutineScope(Dispatchers.Main).launch {
            CurrentUserManager.updateDP(drawable)
        }
    }
}