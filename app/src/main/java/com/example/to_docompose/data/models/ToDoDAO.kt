package com.example.to_docompose.data.models

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDAO {
    @Query("select * from task_table order by id")
    fun getAllTask(): Flow<List<ToDoTask>>

    @Query("select * from task_table where id = :id")
    fun getSelectedTask(id: Int): Flow<ToDoTask>

    @Update
    suspend fun updateTask(newTask: ToDoTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(toDoTask: ToDoTask)

    @Delete
    suspend fun deleteTask(todoTask: ToDoTask)

    @Query("delete from task_table")
    suspend fun deleteAllTask()

    @Query("select * from task_table where title like :searchQuery or description like :searchQuery")
    fun searchTasks(searchQuery: String): Flow<List<ToDoTask>>

    @Query("select * from task_table order by case when priority like 'L%' then 1 when priority like 'M%' then 2 when priority like 'H%' then 3 end")
    fun sortByLowPriority(): Flow<List<ToDoTask>>

    @Query("select * from task_table order by case when priority like 'H%' then 1 when priority like 'M%' then 2 when priority like 'L%' then 3 end")
    fun sortByHighPriority(): Flow<List<ToDoTask>>
}