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

class GroupImageSelection : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_select_group_image)
        supportActionBar?.hide()

        val backBtn = findViewById<ImageButton>(R.id.backToGroupProfile)
        backBtn.setOnClickListener {
            val intent = Intent(this, GroupProfile::class.java)
            startActivity(intent)
            finish()
        }

        val avatar1 = findViewById<ImageButton>(R.id.profilePT1)
        avatar1.setOnClickListener{
            setGroupPicture(R.drawable.groupavatar1)
            val intent = Intent(this, GroupProfile::class.java)
            startActivity(intent)
            finish()
        }
        val avatar2 = findViewById<ImageButton>(R.id.profilePT2)
        avatar2.setOnClickListener{
            setGroupPicture(R.drawable.groupavatar2)
            val intent = Intent(this, GroupProfile::class.java)
            startActivity(intent)
            finish()
        }
        val avatar3 = findViewById<ImageButton>(R.id.profilePT3)
        avatar3.setOnClickListener{
            setGroupPicture(R.drawable.groupavatar3)
            val intent = Intent(this, GroupProfile::class.java)
            startActivity(intent)
            finish()
        }
        val avatar4 = findViewById<ImageButton>(R.id.profilePT4)
        avatar4.setOnClickListener{
            setGroupPicture(R.drawable.groupavatar4)
            val intent = Intent(this, GroupProfile::class.java)
            startActivity(intent)
            finish()
        }
        val avatar5 = findViewById<ImageButton>(R.id.profilePT5)
        avatar5.setOnClickListener{
            setGroupPicture(R.drawable.groupavatar5)
            val intent = Intent(this, GroupProfile::class.java)
            startActivity(intent)
            finish()
        }
        val avatar6 = findViewById<ImageButton>(R.id.profilePT6)
        avatar6.setOnClickListener{
            setGroupPicture(R.drawable.groupavatar6)
            val intent = Intent(this, GroupProfile::class.java)
            startActivity(intent)
            finish()
        }


    }
    private fun setGroupPicture(drawable: Int){
        CoroutineScope(Dispatchers.Main).launch {
            GroupManager.setGroupDP(drawable)
        }
    }
}