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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GroupEdit : AppCompatActivity() {
    private lateinit var edtChangeGroupName: EditText
    private lateinit var okBtn: Button
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_group_edit)

        val backBtn = findViewById<ImageButton>(R.id.backToGroupProfile)
        backBtn.setOnClickListener {
            val intent = Intent(this, GroupProfile::class.java)
            startActivity(intent)
            finish()
        }

        okBtn = findViewById<Button>(R.id.goToGroupProfile)
        edtChangeGroupName = findViewById(R.id.change_group_name)
        mDbRef = FirebaseDatabase.getInstance().getReference()





        okBtn.setOnClickListener {
            if (edtChangeGroupName.text.toString() != null) {
                var myGroup = GroupManager.currentGroup

                mDbRef.child("Groups").child(myGroup?.groupID!!).child("name")
                    .setValue(edtChangeGroupName.text.toString())
                val intent = Intent(this, GroupProfile::class.java)
                GroupManager.currentGroup?.name = edtChangeGroupName.text.toString()
                startActivity(intent)
                finish()
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
}