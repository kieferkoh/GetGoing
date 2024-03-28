package com.example.getgoing

import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await

object DatabaseManager {

    suspend fun <T : Any> fetchDataListFromFirebase(
        dbRef: DatabaseReference,
        clazz: Class<T>
    ): ArrayList<T> {
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

    suspend fun <T : Any> updateDataInFirebase(
        dbRef: DatabaseReference,
        clazz: Class<T>,
        updateFunction: (T) -> Unit
    )
            : Boolean {
        return try {
            val existingData: T? = fetchDataFromFirebase(dbRef, clazz)
            if (existingData != null) {
                updateFunction(existingData)
                dbRef.setValue(existingData).await()
                return true
            }
            true
        } catch (e: DatabaseException) {
            false
        }
    }

    suspend fun <T : Any> createDataInFirebase(
        dbRef: DatabaseReference,
        userId: String,
        data: T
    ): Boolean {
        try {
            dbRef.child(userId).setValue(data).await()
            return true
        } catch (e: Exception) {
            // Handle errors
        }
        return false
    }
}