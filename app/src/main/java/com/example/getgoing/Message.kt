package com.example.getgoing

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Message {
    var message: String? = null
    var senderId: String? = null
    lateinit var time: String
    var vote: Boolean = false

    constructor(){}



    @RequiresApi(Build.VERSION_CODES.O)
    constructor(message: String?, senderId: String?){
        this.message = message
        this.senderId = senderId
        this.time = getCurrentTime()
        this.vote = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(message: String?, senderId: String?, vote: Boolean){
        this.message = message
        this.senderId = senderId
        this.time = getCurrentTime()
        this.vote = vote
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentTime(): String {
        val singaporeZoneId = ZoneId.of("Asia/Singapore")
        val currentTime = LocalTime.now(singaporeZoneId)

        // Format the current time to display only hour and minute
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return currentTime.format(formatter)
    }

}