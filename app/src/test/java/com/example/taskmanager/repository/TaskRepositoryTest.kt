package com.example.taskmanager.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.taskmanager.data.Converters
import com.example.taskmanager.data.PriorityLevel
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskDao
import com.example.taskmanager.data.TaskDatabase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.*

import java.io.IOException
import java.util.Date

class TaskRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var taskDao: TaskDao
    private lateinit var db: TaskDatabase
    private lateinit var repository: TaskRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java)
            .allowMainThreadQueries()
            .addTypeConverter(Converters())
            .build()
        taskDao = db.taskDao()
        repository = TaskRepository(taskDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetTask() = runBlocking {
        val task = Task(
            title = "Test Task",
            description = "Description",
            dueDate = Date(),
            priority = PriorityLevel.HIGH
        )
        repository.insert(task)
        val allTasks = repository.allTasks.getOrAwaitValue()
        assertEquals(allTasks[0].title, task.title)
    }

    @Test
    fun updateTask() = runBlocking {
        val task = Task(
            title = "Test Task",
            description = "Description",
            dueDate = Date(),
            priority = PriorityLevel.MEDIUM
        )
        val id = repository.insert(task)
        val updatedTask = task.copy(id = id.toInt(), title = "Updated Task")
        repository.update(updatedTask)
        val allTasks = repository.allTasks.getOrAwaitValue()
        assertEquals(allTasks[0].title, "Updated Task")
    }

    @Test
    fun deleteTask() = runBlocking {
        val task = Task(
            title = "Test Task",
            description = "Description",
            dueDate = Date(),
            priority = PriorityLevel.LOW
        )
        repository.insert(task)
        repository.delete(task)
        val allTasks = repository.allTasks.getOrAwaitValue()
        assertTrue(allTasks.isEmpty())
    }
}