package com.example.todolistapp.presentation.screens.task

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todolistapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    navController: NavController,
    taskId: String? = null,
    addEditTaskViewModel: AddEditTaskViewModel = viewModel(),
    taskListViewModel: TaskListViewModel = viewModel()
) {
    val taskState = addEditTaskViewModel.addEditTaskUiState
    var taskName by remember { mutableStateOf(taskState.taskName) }
    var taskDescription by remember { mutableStateOf(taskState.taskDescription) }
    var taskDueDate by remember { mutableStateOf(taskState.taskDueDate) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var taskSaved by remember { mutableStateOf(false) }

    LaunchedEffect(taskId) {
        taskId?.let {
            addEditTaskViewModel.getTask(it)
            val updatedTask = addEditTaskViewModel.addEditTaskUiState
            taskName = updatedTask.taskName
            taskDescription = updatedTask.taskDescription
            taskDueDate = updatedTask.taskDueDate
        }
    }

    LaunchedEffect(taskSaved) {
        if (taskSaved) {
            delay(500)
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task", fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color.Black)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color.Black,
                contentColor = Color.White,
                onClick = {
                    coroutineScope.launch {
                        val userId = "User123"
                        addEditTaskViewModel.updateName(taskName)
                        addEditTaskViewModel.updateDescription(taskDescription)
                        addEditTaskViewModel.updateDueDate(taskDueDate)

                        val success = addEditTaskViewModel.saveTask(userId)

                        if (success) {
                            taskListViewModel.loadUserTasks(userId)
                            taskSaved = true
                        } else {
                            snackbarHostState.showSnackbar("Error saving task")
                        }
                    }
                }

            ) {
                Icon(Icons.Filled.Check, contentDescription = "Save Task")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // ✅ Fond blanc
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = taskName,
                onValueChange = {
                    taskName = it
                    addEditTaskViewModel.updateName(it)
                },
                label = { Text("Task Name", color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )

            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = taskDescription,
                onValueChange = {
                    taskDescription = it
                    addEditTaskViewModel.updateDescription(it)
                },
                label = { Text("Task Description", color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                )

            )
            Spacer(modifier = Modifier.height(8.dp))

            DatePickerInput(taskDueDate) { newDate ->
                taskDueDate = newDate
                addEditTaskViewModel.updateDueDate(newDate)
            }
        }
    }
}

@Composable
fun DatePickerInput(
    taskDueDate: String,
    onTaskDueDateChange: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(taskDueDate) }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            val formattedDate = "$y-${(m + 1).toString().padStart(2, '0')}-${d.toString().padStart(2, '0')}"
            selectedDate = formattedDate
            onTaskDueDateChange(formattedDate)
        }, year, month, day
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Due on: $selectedDate", fontSize = 16.sp, color = Color.Black) // ✅ Date en noir
        Icon(
            painter = painterResource(id = R.drawable.calendar_icon),
            contentDescription = "Calendar Icon"
        )
    }
}
