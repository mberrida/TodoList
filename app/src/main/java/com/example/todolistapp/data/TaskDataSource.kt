package com.example.todolistapp.data

import com.example.todolistapp.domain.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID


object TaskDataSource {
    private val db = FirebaseFirestore.getInstance().collection("tasks")

    /**
     * 🔹 Ajouter ou mettre à jour une tâche dans Firestore
     */
    suspend fun saveTask(task: Task) {
        try {
            db.document(task.taskID).set(task).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    /**
     * 🔹 Récupérer une tâche spécifique par ID
     */
    suspend fun getTask(taskId: String): Task? {
        return try {
            val document = db.document(taskId).get().await()
            document.toObject(Task::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 🔹 Supprimer une tâche par ID
     */
    suspend fun deleteTask(taskId: String) {
        try {
            db.document(taskId).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 🔹 Récupérer toutes les tâches d'un utilisateur spécifique
     */
    suspend fun getTasks(userId: String): List<Task> {
        return try {
            db.whereEqualTo("userId", userId).get().await().toObjects(Task::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
