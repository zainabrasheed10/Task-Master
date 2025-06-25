package com.example.taskmaster

data class TaskTodo (
    val task: String,
    val description: String,
    var isCompleted: Boolean = false,
    val priority: String
)