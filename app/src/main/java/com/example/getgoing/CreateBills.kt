package com.example.getgoing

import CreateBillsDisplayAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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

class CreateBills : AppCompatActivity() {

    lateinit var createBillEventRecyclerView: RecyclerView
    lateinit var memberList: ArrayList<User>
    lateinit var CreateBillsDisplayAdapter: CreateBillsDisplayAdapter
    lateinit var mDbRef: DatabaseReference
    lateinit var membersRef: DatabaseReference
    lateinit var billNameEDT: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_bills)
        supportActionBar?.hide()



        var groupID = GroupManager.currentGroup?.groupID
        var groupName = GroupManager.currentGroup?.name

        val backBtn = findViewById<ImageButton>(R.id.backToBills)
        backBtn.setOnClickListener {
            val intent = Intent(this, Bills::class.java)
            startActivity(intent)
            finish()
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
            createBillEventRecyclerView = findViewById(R.id.createBillMemberRecyclerView)
            CreateBillsDisplayAdapter = CreateBillsDisplayAdapter(memberList)
            createBillEventRecyclerView.adapter = CreateBillsDisplayAdapter
            CreateBillsDisplayAdapter.notifyDataSetChanged()
            val recyclerView = createBillEventRecyclerView
            val adapter = recyclerView.adapter as CreateBillsDisplayAdapter

            val doneBtn = findViewById<Button>(R.id.bill_done)
            doneBtn.setOnClickListener {
                billNameEDT = findViewById(R.id.edit_bill_name)
                var billName = billNameEDT.text.toString().trim()
                val members = adapter.getMemberList()
                if (billName.isEmpty()) {
                    Toast.makeText(this@CreateBills, "Please enter a bill name", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else{
                    for (i in 0 until adapter.itemCount) {
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(i)
                        if (viewHolder is CreateBillsDisplayAdapter.MemberViewHolder) {
                            val user = members[i]
                            var inputAmount = viewHolder.inputAmountCreateBill.text.toString().trim()
                            if(inputAmount == null) inputAmount = "0"
                            // Do something with the user and inputAmount
                            mDbRef = FirebaseDatabase.getInstance().getReference().child("Bills").child(groupID!!).child(billName).child(user.phone!!)
                            mDbRef.setValue(inputAmount)
                    }
                    val intent = Intent(this@CreateBills,Bills::class.java)
                    finish()
                    startActivity(intent)
                }
                }


            }






        }
    }
}