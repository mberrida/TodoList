package com.example.todolistapp.presentation.screens.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolistapp.domain.model.Task
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

@Composable
fun TaskListScreen(navController: NavController, taskListViewModel: TaskListViewModel = viewModel()) {
    val tasks by taskListViewModel.tasks.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("AddEditTaskScreen") }) {
                Icon(Icons.Filled.Add, contentDescription = "Ajouter une tâche")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (tasks.isEmpty()) {
                Text("Aucune tâche disponible", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(tasks) { task ->
                        TaskItem(task = task, onClick = { navController.navigate("EditTask/${task.taskID}") })
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = task.taskName, style = MaterialTheme.typography.bodyLarge) // ✅ Corrigé
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Échéance : ${task.taskDueDate}") // ✅ Corrigé
        }
    }
}

