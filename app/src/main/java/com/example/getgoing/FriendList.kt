package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.ImageButton
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendList : AppCompatActivity() {


    lateinit var FriendEntry: RecyclerView
    lateinit var FriendListAdapter: FriendListAdapter
    lateinit var FriendsList: ArrayList<Friend>
    lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      FRIEND LIST PAGE
        setContentView(R.layout.activity_friend_list)

        supportActionBar?.hide()
        val rqBtn = findViewById<Button>(R.id.goToFriendRequest)
        rqBtn.setOnClickListener {
            val intent = Intent(this, FriendRequest::class.java)
            startActivity(intent)
            finish()
        }

        val backBtn = findViewById<ImageButton>(R.id.backToFriendsPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, FriendsPage::class.java)
            startActivity(intent)
            finish()
        }

        val removeBtn = findViewById<Button>(R.id.friendlist_removebutton)
        removeBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                FriendListAdapter.getCheckedFriends().forEach {
                    CurrentUserManager.removeFriend(it.phone)
                    FriendsList.remove(it)

                }
                FriendListAdapter.notifyDataSetChanged()


            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            FriendEntry = findViewById(R.id.recyclableListFriends)
            FriendsList = ArrayList()
            FriendListAdapter = FriendListAdapter(FriendsList)
            FriendEntry.adapter = FriendListAdapter
            CurrentUserManager.currentUser?.let { it ->
                FriendManager.getFriends(it).forEach {
                    FriendsList.add(Friend(it.name!!, it.image!!, it.phone!!))
                }
            }
            FriendListAdapter.notifyDataSetChanged()
        }

        searchView = findViewById(R.id.idSV)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filter(query)
//                if (FriendList.map{it.name}.contains(query)) {
//                    // if query exist within list we
//                    // are filtering our list adapter.
//
//                    FriendListAdapter.filterList(FriendList)
//                } else {
//                    // if query is not present we are displaying
//                    // a toast message as no  data found..
//                    Toast.makeText(this@MainActivity, "No Language found..", Toast.LENGTH_LONG)
//                        .show()
//                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                FriendListAdapter.filterList(FriendsList)
                return false
            }
        })

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
            if (item.name.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            FriendListAdapter.filterList(filteredlist)
        }
    }

}
