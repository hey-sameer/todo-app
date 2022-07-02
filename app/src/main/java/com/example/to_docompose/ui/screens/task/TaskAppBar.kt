package com.example.to_docompose.ui.screens.task

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.R
import com.example.to_docompose.component.AlertDialogBox
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.ui.util.Action

@Composable
fun TaskAppBar(task: ToDoTask?, toListScreen: (Action) -> Unit) {
    when (task) {
        null -> NewTaskAppBar(toListScreen = toListScreen)

        else -> EditTaskAppBar(
            toListScreen = toListScreen,
            task = task
        )
    }
}

@Composable
fun NewTaskAppBar(toListScreen: (Action) -> Unit) {
    TopAppBar(
        navigationIcon = { BackNavigation(onBackClicked = toListScreen) },
        title = { Text(text = stringResource(R.string.new_task_title)) },
        actions = {
            IconButton(onClick = { toListScreen(Action.ADD) }) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
        }
    )
}

@Composable
fun EditTaskAppBar(toListScreen: (Action) -> Unit, task: ToDoTask) {
    TopAppBar(
        navigationIcon = { BackNavigation(onBackClicked = toListScreen) },
        title = {
            Text(
                text = task.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        actions = {
            EditAppBarActions(toListScreen = toListScreen, task = task)
        },
    )
}

@Composable
fun BackNavigation(onBackClicked: (Action) -> Unit) {
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_nav)
        )
    }
}

@Composable
fun DoneAction(onDoneClicked: () -> Unit) {
    IconButton(onClick = { onDoneClicked() }) {
        Icon(imageVector = Icons.Filled.Done, contentDescription = "Save Changes")
    }
}

@Composable
fun DeleteAction(onDeleteClicked: () -> Unit) {
    IconButton(onClick = { onDeleteClicked() }) {
        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Task")
    }
}

@Composable
fun EditAppBarActions(toListScreen: (Action) -> Unit, task: ToDoTask){

    var showDialog by remember {
        mutableStateOf(false)
    }
    if(showDialog)
    AlertDialogBox(
        title = "Remove",
        message = "Are you Sure?",
        onConfirm = { toListScreen(Action.DELETE)},
        onCancel = { showDialog = false},
        closeDialog = { showDialog = false },
    )
    DeleteAction { showDialog = true}
    DoneAction { toListScreen(Action.UPDATE) }
}

@Composable
@Preview()
fun NewTaskAppBarPreview() {
    NewTaskAppBar(toListScreen = {})
}

