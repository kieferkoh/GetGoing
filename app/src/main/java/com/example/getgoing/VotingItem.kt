package com.example.getgoing

class VotingItem {
    var name: String? = null
    var address: String? = null
    var userList: ArrayList<String>? = null
    constructor (){}
    constructor(
        name: String?,
        address: String?,
        userList: java.util.ArrayList<*>?
    ){
        this.name = name
        this.address = address
        this.userList = ArrayList()
    }

}
