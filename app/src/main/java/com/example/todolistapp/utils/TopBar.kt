package com.example.todolistapp.utils

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    TopAppBar( // ✅ Remplacement de `SmallTopAppBar`
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)

    )
}
