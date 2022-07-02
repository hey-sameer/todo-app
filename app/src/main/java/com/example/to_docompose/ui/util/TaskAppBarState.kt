package com.example.to_docompose.ui.util

sealed class TaskAppBarState(){
    object NewTask: TaskAppBarState()
    object EditTask: TaskAppBarState()
}
