package com.example.getgoing

class User {
    var name: String? = null
    var phone: String? = null
    var password: String? = null
    var friends: ArrayList<String>? = null
    var friendreqs: ArrayList<String>? = null
    var groups : ArrayList<String>? = null
    var location: String? = null

    constructor() {}

    constructor(
        name: String?,
        phone: String?,
        password: String?,
        friends: ArrayList<String>?,
        friendreqs: ArrayList<String>?,
        location: String?
    ) {
        this.name = name
        this.phone = phone
        this.password = password
        this.friends = friends
        this.friendreqs = friendreqs
        this.location = location
        this.groups= null
    }
}