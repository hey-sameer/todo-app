package com.example.to_docompose.di

import android.content.Context
import androidx.room.Room
import com.example.to_docompose.data.models.TodoDatabase
import com.example.to_docompose.ui.util.CONSTANTS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    )  =
        Room.databaseBuilder(context,
        TodoDatabase::class.java,
        CONSTANTS.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideDAO(todoDatabase: TodoDatabase) = todoDatabase.toDoDAO
}