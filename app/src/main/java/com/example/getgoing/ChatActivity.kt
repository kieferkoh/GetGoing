package com.example.getgoing

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var spendingButton: ImageButton
    private lateinit var locationButton: ImageButton
    private lateinit var computeHangoutButton: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<com.example.getgoing.Message>
    private lateinit var mDbRef: DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)

        supportActionBar?.hide()

        val intent = Intent()
        val name = GroupManager.currentGroup?.name
        val senderUid = CurrentUserManager.currentUser?.phone
        val groupChatID = GroupManager.currentGroup?.groupID
        //val groupChatID = intent.getStringExtra("group_id")
        Log.d("GroupIDs", "Group IDs: $groupChatID")

        mDbRef = FirebaseDatabase.getInstance().getReference()

        supportActionBar?.title = name


        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)



        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter
        Log.d("GroupIDs", "Group IDs: $groupChatID")

        mDbRef.child("Groups").child(groupChatID!!).child("chat")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {

                        val message = postSnapshot.getValue(com.example.getgoing.Message::class.java)


                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)
            mDbRef.child("Groups").child(groupChatID).child("chat").push()
                .setValue(messageObject)
            messageBox.setText("")
        }







        // bottom 3 buttons
        spendingButton = findViewById(R.id.spending)
        locationButton = findViewById(R.id.location)
        computeHangoutButton = findViewById(R.id.computeHangout)




        computeHangoutButton.setOnClickListener {
            val intent = Intent(this, ComputeHangout::class.java)
            finish()
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.group_edit_menu){
            val intent = Intent(this@ChatActivity,GroupProfile::class.java)
            startActivity(intent)
            return true
        }
        else if(item.itemId == R.id.group_add_friend_menu){
            val intent = Intent(this@ChatActivity,CreateGroupFriends::class.java)
            startActivity(intent)
            return true
        }
        return true
    }

}