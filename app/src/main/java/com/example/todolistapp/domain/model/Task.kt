package com.example.todolistapp.domain.model

data class Task(
    var taskID: String = "",
    var taskName: String = "",
    var taskDescription: String = "",
    var taskDueDate: String = "",
    var taskIsFinished: Boolean = false,
    var userId: String = ""
)
