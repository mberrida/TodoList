package com.example.todolistapp.presentation.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.TaskDataSource
import com.example.todolistapp.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _addEditTaskUiState = MutableStateFlow(AddEditTaskUiState())
    val addEditTaskUiState: StateFlow<AddEditTaskUiState> = _addEditTaskUiState

    fun updateName(newName: String) {
        _addEditTaskUiState.value = _addEditTaskUiState.value.copy(taskName = newName)
    }

    fun updateDescription(newDescription: String) {
        _addEditTaskUiState.value = _addEditTaskUiState.value.copy(taskDescription = newDescription)
    }

    fun updateDueDate(newDate: String) {
        _addEditTaskUiState.value = _addEditTaskUiState.value.copy(taskDueDate = newDate)
    }

    fun updateTaskId(taskId: String) {
        _addEditTaskUiState.value = _addEditTaskUiState.value.copy(taskId = taskId)
    }

    /**
     * 🔹 Sauvegarde une nouvelle tâche ou met à jour une tâche existante
     */
    suspend fun saveTask(userId: String): Boolean {
        return try {
            val task = Task(
                taskID = _addEditTaskUiState.value.taskId.ifEmpty { UUID.randomUUID().toString() },
                taskName = _addEditTaskUiState.value.taskName,
                taskDescription = _addEditTaskUiState.value.taskDescription,
                taskDueDate = _addEditTaskUiState.value.taskDueDate,
                taskIsFinished = _addEditTaskUiState.value.isTaskFinished,
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
                val task = TaskDataSource.getTask(taskId)
                task?.let {
                    _addEditTaskUiState.value = _addEditTaskUiState.value.copy(
                        taskName = it.taskName ?: "",
                        taskDescription = it.taskDescription ?: "",
                        taskDueDate = it.taskDueDate ?: "No due date",
                        taskId = it.taskID ?: "",
                        isTaskFinished = it.taskIsFinished ?: false,
                        userId = it.userId ?: ""
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 🔹 Réinitialise l'état du formulaire
     */
    fun resetState() {
        _addEditTaskUiState.value = AddEditTaskUiState()
    }
}
