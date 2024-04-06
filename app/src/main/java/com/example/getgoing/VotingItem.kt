package com.example.getgoing

import com.google.android.libraries.places.api.Places

class VotingItem {
    var name: String? = null
    var location: Places? = null
    var userList: ArrayList<String>? = null
    constructor (){}
    constructor(
        name: String?,
        location: Places?,
    ){
        this.name = name
        this.location = location
        this.userList = ArrayList()
    }

}
