package com.example.todolistapp.presentation.screens.task

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.util.Log
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
    val taskState by addEditTaskViewModel.addEditTaskUiState.collectAsState()

    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskDueDate by remember { mutableStateOf("No due date") }

    val updatedTaskDueDate by rememberUpdatedState(newValue = taskState.taskDueDate)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // 🔹 Récupération de la tâche
    LaunchedEffect(taskId) {
        taskId?.let {
            Log.d("Navigation", "🔍 Chargement de la tâche ID: $it")
            addEditTaskViewModel.getTask(it)
        }
    }

    // 🔹 Mise à jour des champs après récupération de la tâche
    LaunchedEffect(taskState) {
        taskName = taskState.taskName
        taskDescription = taskState.taskDescription
        taskDueDate = taskState.taskDueDate
        Log.d("Navigation", "✅ Tâche chargée: Nom=$taskName, Date=$taskDueDate")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task", fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        Log.d("Navigation", "🔙 Icône Fermer cliqué, retour à HomeScreen")
                        coroutineScope.launch {
                            delay(200) // ✅ Évite les conflits de navigation
                            navController.navigate("HomeScreen") {
                                popUpTo("HomeScreen") { inclusive = true } // ✅ Ferme TOUTES les pages précédentes
                            }
                        }
                    }) {
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
                            Log.d("Navigation", "✅ Tâche enregistrée avec succès, retour à HomeScreen")
                            delay(200)
                            if (!navController.popBackStack()) {
                                navController.navigate("HomeScreen") { popUpTo("HomeScreen") { inclusive = true } }
                            }
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
                .background(Color.White)
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            DatePickerInput(updatedTaskDueDate) { newDate ->
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

    LaunchedEffect(taskDueDate) { // ✅ Mise à jour correcte de la date
        selectedDate = taskDueDate
    }

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
        Text(text = "Due on: $selectedDate", fontSize = 16.sp, color = Color.Black)
        Icon(
            painter = painterResource(id = R.drawable.calendar_icon),
            contentDescription = "Calendar Icon"
        )
    }
}
