package com.example.getgoing


import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await

object DatabaseManager {

//    suspend fun fetchUserFromFirebase(dbRef: DatabaseReference): User? {
//        return try {
//            val dataSnapshot = dbRef.get().await()
//            val userData = dataSnapshot.value as? HashMap<*, *>
//            if (userData != null && userData.isNotEmpty()) {
//                val name = userData["name"] as? String?
//                val phone = userData["phone"] as? String?
//                val password = userData["password"] as? String?
//                val groups = userData["groups"] as? HashMap<*, *>?
//                val friends = userData["friends"] as? HashMap<*, *>?
//
//                User(name, phone, password, groups, friends)
//            } else {
//                null
//            }
//        } catch (e: Exception) {
//            // Handle errors
//            null
//        }
//    }

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

    suspend fun <T : Any> createOrUpdateListFirebase(data: T?, dbRef: DatabaseReference, clazz: Class<T>): Boolean {
        var existingData : ArrayList<T> = fetchDataListFromFirebase(dbRef, clazz)
        if (existingData.contains(data)){
            return false
        }
        if (existingData[0] != " "){
            existingData.add(data!!)
        }
        else {
            existingData = data?.let { arrayListOf(it) }!!
        }
        dbRef.setValue(existingData).await()
        return true
    }

    suspend fun <T : Any> createDataFirebase(data: T?, dbRef: DatabaseReference): Boolean {
        dbRef.setValue(data)
        return true
    }


    suspend fun <T : Any> removeDataFromListFirebase(data: T?, dbRef: DatabaseReference, clazz: Class<T>): Boolean {
        var existingData : ArrayList<T> = fetchDataListFromFirebase(dbRef, clazz)
        if (!existingData.contains(data)){
            return false
        }
        if (existingData.all { it is String}){
            val stringDataList = existingData as ArrayList<String>
            val stringData = data as? String
            stringDataList.remove(stringData)
            if (stringDataList.isEmpty()){
                stringDataList.add(" ")
            }
            dbRef.setValue(stringDataList).await()
        }
        return true
    }
}