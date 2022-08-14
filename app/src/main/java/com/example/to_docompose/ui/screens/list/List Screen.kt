package com.example.to_docompose.ui.screens.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_docompose.R
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.util.Action
import com.example.to_docompose.ui.util.RequestState
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListScreen(
    onTaskClick: (Int) -> Unit = {},
    sharedViewModel: SharedViewModel
) {
    //called everytime list screen opens to have updated task

    LaunchedEffect(key1 = true){
        sharedViewModel.readSortState()
    }
    val scaffoldState = rememberScaffoldState()
    val action by sharedViewModel.action
    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseAction = { sharedViewModel.handleDatabaseActions(action) },
        onUndoClicked = {
            sharedViewModel.handleDatabaseActions(Action.UNDO)
        },
        taskTitle = sharedViewModel.title.value,
        action = action
    )



    val allTask by sharedViewModel.allTask.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val searchedTask by sharedViewModel.searchedTasks.collectAsState()
    val listAppBarState by sharedViewModel.listAppBarState

    if(sortState is RequestState.Success)
    sharedViewModel.viewByPriority((sortState as RequestState.Success<Priority>).data)

    Scaffold(
        scaffoldState = scaffoldState,
        content = @Composable {
            ListContent(allTasks = allTask, toTaskScreen = onTaskClick, searchedTasks = searchedTask, searchAppBarState = listAppBarState) {
                sharedViewModel.updateFieldsWithCurrentSelectedTask(it)
                sharedViewModel.action.value = Action.DELETE
            }
        },
        floatingActionButton = { ListScreenFAB(
            onFabClicked = {
                sharedViewModel.updateFieldsWithCurrentSelectedTask(null)
                onTaskClick(-1)
            },
        ) },
        topBar = {
            ListAppBar(sharedViewModel = sharedViewModel)
        }

    )
}

@Composable
@Preview
fun ListScreenFAB(onFabClicked: () -> Unit = {}){
    FloatingActionButton(onClick = {onFabClicked()},
        backgroundColor = Color(0xFF3700B3)
        ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White,
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseAction: () -> Unit,
    onUndoClicked: () -> Unit,
    taskTitle: String,
    action: Action
){
    handleDatabaseAction()

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action){
        if(action != Action.NO_ACTION){
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = "${snackBarMessage(action)} $taskTitle",
                    actionLabel = snackBarLabel(action),
                )
                undoDeletedTask(action, snackBarResult, onUndoClicked)
            }
        }
    }
}

private fun snackBarLabel(action:Action): String = if(action == Action.DELETE) "Undo" else "OK"
private fun snackBarMessage(action: Action): String = when(action){
    Action.ADD -> "Added:"
    Action.UPDATE -> "Updated:"
    Action.DELETE -> "Deleted:"
    Action.DELETE_ALL -> "All tasks removed"
    Action.UNDO -> "Undo:"
    Action.NO_ACTION -> ""
}
private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: () -> Unit
){
    if(snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE)
        onUndoClicked()
}
