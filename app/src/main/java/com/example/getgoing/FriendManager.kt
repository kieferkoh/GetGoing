package com.example.getgoing

import android.provider.ContactsContract.Data
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FriendManager {
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()

    suspend fun addFriend(phone: String, currentUser: User) : Boolean {
        val friend = CurrentUserManager.getUserByPhone(phone) ?: return false
        val dbRef = currentUser.phone?.let { mDbRef.child("User").child(it).child("friends") }
        DatabaseManager.createOrUpdateListFirebase(phone, dbRef!!, String::class.java)
        return true
    }

    suspend fun removeFriend(phone: String, currentUser: User) : Boolean {
        val friend = CurrentUserManager.getUserByPhone(phone) ?: return false
        val dbRef = currentUser.phone?.let { mDbRef.child("User").child(it).child("friends") }
        DatabaseManager.removeDataFromListFirebase(phone, dbRef!!, String::class.java)
        return true
    }

    suspend fun sendFriendReq(phone: String, currentUser: User) : Boolean {
        val friend = CurrentUserManager.getUserByPhone(phone) ?: return false
        val dbRef = mDbRef.child("User").child(phone).child("friendreqs")
        DatabaseManager.createOrUpdateListFirebase(currentUser.phone, dbRef, String::class.java)
        return true
    }

    suspend fun removeFriendReq(phone: String, currentUser: User) : Boolean {
        val friend = CurrentUserManager.getUserByPhone(phone) ?: return false
        val dbRef = currentUser.phone?.let { mDbRef.child("User").child(it).child("friendreqs") }
        DatabaseManager.removeDataFromListFirebase(phone, dbRef!!, String::class.java)
        return true
    }

    suspend fun getFriends(currentUser: User) : ArrayList<User> {
        val friendList: ArrayList<User> = arrayListOf()
        val dbRef = mDbRef.child("User").child(currentUser.phone!!).child("friends")
        DatabaseManager.fetchDataListFromFirebase(dbRef, String::class.java)
            .forEach {
            CurrentUserManager.getUserByPhone(it)?.let { friend -> friendList.add(friend) }
        }
        return friendList
    }

    suspend fun getFriendsReq(currentUser: User) : ArrayList<User> {
        val friendrqList: ArrayList<User> = arrayListOf()
        val dbRef = mDbRef.child("User").child(currentUser.phone!!).child("friendreqs")
        DatabaseManager.fetchDataListFromFirebase(dbRef, String::class.java)
            .forEach {
                CurrentUserManager.getUserByPhone(it)?.let { friend -> friendrqList.add(friend) }
            }
        return friendrqList
    }


}