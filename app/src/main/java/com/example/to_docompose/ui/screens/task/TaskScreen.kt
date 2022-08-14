package com.example.to_docompose.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.ui.screens.list.TAG
import com.example.to_docompose.ui.util.Action
import com.example.to_docompose.ui.viewmodels.SharedViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TaskScreen(task: ToDoTask?, toListScreen: (Action) -> Unit, sharedViewModel: SharedViewModel){

    val title by sharedViewModel.title
    val desc by sharedViewModel.description
    val priority by sharedViewModel.priority
    val context = LocalContext.current
    BackHandler(enabled = true) {
        toListScreen(Action.NO_ACTION)
    }
    Scaffold(topBar = {
        TaskAppBar(
            task = task,
            toListScreen = { action ->
                if (action == Action.NO_ACTION) toListScreen(action)
                else {
                    if (sharedViewModel.validateFields())
                        toListScreen(action)
                    else
                        displayToast(context)
                }
            }
        )
    }, content = {
        TaskContent(
            title = title,
            onTitleChange = {
                sharedViewModel.updateTitle(it)
            },
            description = desc ?: "",
            onDescriptionChange = {
                sharedViewModel.description.value = it
            },
            priority = priority,
            onPriorityChange = {
                sharedViewModel.priority.value = it
            }
        )
    })
}

fun displayToast(context: Context) {
    Toast.makeText(context,"Title can't be empty", Toast.LENGTH_LONG).show()
}
