package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VotingPage : AppCompatActivity() {


    lateinit var VotingView: RecyclerView
    lateinit var VotingAdapter: VotingAdapter
    lateinit var VotingList: ArrayList<VotingItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      FRIEND LIST PAGE
        setContentView(R.layout.activity_voting_page)

        supportActionBar?.hide()

        val gid: String? = intent.getStringExtra("GID")
        val totalUsers: Int? = intent.getStringExtra("members")?.toInt()
        val backBtn = findViewById<ImageButton>(R.id.backToChatPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, FriendsPage::class.java)
            startActivity(intent)
            finish()
        }

        CoroutineScope(Dispatchers.Main).launch {
            VotingView = findViewById(R.id.recyclableListVotes)
            VotingList = ArrayList()
            VotingAdapter = VotingAdapter(VotingList, gid!!, totalUsers!!, this@VotingPage)
            VotingView.adapter = VotingAdapter
//            VotingList.add(CurrentUserManager.)
            VotingAdapter.notifyDataSetChanged()
        }

    }

    fun CloseVoting(item: VotingItem){
        val intent = Intent(this, FriendsPage::class.java)
        intent.putExtra("Activity", item.name)
        startActivity(intent)
        finish()
    }

}
