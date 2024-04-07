package com.example.getgoing

import RespectiveBillDisplayAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RespectiveBill : AppCompatActivity() {
    private lateinit var billName: TextView
    private lateinit var billCost: TextView
    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var RespectiveBillDisplayAdapter: RespectiveBillDisplayAdapter
    private lateinit var membersRef: DatabaseReference
    private lateinit var memberList: ArrayList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_respective_bill)
        val eventName = intent.getStringExtra("eventName")
        val totalAmount: Double = intent.getDoubleExtra("totalAmount", 0.0)
        val groupID = GroupManager.currentGroup?.groupID

        billName = findViewById(R.id.billName)
        billName.text = eventName

        billCost = findViewById(R.id.billCost)
        billCost.text = totalAmount.toString()

        val backBtn = findViewById<ImageButton>(R.id.backToBills)
        backBtn.setOnClickListener {
            intent= Intent(this,Bills::class.java)
            finish()
            startActivity(intent)
        }

        CoroutineScope(Dispatchers.Main).launch {
            memberList = arrayListOf()
            membersRef = FirebaseDatabase.getInstance().getReference("Groups").child(groupID!!)
                .child("members")
            val userListString = DatabaseManager.fetchDataListFromFirebase(
                membersRef,
                String::class.java
            )
            for (phone in userListString) {
                var user = CurrentUserManager.getUserByPhone(phone)
                memberList.add(user!!)
            }
            memberRecyclerView = findViewById(R.id.recyclableListBills)
            RespectiveBillDisplayAdapter = RespectiveBillDisplayAdapter(memberList, eventName!!)
            memberRecyclerView.adapter = RespectiveBillDisplayAdapter
            RespectiveBillDisplayAdapter.notifyDataSetChanged()


        }
    }
}