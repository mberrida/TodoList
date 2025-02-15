package com.example.todolistapp.presentation.screens.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.TaskDataSource
import com.example.todolistapp.domain.model.Task
import kotlinx.coroutines.launch
import java.util.UUID

data class AddEditTaskUiState(
    var taskName: String = "",
    var taskDescription: String = "",
    var taskDueDate: String = "No due date",
    var isTaskFinished: Boolean = false,
    var taskId: String = "",
    var userId: String = ""
)

class AddEditTaskViewModel : ViewModel() {

    var addEditTaskUiState by mutableStateOf(AddEditTaskUiState())
        private set

    fun updateName(newName: String) {
        addEditTaskUiState = addEditTaskUiState.copy(taskName = newName)
    }

    fun updateDescription(newDescription: String) {
        addEditTaskUiState = addEditTaskUiState.copy(taskDescription = newDescription)
    }

    fun updateDueDate(newDate: String) {
        addEditTaskUiState = addEditTaskUiState.copy(taskDueDate = newDate)
    }

    fun updateTaskId(taskId: String) {
        addEditTaskUiState = addEditTaskUiState.copy(taskId = taskId)
    }

    /**
     * 🔹 Sauvegarde une nouvelle tâche ou met à jour une tâche existante
     */
    suspend fun saveTask(userId: String): Boolean {
        return try {
            val task = Task(
                taskID = addEditTaskUiState.taskId.ifEmpty { UUID.randomUUID().toString() },
                taskName = addEditTaskUiState.taskName,
                taskDescription = addEditTaskUiState.taskDescription,
                taskDueDate = addEditTaskUiState.taskDueDate,
                taskIsFinished = addEditTaskUiState.isTaskFinished,
                userId = userId
            )
            TaskDataSource.saveTask(task)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }






    /**
     * 🔹 Récupère une tâche depuis Firestore et met à jour l'UI
     */
    fun getTask(taskId: String) {
        viewModelScope.launch {
            try {
                val task = TaskDataSource.getTask(taskId) // 🔹 Correction ici, récupération directe de la tâche
                task?.let {
                    addEditTaskUiState = addEditTaskUiState.copy(
                        taskName = it.taskName ?: "",
                        taskDescription = it.taskDescription ?: "",
                        taskDueDate = it.taskDueDate ?: "No due date",
                        taskId = it.taskID ?: "",
                        isTaskFinished = it.taskIsFinished ?: false,
                        userId = it.userId ?: ""
                    )
                }
            } catch (e: Exception) {
                // Gérer les erreurs
                e.printStackTrace()
            }
        }
    }

    /**
     * 🔹 Réinitialise l'état du formulaire
     */
    fun resetState() {
        addEditTaskUiState = AddEditTaskUiState()
    }
}
