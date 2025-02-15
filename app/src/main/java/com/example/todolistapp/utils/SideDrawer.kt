package com.example.todolistapp.utils

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SideDrawer(navController: NavController) {
    ModalDrawerSheet {
        TextButton(onClick = { navController.navigate("HomeScreen") }) { Text("Accueil") }
        TextButton(onClick = { navController.navigate("TaskListScreen") }) { Text("Tâches") }
        SignOutButton(navController)
    }
}
