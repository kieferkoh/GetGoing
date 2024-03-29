package com.example.getgoing

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

object GroupManager {
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    var currentGroup: Group? = null
    suspend fun createGroup(name: String, members: ArrayList<String>) : Boolean {
        val gid = UUID.randomUUID().toString()
        val dbRefGrp = mDbRef.child("Groups").child(gid)
        DatabaseManager.createDataFirebase(Group(name, members, gid), dbRefGrp)
        members.forEach {
            val dbRefMember = mDbRef.child("User").child(it).child("groups")
            DatabaseManager.createOrUpdateListFirebase(gid, dbRefMember, String::class.java)
        }
        return true
    }
    suspend fun addMembers(gid: String, members: ArrayList<String>) : Boolean {
        val dbRefGrp = mDbRef.child("Groups").child(gid).child("members")
        members.forEach {
            DatabaseManager.createOrUpdateListFirebase(it, dbRefGrp, String::class.java)
        }
        return true
    }

    suspend fun delMembers(gid: String, members: ArrayList<String>) : Boolean {
        val dbRefGrp = mDbRef.child("Groups").child(gid).child("members")
        members.forEach {
            DatabaseManager.removeDataFromListFirebase(it, dbRefGrp, String::class.java)
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
}