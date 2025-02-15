package com.example.todolistapp.presentation.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolistapp.domain.model.Task
import com.example.todolistapp.presentation.screens.task.TaskListViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberDismissState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SwipeToDismiss

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    taskListViewModel: TaskListViewModel = viewModel()
) {
    val tasks by taskListViewModel.tasks.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val userId = "User123"

    LaunchedEffect(Unit) {
        taskListViewModel.loadUserTasks(userId)
    }

    Scaffold(
        topBar = { HomeTopBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("AddEditTaskScreen") },
                containerColor = Color.Black
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Ajouter une tâche", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Hello,\nUser!",
                fontSize = 26.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (tasks.isEmpty()) {
                Text(
                    text = "No tasks available",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn {
                    items(tasks, key = { it.taskID ?: "" }) { task ->
                        SwipeTaskItem(
                            task = task,
                            onDelete = { taskListViewModel.deleteTask(task.taskID ?: "", userId) },
                            onEdit = { task.taskID.let { navController.navigate("AddEditTaskScreen/$it") } },
                            navController = navController // ✅ Ajout du paramètre manquant
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SwipeTaskItem(task: Task, onDelete: (Task) -> Unit, onEdit: (Task) -> Unit, navController: NavController) {
    val dismissState = rememberDismissState()

    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
        Log.d("Navigation", "Task ID envoyé: ${task.taskID}") // ✅ Vérification de l'ID
        navController.navigate("AddEditTaskScreen/${task.taskID}")
    }
    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        onDelete(task)
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.StartToEnd -> Color.Blue // Modifier
                DismissDirection.EndToStart -> Color.Red // Supprimer
                else -> Color.Gray
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(16.dp),
                contentAlignment = if (dismissState.dismissDirection == DismissDirection.EndToStart) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                if (dismissState.dismissDirection == DismissDirection.EndToStart) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.White)
                } else {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White)
                }
            }
        },
        dismissContent = {
            TaskItem(
                task = task,
                onTaskChecked = { isChecked -> /* Ajoute ici la fonction pour cocher/décocher */ },
                onClick = { onEdit(task) } // ✅ Ajoute l'action pour ouvrir l'édition
            )
        }

    )
}


@Composable
fun TaskItem(task: Task, onTaskChecked: (Boolean) -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.taskIsFinished ?: false,
                onCheckedChange = { isChecked -> onTaskChecked(isChecked) },
                modifier = Modifier.padding(end = 12.dp)
            )

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    CenterAlignedTopAppBar(
        title = { Text("HOME") },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
    )
}
