package com.example.getgoing

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

object GroupManager {
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    var currentGroup: Group? = null
    var places: ArrayList<Place>? = null
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createGroup(name: String, members: ArrayList<String>) : Boolean {
        val gid = UUID.randomUUID().toString()
        val dbRefGrp = mDbRef.child("Groups").child(gid)
        currentGroup = Group(name, members, gid)
        DatabaseManager.createDataFirebase(Group(name, members, gid), dbRefGrp)
        DatabaseManager.createDataFirebase("", dbRefGrp.child("Activity"))
        members.forEach {
            val dbRefMember = mDbRef.child("User").child(it).child("groups")
            DatabaseManager.createOrUpdateListFirebase(gid, dbRefMember, String::class.java)
        }
        return true
    }


    suspend fun addMembers(gid: String, members: ArrayList<String>) : Boolean {
        val dbRefGrp = mDbRef.child("Groups").child(gid).child("members")
        members.forEach {
            val dbRefMember = mDbRef.child("User").child(it).child("groups")
            DatabaseManager.createOrUpdateListFirebase(it, dbRefGrp, String::class.java)
            DatabaseManager.createOrUpdateListFirebase(gid, dbRefMember, String::class.java)
        }
        return true
    }

    suspend fun delMembers(gid: String, members: ArrayList<String>) : Boolean {
        val dbRefGrp = mDbRef.child("Groups").child(gid).child("members")
        members.forEach {
            val dbRefMember = mDbRef.child("User").child(it).child("groups")
            DatabaseManager.removeDataFromListFirebase(it, dbRefGrp, String::class.java)
            DatabaseManager.removeDataFromListFirebase(gid, dbRefMember, String::class.java)
        }
        return true
    }

    suspend fun getGroups(gidList: ArrayList<String>) : ArrayList<Group> {
        val dbRefGrp = mDbRef.child("Groups")
        val groupList: ArrayList<Group> = arrayListOf()
        gidList.forEach {
            DatabaseManager.fetchDataFromFirebase(dbRefGrp.child(it), Group::class.java)
                ?.let { group -> groupList.add(group) }
        }
        return groupList
    }

    suspend fun setGroupDP(image: Int) {
        DatabaseManager.createDataFirebase(image, mDbRef.child("Groups").child(currentGroup?.groupID!!).child("image"))
        currentGroup?.image = image
    }
}