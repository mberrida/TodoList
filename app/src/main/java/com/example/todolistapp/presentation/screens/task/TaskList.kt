package com.example.todolistapp.presentation.screens.task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp

@Composable
fun TaskListScreen(
    navController: NavController,
    userId: String,  // ✅ Ajout de `userId` ici
    taskListViewModel: TaskListViewModel = viewModel()
) {
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
                        TaskItem(
                            task = task,
                            onTaskChecked = { isChecked ->
                                taskListViewModel.updateTaskCompletion(task.taskID ?: "", isChecked, userId) // ✅ Passer `userId`
                            },
                            onClick = { navController.navigate("EditTask/${task.taskID}") }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TaskItem(task: Task, onTaskChecked: (Boolean) -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // ✅ Espacement correct
            .clickable { onClick() }, // ✅ Clique pour modifier la tâche
        shape = RoundedCornerShape(16.dp), // ✅ Coins arrondis
        colors = CardDefaults.cardColors(containerColor = Color.White), // ✅ Fond blanc
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // ✅ Ombre plus marquée
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ Checkbox à gauche
            Checkbox(
                checked = task.taskIsFinished ?: false,
                onCheckedChange = { isChecked -> onTaskChecked(isChecked) },
                modifier = Modifier.padding(end = 12.dp)
            )

            // ✅ Texte de la tâche bien aligné
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.taskName ?: "No title",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Due on ${task.taskDueDate ?: "No due date"}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}







