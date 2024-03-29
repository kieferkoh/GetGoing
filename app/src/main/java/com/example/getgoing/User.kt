
package com.example.getgoing

class User {
    var name: String? = null
    var phone: String? = null
    var password: String? = null
    var image: Int? = null
    var friends: ArrayList<String>? = null
    var groups: ArrayList<String>? = null
    var friendreqs: ArrayList<String>? = null
    var location: String? = null

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
        this.location = " "
        this.groups = arrayListOf(" ")
        this.friendreqs = arrayListOf(" ")

    }

}