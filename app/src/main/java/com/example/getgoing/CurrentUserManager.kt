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

    fun getFriendList(phone: String): DatabaseReference {

        return (mDbRef.child("User").child(phone).child("friends"))
    }

    fun addFriend(phone: String){

    }

    fun getGroupList(phone: String): DatabaseReference {
        return (mDbRef.child("User").child(phone).child("groups"))
    }

    fun getName(phone: String): String {
        return (mDbRef.child("User").child(phone).child("name").toString())
    }

}
