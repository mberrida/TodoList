package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.todolistapp.presentation.navigation.Navigation
import com.example.todolistapp.ui.theme.TodoListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListAppTheme {
                val navController = rememberNavController()
                Navigation(navController) // ✅ Appelle bien la fonction @Composable
            }
        }
    }
}
