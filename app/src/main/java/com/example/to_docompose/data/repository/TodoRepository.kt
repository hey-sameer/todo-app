package com.example.to_docompose.data.repository

import com.example.to_docompose.data.models.ToDoDAO
import com.example.to_docompose.data.models.ToDoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TodoRepository @Inject constructor(private val todoDao: ToDoDAO) {

    val getAllTask: Flow<List<ToDoTask>> = todoDao.getAllTask()
    val getTaskSortedLowPriority: Flow<List<ToDoTask>> = todoDao.sortByLowPriority()
    val getTaskSortedHighPriority: Flow<List<ToDoTask>> = todoDao.sortByHighPriority()

    fun getTaskByID(taskID: Int): Flow<ToDoTask> = todoDao.getSelectedTask(taskID)

    suspend fun insertTask(task: ToDoTask) = todoDao.insertTask(task)

    suspend fun deleteTask(task: ToDoTask) = todoDao.deleteTask(task)

    suspend fun updateTask(task : ToDoTask) = todoDao.updateTask(task)

    suspend fun deleteAllTask() = todoDao.deleteAllTask()

    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>> = todoDao.searchTasks(searchQuery)

}