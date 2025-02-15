package com.example.todolistapp.presentation.screens.home

import android.annotation.SuppressLint
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
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss

@OptIn(ExperimentalMaterial3Api::class)
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
                    items(tasks, key = { it.taskID ?: UUID.randomUUID().toString() }) { task ->
                        SwipeTaskItem(
                            task = task,
                            onDelete = { taskListViewModel.deleteTask(it.taskID ?: "", userId) },
                            onEdit = { navController.navigate("AddEditTaskScreen/${it.taskID}") }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SwipeTaskItem(task: Task, onDelete: (Task) -> Unit, onEdit: (Task) -> Unit) {
    val dismissState = rememberDismissState()

    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
        onEdit(task)
    }
    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        onDelete(task)
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {
            val color = when {
                dismissState.dismissDirection == DismissDirection.StartToEnd -> Color.Blue
                dismissState.dismissDirection == DismissDirection.EndToStart -> Color.Red
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
            TaskItem(task)
        }
    )
}

@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.taskName ?: "",
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Due on ${task.taskDueDate ?: "No due date"}",
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
