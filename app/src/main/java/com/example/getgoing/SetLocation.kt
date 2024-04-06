package com.example.getgoing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.getgoing.databinding.ActivitySetLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.io.IOException


class SetLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySetLocationBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var searchView: SearchView
    private lateinit var locationCoordinates: LatLng
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDbRef = FirebaseDatabase.getInstance().getReference()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding = ActivitySetLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment



        //Set Location Button
        var addressListOut: List<Address>? = null

        findViewById<Button>(R.id.setLocationButton).setOnClickListener {
            setLocation(addressListOut)
            val intent = Intent(this, ChatActivity::class.java)
            finish()
            startActivity(intent)

        }

        //Get Location Button

        findViewById<Button>(R.id.getLocationButton).setOnClickListener {
            getCurrentLocation()
        }

        //Back Button

        findViewById<ImageButton>(R.id.backToChatPage).setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            finish()
            startActivity(intent)
        }

        // Initialize the SearchView
        searchView = findViewById(R.id.searchView)
        // Set query hint
        searchView.queryHint = "Search location"
        // Set SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location: String = searchView.query.toString()
                var addressList: List<Address>? = null

                if (location != null) {
                    val geocoder = Geocoder(this@SetLocation)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    if (addressList != null) {
                        val address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        addressListOut=addressList
                        // Handle the obtained LatLng
                        removeAllMarkers()

                        mMap.addMarker(MarkerOptions().position(latLng).title("User Location"))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0F))
                    } else {
                        Toast.makeText(this@SetLocation, "Location not found.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // enable zooming
        mMap.uiSettings.isZoomControlsEnabled = true
        if (CurrentUserManager.currentUser != null) {
            mDbRef.child("User").child(CurrentUserManager.currentUser?.phone!!).child("location")
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val longitude = snapshot.child("longitude").getValue(Double::class.java)
                        val latitude = snapshot.child("latitude").getValue(Double::class.java)
                        val singapore = LatLng(latitude!!, longitude!!)
                        mMap.addMarker(MarkerOptions().position(singapore).title("Marker in Singapore"))
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

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations, this can be null.
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(currentLatLng)
                            .title("Current Location")
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                } else {
                    Toast.makeText(
                        this,
                        "Unable to retrieve current location.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setLocation(addressListOut: List<Address>?) {
        if (addressListOut != null && addressListOut.isNotEmpty()) {
            val latestAddress = addressListOut.last()
            val latitude = latestAddress.latitude
            val longitude = latestAddress.longitude
            if (CurrentUserManager.currentUser != null) {
                val currentUserRef = mDbRef.child("User").child(CurrentUserManager.currentUser?.phone!!).child("location")
                currentUserRef.child("longitude").setValue(longitude)
                currentUserRef.child("latitude").setValue(latitude)
        }
            else{
                Toast.makeText(this@SetLocation,"No address specified",Toast.LENGTH_SHORT).show()
            }

    }}

    // Function to remove all markers from the map
    private fun removeAllMarkers() {
        mMap.clear() // This clears all markers, polylines, and other overlays from the map.
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

}