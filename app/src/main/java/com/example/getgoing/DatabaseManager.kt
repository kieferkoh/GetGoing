package com.example.getgoing

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

object DatabaseManager {
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()

    suspend fun <T : Any> fetchDataListFromFirebase(dbRef: DatabaseReference, clazz: Class<T>): ArrayList<T> {
        val dataSnapshot = dbRef.get().await()
        val dataList: ArrayList<T> = ArrayList()

        for (snapshot in dataSnapshot.children) {
            val data = snapshot.getValue(clazz)
            data?.let { dataList.add(it) }
        }

        return dataList
    }

    suspend fun <T : Any> fetchDataFromFirebase(dbRef: DatabaseReference, clazz: Class<T>): T? {
        val dataSnapshot = dbRef.get().await()
        return dataSnapshot.getValue(clazz)
    }
}