package com.example.getgoing

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.getgoing.databinding.ActivityComputeHangout2Binding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.io.IOException

class ComputeHangout2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityComputeHangout2Binding
    private lateinit var mDbRef: DatabaseReference
    private lateinit var searchView: SearchView
    private lateinit var placesClient: PlacesClient
    var totalLongitude: Double = 0.0
    var totalLatitude: Double = 0.0
    var membersSize: Int = 2
    var places: ArrayList<Place>? = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        mDbRef = FirebaseDatabase.getInstance().getReference()
        binding = ActivityComputeHangout2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Back Button

        findViewById<ImageButton>(R.id.backToChatPage).setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            finish()
            startActivity(intent)
        }

        // Get Friends Location
        findViewById<Button>(R.id.getFriendsLocationButton).setOnClickListener {
            mMap.clear()
            showFriendsLocation()
        }

        // Start Vote

        findViewById<Button>(R.id.startVoteButton).setOnClickListener {
            if (places!!.isEmpty()) {
                Toast.makeText(this, "Input an activity first", Toast.LENGTH_SHORT).show()
            } else {

                GroupManager.places = places
                val senderUid = "Voting"
                val groupChatID = GroupManager.currentGroup?.groupID
                val message = "Vote for Hangout Spot Here!!"
                val messageObject = Message(message, senderUid, true)
                mDbRef.child("Vote").child(groupChatID!!).setValue(arrayListOf(""))
                mDbRef.child("Groups").child(groupChatID).child("chat").push()
                    .setValue(messageObject)
                createVoteDatabase()
                val intent = Intent(this, VotingPage::class.java)
                intent.putExtra("GID", GroupManager.currentGroup?.groupID)
                finish()
                startActivity(intent)
            }
        }



        Places.initialize(applicationContext, "AIzaSyCBtPj2MvYWZHkyL5BSu2OCMtAujsJZszI")
        placesClient = Places.createClient(this)

        // Initialize searchView
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    mMap.clear()
                    performAutocomplete(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text changes in the search bar if needed
                return true
            }
        })

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
        if (CurrentUserManager.currentUser != null) {
            mDbRef.child("User").child(CurrentUserManager.currentUser?.phone!!).child("location")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val longitude = snapshot.child("longitude").getValue(Double::class.java)
                        val latitude = snapshot.child("latitude").getValue(Double::class.java)
                        val singapore = LatLng(latitude!!, longitude!!)
                        mMap.addMarker(
                            MarkerOptions().position(singapore).title("Marker in Singapore")
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, 12f))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        } else {


            // Add a marker in Singapore and move the camera
            val singapore = LatLng(1.3521, 103.8198)
            mMap.addMarker(MarkerOptions().position(singapore).title("Marker in Singapore"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, 12f))
        }
    }


    // NEXT three functions are to show friends locations
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
                    Toast.makeText(this@ComputeHangout2, "Error: restart app", Toast.LENGTH_SHORT)
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

        membersSize = members.size
        totalLatitude = 0.0
        totalLongitude = 0.0
        for (member in members) {
            val locationReference = mDbRef.child("User").child(member).child("location")
            var locations = ArrayList<LatLng>()

            locationReference
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val longitude = snapshot.child("longitude").getValue(Double::class.java)
                        val latitude = snapshot.child("latitude").getValue(Double::class.java)
                        val location = LatLng(latitude!!, longitude!!)

                        totalLatitude += latitude
                        totalLongitude += longitude

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


    // Searching functions
    private fun performAutocomplete(query: String) {
        showFriendsLocation()
        GroupManager.places = null
        places?.clear()
        val averageLocation =
            LatLng(totalLatitude / membersSize, totalLongitude / membersSize)
        Log.d("average11", "average: $averageLocation")

        averageLocation.let { location ->
            // Only proceed if averageLocation is not null
            val bias = RectangularBounds.newInstance(
                LatLng(location.latitude - 0.001, location.longitude - 0.001),
                LatLng(location.latitude + 0.001, location.longitude + 0.001)
            )

            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setCountries("SG") // Restrict predictions to Singapore
                .setLocationBias(bias)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    // Handle Autocomplete prediction response

                    var counter = 0 // Counter to keep track of the number of results
                    for (prediction in response.autocompletePredictions) {
                        if (counter >= 5) {
                            break
                        }
                        // Process each prediction

                        val placeId = prediction.placeId
                        val placeName = prediction.getPrimaryText(null).toString()

                        // Retrieve place details using the Place ID
                        val placeRequest =
                            FetchPlaceRequest.newInstance(
                                placeId,
                                listOf(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME)
                            )

                        placesClient.fetchPlace(placeRequest)
                            .addOnSuccessListener { response ->
                                val place = response.place
                                places?.add(place)
                                val latLng = place.latLng
                                val address = place.address

                                // Display marker on the map
                                if (latLng != null && address!=null) {
                                    counter++
                                    mMap.addMarker(
                                        MarkerOptions().position(latLng).title(placeName).icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_BLUE
                                            )
                                        )
                                    )

                                }

                            }
                            .addOnFailureListener { exception ->
                                // Handle error
                                Log.e(
                                    "FetchPlace",
                                    "Failed to fetch place details: ${exception.message}"
                                )
                            }

                    }
                }
                .addOnFailureListener { exception ->
                    // Handle error
                    Log.e("Autocomplete", "Autocomplete prediction failed: ${exception.message}")
                }
        }
    }

    private fun createVoteDatabase() {
        if (places != null) {
            val groupID = GroupManager.currentGroup?.groupID
            val size = membersSize
            val addressList: ArrayList<String> = arrayListOf()
            for (place in places!!) {
                val latLng = place.latLng
                if (latLng != null && place.address!=null) {

                    Log.d("adress", place.address!!)
                    val latitude = latLng.latitude
                    val longitude = latLng.longitude
                    val sanitisedAddress = sanitizeAddress(place.address!!.toString())
                    addressList.add(sanitisedAddress)
                    Log.d("adress", sanitisedAddress)

                    mDbRef.child("Vote").child(groupID!!).child("Size").setValue(size)
                    val reference = mDbRef.child("Vote").child(groupID).child(sanitisedAddress)
                    reference.child("Name").setValue(place.name)
                    reference.child("Address").setValue(sanitisedAddress)
                    reference.child("userList").setValue(arrayListOf(""))
                    reference.child("Latitude").setValue(latitude)
                    reference.child("Longitude").setValue(longitude)
                }
            }
            mDbRef.child("Vote").child(groupID!!).child("AddressList").setValue(addressList.toSet().toList())
        }
    }
    private fun sanitizeAddress(address: String): String {
        val regex = Regex("[^A-Za-z0-9, ]")
        return regex.replace(address, "")
    }
//    fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String? {
//        val geocoder = Geocoder(context)
//        try {
//            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
//            if (addresses != null && addresses.isNotEmpty()) {
//                val address = addresses[0]
//                // Here you can extract the name or any other relevant information from the address
//                val name = address.featureName
//                // You can construct a more detailed address using other fields like locality, adminArea, etc.
//                return name
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return null
//    }

}