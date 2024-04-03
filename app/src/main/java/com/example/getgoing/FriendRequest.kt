package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendRequest : AppCompatActivity(), FriendItemClickListener {

    // on below line we are creating variables for
    // our swipe to refresh layout, recycler view,
    // adapter and list.
    lateinit var FriendEntry: RecyclerView
    lateinit var FriendListAdapter: FriendListAdapter
    lateinit var RemoveFrenAdapter: RemoveFriendAdapter
    lateinit var FriendsList: ArrayList<Friend>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_friend_request)
        supportActionBar?.hide()

        val backBtn = findViewById<ImageButton>(R.id.backToFriendList)
        backBtn.setOnClickListener {
            val intent = Intent(this, FriendList::class.java)
            startActivity(intent)
            finish()
        }
        CoroutineScope(Dispatchers.Main).launch {
            FriendEntry = findViewById(R.id.recyclableListFriends)
            FriendsList = ArrayList()
            RemoveFrenAdapter = RemoveFriendAdapter(FriendsList, this@FriendRequest)
            FriendEntry.adapter = RemoveFrenAdapter
            CurrentUserManager.currentUser?.let {
                FriendManager.getFriendsReq(it).forEach {
                    FriendsList.add(Friend(it.name!!, it.image!!, it.phone!!))
                }
            }
            RemoveFrenAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)
        val searchView: SearchView = searchItem.getActionView() as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                filter(msg)
                return false
            }
        })
        return true
    }


    private fun filter(text: String) {
        val filteredlist: ArrayList<Friend> = ArrayList()

        for (item in FriendsList) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            FriendListAdapter.filterList(filteredlist)
        }
    }

    override fun onAcceptButtonClick(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val clickedFriend = FriendsList[position]
            CurrentUserManager.addFriend(clickedFriend.phone)
            FriendsList.removeAt(position)
            RemoveFrenAdapter.notifyItemRemoved(position)
        }
    }

    // Implement the interface method to handle decline button clicks
    override fun onDeclineButtonClick(position: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val clickedFriend = FriendsList[position]
            CurrentUserManager.rejectFriendReq(clickedFriend.phone)
            FriendsList.removeAt(position)
            RemoveFrenAdapter.notifyItemRemoved(position)
        }
    }
}