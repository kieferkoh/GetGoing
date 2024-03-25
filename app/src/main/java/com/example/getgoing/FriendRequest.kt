package com.example.getgoing

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView

class FriendRequest : AppCompatActivity() {

    // on below line we are creating variables for
    // our swipe to refresh layout, recycler view,
    // adapter and list.
    lateinit var FriendEntry: RecyclerView
    lateinit var FriendListAdapter: FriendListAdapter
    lateinit var RemoveFrenAdapter: RemoveFriendAdapter
    lateinit var FriendList: ArrayList<Friend>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_friend_request)

        val backBtn = findViewById<ImageButton>(R.id.backToFriendList)
        backBtn.setOnClickListener {
            val intent = Intent(this, FriendList::class.java)
            startActivity(intent)
            finish()
        }
        // on below line we are initializing our views with their ids.
        FriendEntry = findViewById(R.id.recyclableListFriends)

        // on below line we are initializing our list
        FriendList = ArrayList()

        // on below line we are initializing our adapter
        RemoveFrenAdapter = RemoveFriendAdapter(FriendList)

        // on below line we are setting adapter to our recycler view.
        FriendEntry.adapter = RemoveFrenAdapter

        // on below line we are adding data to our list
        FriendList.add(Friend("Android Development", R.drawable.egg_prata))
        FriendList.add(Friend("C++ Development", R.drawable.harold_meme))
        FriendList.add(Friend("Java Development", R.drawable.io))
        FriendList.add(Friend("Python Development", R.drawable.egg_prata))
        FriendList.add(Friend("JavaScript Development", R.drawable.harold_meme))

        // on below line we are notifying adapter
        // that data has been updated.
        RemoveFrenAdapter.notifyDataSetChanged()
    }

    // calling on create option menu
    // layout to inflate our menu file.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // below line is to get our inflater
        val inflater = menuInflater

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu)

        // below line is to get our menu item.
        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)

        // getting search view of our item.
        val searchView: SearchView = searchItem.getActionView() as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
            }
        })
        return true
    }


    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Friend> = ArrayList()

        // running a for loop to compare elements.
        for (item in FriendList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            FriendListAdapter.filterList(filteredlist)
        }
    }
}