package com.example.todolistapp.presentation.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.TaskDataSource
import com.example.todolistapp.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {

    // 🔹 Stocker la liste des tâches sous forme de Flow
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    // 🔹 Charger les tâches depuis Firestore
    fun loadUserTasks(userId: String) {
        viewModelScope.launch {
            val userTasks = TaskDataSource.getTasks(userId)
            _tasks.value = userTasks
        }
    }

    // 🔹 Ajouter une nouvelle tâche
    fun addTask(task: Task) {
        viewModelScope.launch {
            TaskDataSource.saveTask(task)
            loadUserTasks(task.userId ?: "") // Recharger après ajout
        }
    }

    // 🔹 Modifier une tâche existante
    fun updateTask(task: Task) {
        viewModelScope.launch {
            TaskDataSource.saveTask(task)
            loadUserTasks(task.userId ?: "") // Recharger après mise à jour
        }
    }

    // 🔹 Supprimer une tâche
    fun deleteTask(taskId: String, userId: String) {
        viewModelScope.launch {
            TaskDataSource.deleteTask(taskId)
            loadUserTasks(userId) // Recharger après suppression
        }
    }


    fun updateTaskCompletion(taskId: String, isFinished: Boolean) {
        viewModelScope.launch {
            try {
                val task = tasks.value.find { it.taskID == taskId }
                if (task != null) {
                    val updatedTask = task.copy(taskIsFinished = isFinished)
                    TaskDataSource.saveTask(updatedTask) // 🔹 Met à jour Firestore ou ta source de données
                    loadUserTasks(task.userId ?: "") // 🔹 Recharge les tâches après la modification
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

