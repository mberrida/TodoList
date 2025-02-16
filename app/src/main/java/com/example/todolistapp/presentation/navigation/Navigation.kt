package com.example.todolistapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolistapp.presentation.screens.auth.SignInScreen
import com.example.todolistapp.presentation.screens.auth.SignUpScreen
import com.example.todolistapp.presentation.screens.home.HomeScreen
import com.example.todolistapp.presentation.screens.splash.SplashScreen
import com.example.todolistapp.presentation.screens.task.AddEditTaskScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = "SplashScreen") {
        composable("SplashScreen") { SplashScreen(navController) }
        composable("SignInScreen") { SignInScreen(navController) }
        composable("SignUpScreen") { SignUpScreen(navController) }
        composable("HomeScreen") { HomeScreen(navController) }

        composable("AddEditTaskScreen") {
            AddEditTaskScreen(navController)
        }

        composable("AddEditTaskScreen/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            AddEditTaskScreen(navController, taskId)
        }
    }
}
