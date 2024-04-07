package com.example.getgoing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.getgoing.databinding.ActivityComputedHangoutSpotBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ComputedHangoutSpot : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityComputedHangoutSpotBinding
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDbRef = FirebaseDatabase.getInstance().getReference()
        binding = ActivityComputedHangoutSpotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<ImageButton>(R.id.backToChatPage).setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        val latitude = intent.getDoubleExtra("latitude",1.290270)
        Log.d("latitude",latitude.toString())
        val longitude = intent.getDoubleExtra("longitude",103.851959)
        Log.d("latitude",latitude.toString())

        // Add a marker in Sydney and move the camera
        val hangoutSpot = LatLng(latitude, longitude)
        mMap.addMarker(
            MarkerOptions().position(hangoutSpot).title("computed hangout spot").icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE
                )
            )
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hangoutSpot,12f))
        showFriendsLocation()
    }

    private fun showFriendsLocation() {
        getMembers { members ->
            getLocationsFromMembers(members) { locations ->
                for (location in locations) {
                    mMap.addMarker(MarkerOptions().position(location).title("Members Locations"))
                }

            }

        }
    }

    private fun getMembers(callback: (ArrayList<String>) -> Unit) {
        val currentGroupID = GroupManager.currentGroup?.groupID
        var membersReference = mDbRef.child("Groups").child(currentGroupID!!).child("members")
        membersReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    var members = ArrayList<String>()

                    // Iterate through each child node to retrieve members
                    for (memberSnapshot in snapshot.children) {
                        val member = memberSnapshot.getValue(String::class.java)
                        members.add(member!!)
                    }
                    // Now 'members' list contains the array of member strings
                    // You can use 'members' list here as needed
                    callback(members)
                } else {
                    // Handle case where members node does not exist or is empty
                    Toast.makeText(this@ComputedHangoutSpot, "Error: restart app", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun getLocationsFromMembers(
        members: ArrayList<String>,
        callback: (ArrayList<LatLng>) -> Unit
    ) {

        for (member in members) {
            val locationReference = mDbRef.child("User").child(member).child("location")
            var locations = ArrayList<LatLng>()

            locationReference
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val longitude = snapshot.child("longitude").getValue(Double::class.java)
                        val latitude = snapshot.child("latitude").getValue(Double::class.java)
                        val location = LatLng(latitude!!, longitude!!)


                        mMap.addMarker(
                            MarkerOptions().position(location).title("Members Locations")
                        )
                        locations.add(location)

                        if (locations.size == members.size) {

                            callback(locations)

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

        }

    }
}