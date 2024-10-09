package com.example.taskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Date,
    val priority: PriorityLevel,
    val isCompleted: Boolean = false
)

enum class PriorityLevel {
    LOW,
    MEDIUM,
    HIGH
}