package com.example.getgoing

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.Date
import java.text.SimpleDateFormat

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var spendingButton: ImageButton
    private lateinit var locationButton: ImageButton
    private lateinit var computeHangoutButton: ImageButton
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<com.example.getgoing.Message>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var groupTitle: TextView
    private lateinit var groupProfileButton: ImageButton


    @RequiresApi(Build.VERSION_CODES.O)
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

        CoroutineScope(Dispatchers.Main).launch {
            val activity = DatabaseManager.fetchDataFromFirebase(mDbRef.child("Groups").child(groupChatID!!).child("Activity"), String::class.java)
            val longitude = DatabaseManager.fetchDataFromFirebase(mDbRef.child("Vote").child(groupChatID).child(activity!!).child("Longitude"),Double::class.java)
            val latitude = DatabaseManager.fetchDataFromFirebase(mDbRef.child("Vote").child(groupChatID).child(activity).child("Latitude"),Double::class.java)

            val hangout = findViewById<TextView>(R.id.hangoutSpot)
            "Hangout : $activity".also { hangout.text = it }
            hangout.setOnClickListener {
                if(activity!=null){
                    val intent = Intent(this@ChatActivity,ComputedHangoutSpot::class.java)
                    Log.d("activity",activity.toString())
                    Log.d("latitude",latitude.toString())
                    Log.d("latitude",longitude.toString())
                    intent.putExtra("longitude",longitude)
                    intent.putExtra("latitude", latitude)
                    finish()
                    startActivity(intent)

                }
            }
        }

        supportActionBar?.title = name


        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        backButton = findViewById(R.id.backToListOfGroups)
        groupProfileButton = findViewById(R.id.edit_grp_ProfilePage)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        groupTitle = findViewById(R.id.idTVHeading)



        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter
        Log.d("GroupIDs", "Group IDs: $groupChatID")

        groupTitle.setText(GroupManager.currentGroup?.name)

        mDbRef.child("Groups").child(groupChatID!!).child("chat")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {

                        val message =
                            postSnapshot.getValue(com.example.getgoing.Message::class.java)


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

        backButton.setOnClickListener {
            val intent = Intent(this, GroupDisplay::class.java)
            finish()
            startActivity(intent)
        }

        groupProfileButton.setOnClickListener {
            val intent = Intent(this@ChatActivity, GroupProfile::class.java)
            finish()
            startActivity(intent)
        }


        // bottom 3 buttons
        spendingButton = findViewById(R.id.spending)
        locationButton = findViewById(R.id.location)
        computeHangoutButton = findViewById(R.id.computeHangout)




        computeHangoutButton.setOnClickListener {
            val intent = Intent(this, ComputeHangout2::class.java)
            finish()
            startActivity(intent)
        }

        locationButton.setOnClickListener {
            val intent = Intent(this,SetLocation::class.java)
            intent.putExtra("From", "chat")
            finish()
            startActivity(intent)
        }

        spendingButton.setOnClickListener {
            val intent = Intent(this,Bills::class.java)
            intent.putExtra("From", "chat")
            finish()
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.group_edit_menu) {
            val intent = Intent(this@ChatActivity, GroupProfile::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == R.id.group_add_friend_menu) {
            val intent = Intent(this@ChatActivity, CreateGroupFriends::class.java)
            startActivity(intent)
            return true
        }
        return true
    }

}