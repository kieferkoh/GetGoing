package com.example.getgoing

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object CurrentUserManager {


    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    var currentUser: User? = null


    suspend fun getFriendReqs(user: String): ArrayList<String> {
        return DatabaseManager.fetchDataListFromFirebase(
            mDbRef.child("User").child(user).child("friendreqs"), String::class.java
        )
    }
    suspend fun getFriendList(user: String): ArrayList<String> {
        return DatabaseManager.fetchDataListFromFirebase(
            mDbRef.child("User").child(user).child("friends"), String::class.java
        )
    }

    suspend fun getGroupList(user: String): ArrayList<String> {
        return DatabaseManager.fetchDataListFromFirebase(
            mDbRef.child("User").child(user).child("groups"), String::class.java
        )
    }

    suspend fun getUserByPhone(phone: String): User? {
        return DatabaseManager.fetchDataFromFirebase(
            mDbRef.child("User").child(phone), User::class.java
        )
    }
    suspend fun getName(phone: String): String? {
        return getUserByPhone(phone)?.name
    }

    suspend fun addFriend(phone: String) : Boolean {
        val userAdded = currentUser?.let { FriendManager.addFriend(phone, it) }
        val removeReq = currentUser?.let { FriendManager.removeFriendReq(phone, it)}
        val friendAdded = getUserByPhone(phone)?.let { FriendManager.addFriend(currentUser?.phone!!, it)}
        return (userAdded == true && removeReq == true && friendAdded == true)
    }

    suspend fun sendFriendReq(phone: String) : Boolean? {
        if (phone == currentUser?.phone!!){
            return false // Cannot add ownself
        }
        if (getFriendList(currentUser?.phone!!).contains(phone)){
            return false // If already friend
        }
        if (getFriendReqs(currentUser?.phone!!).contains(phone)){
            return addFriend(phone) // If friend has already sent friend request to current user
        }
        return currentUser?.let { FriendManager.sendFriendReq(phone, it) }
    }

    suspend fun rejectFriendReq(phone: String) : Boolean? {
        return currentUser?.let { FriendManager.removeFriendReq(phone, it)}
    }

    suspend fun removeFriend(phone: String) : Boolean {
        val userRemove = currentUser?.let { FriendManager.removeFriend(phone, it)}
        val friendRemove = getUserByPhone(phone)?.let { FriendManager.removeFriend(currentUser?.phone!!, it)}
        return (userRemove == true && friendRemove == true)
    }

    suspend fun createGroup(name: String, members: ArrayList<String>) : Boolean {
        members.add(currentUser?.phone!!)
        return GroupManager.createGroup(name, members)
    }


}
