package com.example.todolistapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object UserDataSource {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun signUp(email: String, password: String): String? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            null
        }
    }

    suspend fun signIn(email: String, password: String): String? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            null
        }
    }
}

