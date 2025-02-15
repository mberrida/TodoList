package com.example.todolistapp.utils

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignOutButton(navController: NavController) {
    Button(
        onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("SignInScreen") { popUpTo("HomeScreen") { inclusive = true } }
        }
    ) {
        Text("Déconnexion")
    }
}
