package com.example.todolistapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object UserDataSource {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    suspend fun signUp(email: String, password: String, username: String): String? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid

            if (userId != null) {
                val user = hashMapOf(
                    "email" to email,
                    "username" to username
                )

                usersCollection.document(userId).set(user).await()
            }
            userId
        } catch (e: Exception) {
            null
        }
    }


    suspend fun signIn(email: String, password: String): Pair<String?, String?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid

            if (userId != null) {
                val doc = usersCollection.document(userId).get().await()
                val username = doc.getString("username") ?: "User"
                return Pair(userId, username)
            }
            Pair(userId, null)
        } catch (e: Exception) {
            Pair(null, null)
        }
    }

}

