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
import android.util.Log
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.io.IOException
import java.util.Locale


class SetLocation : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivitySetLocationBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var searchView: SearchView
    private lateinit var locationCoordinates: LatLng
    private lateinit var mDbRef: DatabaseReference
    private lateinit var placesClient: PlacesClient
    var addressListOut: LatLng? = null
    var place: Place? = null
    var placeNameOut: String? = null


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

        //
        val whereFrom: String? = intent.getStringExtra("From")

        //Set Location Button

        findViewById<Button>(R.id.setLocationButton).setOnClickListener {

            val intent: Intent
            if (whereFrom == "chat") {
                intent = Intent(this, ChatActivity::class.java)
            } else {
                intent = Intent(this, ProfilePage::class.java)
                intent.putExtra("placeName",placeNameOut)
                Log.d("placename",placeNameOut!!)
            }
            setLocation(place)
            finish()
            startActivity(intent)

        }

        //Get Location Button

        findViewById<Button>(R.id.getLocationButton).setOnClickListener {
            getCurrentLocation()
        }

        //Back Button

        findViewById<ImageButton>(R.id.backToChatPage).setOnClickListener {
            val intent: Intent
            if (whereFrom == "chat") {
                intent = Intent(this, ChatActivity::class.java)
            } else {
                intent = Intent(this, ProfilePage::class.java)
            }
            finish()
            startActivity(intent)
        }

        Places.initialize(applicationContext, "AIzaSyCBtPj2MvYWZHkyL5BSu2OCMtAujsJZszI")
        placesClient = Places.createClient(this)

        // Initialize the SearchView
        searchView = findViewById(R.id.searchView)
        // Set query hint
        searchView.queryHint = "Search location"
        // Set SearchView listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchLocation(query) }
                return true
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

    private fun setLocation(selectedPlace: Place?) {
        if (selectedPlace != null && selectedPlace.latLng != null) {
            val latitude = selectedPlace.latLng?.latitude
            val longitude = selectedPlace.latLng?.longitude
            if (CurrentUserManager.currentUser != null && latitude != null && longitude != null) {
                val currentUserRef =
                    mDbRef.child("User").child(CurrentUserManager.currentUser?.phone!!)
                        .child("location")
                currentUserRef.child("longitude").setValue(longitude)
                currentUserRef.child("latitude").setValue(latitude)
            } else {
                Toast.makeText(this@SetLocation, "No address specified", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@SetLocation, "Selected place is null", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to remove all markers from the map
    private fun removeAllMarkers() {
        mMap.clear() // This clears all markers, polylines, and other overlays from the map.
    }
    private fun searchLocation(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setCountries(arrayListOf("SG"))
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                for (prediction in response.autocompletePredictions) {
                    val placeId = prediction.placeId
                    val placeName = prediction.getPrimaryText(null).toString()
                    placeNameOut = placeName

                    val placeRequest = FetchPlaceRequest.newInstance(placeId, listOf(Place.Field.LAT_LNG))
                    placesClient.fetchPlace(placeRequest)
                        .addOnSuccessListener { response ->
                            val searchedPlace = response.place
                            val latLng = searchedPlace.latLng
                            addMarker(latLng!!, placeName)
                            // Update the global variable addressListOut with the obtained LatLng
                            addressListOut = latLng
                            place = searchedPlace

                        }
                        .addOnFailureListener { exception ->
                            // Handle failure
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }

    private fun addMarker(latLng: LatLng, title: String) {
        removeAllMarkers() // Remove existing markers
        mMap.addMarker(MarkerOptions().position(latLng).title(title))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

}