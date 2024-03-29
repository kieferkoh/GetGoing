package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import java.util.Locale

class CreateGroupFriends : AppCompatActivity() {


    lateinit var FriendEntry: RecyclerView
    lateinit var FriendListAdapter: FriendListAdapter
    lateinit var FriendList: ArrayList<Friend>
    lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      GROUP FRIEND LIST PAGE
        setContentView(R.layout.activity_create_group_friend_list)

        val doneBtn = findViewById<Button>(R.id.doneToGroupsPage)
        doneBtn.setOnClickListener {
            val intent = Intent(this, GroupDisplay::class.java)
            startActivity(intent)
            finish()
        }

        val backBtn = findViewById<Button>(R.id.cancelToGroupsPage)
        backBtn.setOnClickListener {
            val intent = Intent(this, CreateGroupName::class.java)
            startActivity(intent)
            finish()
        }

        FriendEntry = findViewById(R.id.groupFriendsRecyclerView)
        FriendList = ArrayList()
        FriendListAdapter = FriendListAdapter(FriendList)
        FriendEntry.adapter = FriendListAdapter

        // on below line we are adding data to our list
//        FriendList.add(Friend("Bob", R.drawable.bob))
//        FriendList.add(Friend("Roy", R.drawable.roy))
//        FriendList.add(Friend("Tino", R.drawable.tino))


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
                FriendListAdapter.filterList(FriendList)
                return false
            }
        })
        FriendListAdapter.notifyDataSetChanged()

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

        for (item in FriendList) {
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

    private fun addFriendToGroup(friend: Friend){
        // Add friend to group database
    }
}
