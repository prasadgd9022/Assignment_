package com.example.taskmanager.data

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromPriority(value: String): PriorityLevel {
        return PriorityLevel.valueOf(value)
    }

    @TypeConverter
    fun priorityToString(priority: PriorityLevel): String {
        return priority.name
    }
}