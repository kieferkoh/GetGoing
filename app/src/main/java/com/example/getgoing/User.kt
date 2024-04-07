
package com.example.getgoing

import com.google.android.gms.maps.model.LatLng

class User {
    var name: String? = null
    var phone: String? = null
    var password: String? = null
    var image: Int? = null
    var friends: ArrayList<String>? = null
    var groups: ArrayList<String>? = null
    var friendreqs: ArrayList<String>? = null
    var location: DefaultLocation? = null
    var address: String? = null

    constructor() {}

    constructor(
        name: String?,
        phone: String?,
        password: String?
    ) {
        this.name = name
        this.phone = phone
        this.password = password
        this.image = R.drawable.roy
        this.friends = arrayListOf(" ")
        this.location = DefaultLocation()
        this.groups = arrayListOf(" ")
        this.friendreqs = arrayListOf(" ")
        this.address = "Default Location"

    }

}