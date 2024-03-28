package com.example.getgoing

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object CurrentUserManager {


    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    private var currentUser: User? = null

    fun setCurrentUser(user: User?) {
        currentUser = user
    }

    fun getCurrentUser(): User? {
        return currentUser
    }
    fun getUserByPhone(phone: String, callback: (User?) -> Unit) {
        mDbRef.child("User").child(phone)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    callback(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }


    suspend fun getFriendList(user: String): ArrayList<String> {
        return DatabaseManager.fetchDataListFromFirebase(
            mDbRef.child("User").child(user).child("friends"), String::class.java
        )
    }

    suspend fun getGroupList(user: String): ArrayList<Group> {
        return DatabaseManager.fetchDataListFromFirebase(
            mDbRef.child("User").child(user).child("groups"), Group::class.java
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


}
