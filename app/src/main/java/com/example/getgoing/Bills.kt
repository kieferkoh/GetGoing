package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.getgoing.databinding.BillEventItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Bills : AppCompatActivity() {

    lateinit var billEventRecyclerView: RecyclerView
    lateinit var eventList: ArrayList<Event>
    lateinit var BillsDisplayAdapter: BillsDisplayAdapter
    lateinit var mDbRef: DatabaseReference
    lateinit var eventRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bills)
        supportActionBar?.hide()

        val backBtn = findViewById<ImageButton>(R.id.backToChatPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
            finish()
        }

        val createBillsButton = findViewById<ImageButton>(R.id.createBillButton)

        createBillsButton.setOnClickListener {
            val intent = Intent(this, CreateBills::class.java)
            startActivity(intent)
            finish()
        }

        var groupID = GroupManager.currentGroup?.groupID
        var groupName = GroupManager.currentGroup?.name


//        eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Path is populated
//                    var eventListStrings:ArrayList<String>? = null
//                    dataSnapshot.getValue(ArrayList<String>::class.java)
//                } else {
//                    // Path is empty
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//            }
//        })


        CoroutineScope(Dispatchers.Main).launch {
            eventList = arrayListOf()
            eventRef = FirebaseDatabase.getInstance().getReference("Bills").child(groupID!!)
            val eventListString = DatabaseManager.fetchDataListFromFirebase(
                eventRef.child("EventList"),
                String::class.java
            )
            for (event in eventListString){
                var totalAmount = DatabaseManager.fetchDataFromFirebase(eventRef.child(event), Int::class.java)
                eventList.add(Event(event,totalAmount!!))
            }
            billEventRecyclerView = findViewById(R.id.recyclableListBills)

            BillsDisplayAdapter = BillsDisplayAdapter(eventList)
            billEventRecyclerView.adapter = BillsDisplayAdapter
            BillsDisplayAdapter.notifyDataSetChanged()


        }


    }
}
