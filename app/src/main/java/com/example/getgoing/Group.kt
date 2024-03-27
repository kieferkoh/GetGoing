package com.example.getgoing

class Group {
    var name: String? = null
    var groupID: String? = null
    var members : ArrayList<String>? = null
    var chat: ArrayList<Message>? = null

    constructor() {}

    constructor(
        name: String?,
        groupID: String?,
        members : ArrayList<String>?,
        chat: ArrayList<Message>?
    ) {
        this.name = name
        this.groupID = groupID
        this.members = members
        this.chat = chat
    }
}