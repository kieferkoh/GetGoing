package com.example.getgoing

import android.os.Build
import androidx.annotation.RequiresApi
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
        groupID: String?,
        image: Int?,
        members: ArrayList<String>?,
        chat: ArrayList<Message>?
    ) {
        this.name = name
        this.members = members
        this.groupID = groupID
        this.image = image
        this.chat = chat
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(
        name: String?,
        members: ArrayList<String>?,
        groupID: String?
    ) {
        this.name = name
        this.members = members
        this.groupID = groupID
        this.image = R.drawable.roy
        this.chat = arrayListOf(Message(" ","0"))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(
        groupID: String?,
        image: Int?,
        name: String?
    ){
        this.name = name
        this.members = arrayListOf(" ")
        this.groupID = groupID
        this.image = image
        this.chat = arrayListOf(Message(" ","0"))

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