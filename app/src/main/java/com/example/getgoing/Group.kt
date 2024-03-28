package com.example.getgoing

import java.util.UUID

class Group {
    var name: String? = null
    var groupID: String? = null
    var image: Int? = null
    var members: ArrayList<String>? = null
    var chat: ArrayList<Message>? = null

    constructor() {}

    constructor(
        name: String?,
        members: ArrayList<String>?,
        groupID: String?
    ) {
        this.name = name
        this.members = members
        this.groupID = groupID
        this.image = null
        this.chat = ArrayList<Message>()
    }

    fun addMember(friendPhone: String?) {
        if (members == null) {
            members = ArrayList()
        }
        friendPhone?.let {
            members?.add(it)
        }
    }
    fun setGroupName(name: String?){
        this.name = name
    }
    fun setGroupImage(image: Int?){
        // TODO:
    }

}