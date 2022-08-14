package com.example.to_docompose.ui.screens.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R
import com.example.to_docompose.component.AlertDialogBox
import com.example.to_docompose.component.TaskPriorityItem
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.util.Action
import com.example.to_docompose.ui.util.ListAppBarState
import com.example.to_docompose.ui.viewmodels.SharedViewModel

const val TAG = "TAG"

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel
) = when (sharedViewModel.listAppBarState.value) {
    ListAppBarState.CLOSED -> {
        DefaultListAppBar(
            onSearchClick = { sharedViewModel.listAppBarState.value = ListAppBarState.OPENED },
            onSortedClicked = { selectedPriority -> sharedViewModel.changeSortPriority(selectedPriority) }
        ) {
            sharedViewModel.action.value = Action.DELETE_ALL
        }
    }
    else -> {
        SearchAppBar(
            text = sharedViewModel.listAppBarSearchQuery.value,
            onTextChanged = { newStr ->
                sharedViewModel.listAppBarSearchQuery.value = newStr
                sharedViewModel.searchTask(query = newStr)
            },
            onCloseClicked = {
                    if(sharedViewModel.listAppBarSearchQuery.value == "")
                        sharedViewModel.listAppBarState.value = ListAppBarState.CLOSED
                else{
                    sharedViewModel.listAppBarSearchQuery.value = ""
                }
            },
            onSearchClicked = {
                sharedViewModel.searchTask(query = it)
            }
        )
    }
}


@Composable
fun DefaultListAppBar(
    onSearchClick: () -> Unit,
    onSortedClicked: (Priority) -> Unit,
    onDeleteAllClick: () -> Unit
){
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.task))
        },
        actions = {
            ListAppBarAction(
                onSearchClick,
                onSortedClicked,
                onDeleteAllClick
            )
        },
        backgroundColor = Color(0xFF3700B3)
    )
}

@Composable
fun ListAppBarAction(
    onSearchClick: () -> kotlin.Unit,
    onSortedClicked: (Priority) -> Unit,
    onDeleteAllClick: () -> Unit
){
    var alertDialog by remember { mutableStateOf(false) }
    if(alertDialog)
    AlertDialogBox(
        title = stringResource(R.string.alert_dialog_delete_all),
        message = stringResource(R.string.alert_dialog_delete_all_msg),
        onCancel = { alertDialog = false },
        closeDialog = { alertDialog = false },
        onConfirm = {
            onDeleteAllClick()
            alertDialog = false
        },
    )
    SearchActionButton(onClick = onSearchClick)
    SortActionButton(onClick = onSortedClicked)
    DeleteAll { alertDialog = true }
}

@Composable
fun SearchActionButton(onClick: ()-> Unit){
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_task)
        )
    }
}

@Composable
fun SortActionButton(onClick: (Priority) -> Unit){
    var expanded by remember{ mutableStateOf(false)}
    IconButton(onClick = {expanded = true}) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(id = R.string.sort_task)
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(onClick = {
                expanded = false
                onClick(Priority.HIGH)
            }) {
                TaskPriorityItem(priority = Priority.HIGH)
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onClick(Priority.LOW)
            }) {
                TaskPriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onClick(Priority.NONE)
            }) {
                TaskPriorityItem(priority = Priority.NONE)
            }
        }
    }
}

@Composable
fun DeleteAll(onDeleteAllClick: ()-> Unit){
    var expanded by remember{ mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.List,
            contentDescription = stringResource(R.string.delete_all_task)
        )
    }
    DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
        DropdownMenuItem(onClick = { expanded = false; onDeleteAllClick() }) {
            Text(text = stringResource(R.string.delete_all_text))
        }

    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: ()->Unit,
    onSearchClicked: (String)->Unit
){
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember{ FocusRequester() }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        TextField(

            value = text,
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            leadingIcon = {
                IconButton(
                    onClick = { },
                    modifier = Modifier.alpha(ContentAlpha.disabled)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_task)
                    )
                }

            },
            placeholder ={ Text(
                text = stringResource(R.string.search_placeholder),
                modifier = Modifier.alpha(ContentAlpha.medium)
            )},
            trailingIcon = {
                IconButton(onClick = {onCloseClicked()},
                    modifier = Modifier.alpha(if(text == "") ContentAlpha.disabled else ContentAlpha.high)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(R.string.close_string)
                    )
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            )
        )
    }
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }
}

@Composable
@Preview
fun SearchAppBarPreview(){
    SearchAppBar("", {},{},{})
}
