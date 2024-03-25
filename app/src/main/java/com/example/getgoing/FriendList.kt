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

class FriendList : AppCompatActivity() {

    // on below line we are creating variables for
    // our swipe to refresh layout, recycler view,
    // adapter and list.
    lateinit var FriendEntry: RecyclerView
    lateinit var FriendListAdapter: FriendListAdapter
    lateinit var FriendList: ArrayList<Friend>


    // creating array adapter for listview
    lateinit var listAdapter: ArrayAdapter<String>

    // creating array list for listview
    lateinit var programmingLanguagesList: ArrayList<String>;

    // creating variable for searchview
    lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      FRIEND LIST PAGE
        setContentView(R.layout.activity_friend_list)

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
        // on below line we are initializing our views with their ids.
        FriendEntry = findViewById(R.id.recyclableListFriends)

        // on below line we are initializing our list
        FriendList = ArrayList()

        // on below line we are initializing our adapter
        FriendListAdapter = FriendListAdapter(FriendList)

        // on below line we are setting adapter to our recycler view.
        FriendEntry.adapter = FriendListAdapter

        // on below line we are adding data to our list
        FriendList.add(Friend("Android Development", R.drawable.egg_prata))
        FriendList.add(Friend("C++ Development", R.drawable.harold_meme))
        FriendList.add(Friend("Java Development", R.drawable.io))
        FriendList.add(Friend("Python Development", R.drawable.egg_prata))
        FriendList.add(Friend("JavaScript Development", R.drawable.harold_meme))

        // initializing list adapter and setting layout
        // for each list view item and adding array list to it.
//        listAdapter = ArrayAdapter<String>(
//            this,
//            android.R.layout.simple_list_item_1,
//            programmingLanguagesList
//        )
   //     programmingLanguagesLV.adapter = listAdapter
        FriendEntry = findViewById(R.id.recyclableListFriends)
        searchView = findViewById(R.id.idSV)
        // on below line we are adding on query
        // listener for our search view.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // on below line we are checking
                // if query exist or not.
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
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.
                FriendListAdapter.filterList(FriendList)
                return false
            }
        })

        // on below line we are notifying adapter
        // that data has been updated.
        FriendListAdapter.notifyDataSetChanged()



//
//

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

    private fun deleteFriend(friend: Friend){
        FriendList.remove(friend)
        FriendListAdapter.notifyDataSetChanged()
    }
}
