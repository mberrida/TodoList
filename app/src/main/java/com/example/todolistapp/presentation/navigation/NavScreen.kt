package com.example.todolistapp.presentation.navigation

sealed class NavScreen(val route: String) {
    object SignIn : NavScreen("signin")
    object SignUp : NavScreen("signup")
    object Home : NavScreen("home")
}