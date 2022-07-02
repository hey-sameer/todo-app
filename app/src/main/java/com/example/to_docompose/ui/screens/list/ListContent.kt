package com.example.to_docompose.ui.screens.list


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.ui.util.DotsCollision
import com.example.to_docompose.ui.util.ListAppBarState
import com.example.to_docompose.ui.util.RequestState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListContent(
    allTasks: RequestState<List<ToDoTask>>,
    searchedTasks: RequestState<List<ToDoTask>>,
    toTaskScreen: (Int) -> Unit,
    searchAppBarState: ListAppBarState,
    onSwipeToDismiss: (ToDoTask) -> Unit,
){
    if(searchAppBarState == ListAppBarState.TRIGGERED){
        if(searchedTasks is RequestState.Loading)
            LoadingList()
        else if(searchedTasks is RequestState.Success)
            DisplayContent(tasks = searchedTasks.data, toTaskScreen = toTaskScreen, onSwipeToDismiss)
    }
    else{
        if(allTasks is RequestState.Loading)
            LoadingList()
        else if (allTasks is RequestState.Success)
        DisplayContent(tasks = allTasks.data, toTaskScreen = toTaskScreen, onSwipeToDismiss)
    }
}

@Composable
fun DisplayContent(tasks: List<ToDoTask>, toTaskScreen: (Int) -> Unit, onSwipeToDismiss: (ToDoTask) -> Unit){
    if(tasks.isEmpty())
        EmptyContent()
    else
        DisplayTask(tasks = tasks, toTaskScreen = toTaskScreen, onSwipeToDismiss = onSwipeToDismiss)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayTask(tasks: List<ToDoTask>, toTaskScreen: (Int) -> Unit, onSwipeToDismiss: (ToDoTask) -> Unit){
    LazyColumn{
        items(
            items = tasks,
            key = {
                it.hashCode()
            }
        ){ task ->
            val scope = rememberCoroutineScope()
            var itemAppeared by remember { mutableStateOf(false) }
            val dismissedState = rememberDismissState(confirmStateChange = {
                if(it == DismissValue.DismissedToStart)
                {
                    scope.launch {
                        itemAppeared = false
                        delay(300)
                        onSwipeToDismiss(task)
                    }
                }
                true
            })

            val offsetX: Dp by animateDpAsState(if(dismissedState.targetValue == DismissValue.Default) (-70).dp else 0.dp)
            val dismissedBackground: Color by animateColorAsState(if(dismissedState.targetValue == DismissValue.Default) Color(0xFFFC5E03) else Color.Red)
            LaunchedEffect(key1 = true){
                itemAppeared = true
            }
            AnimatedVisibility(
                visible=  itemAppeared,
                enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300)),
            ) {
                SwipeToDismiss(
                    state = dismissedState,
                    background = { DismissedBackground(offsetX = offsetX, background = dismissedBackground) },
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {FractionalThreshold(.25f)},
                    dismissContent = {
                        ListItem(toDoTask = task, toTaskScreen = toTaskScreen)
                    }
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListItem(
    toDoTask: ToDoTask,
    toTaskScreen: (taskId: Int) -> Unit
){
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        elevation = 2.dp,
        onClick = { toTaskScreen(toDoTask.id) }) {
            Column(modifier = Modifier
                .padding(all = 12.dp)
                .fillMaxWidth()) {

                Row()
                {
                    Text(
                        text = toDoTask.title,
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        maxLines = 1,
                        modifier = Modifier.weight(9f)
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .weight(1f), contentAlignment = Alignment.TopEnd){
                        Canvas(modifier = Modifier.size(16.dp)){
                            drawCircle(color = toDoTask.priority.color)
                        }
                    }
                }
                toDoTask.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                    )
                }
            }
    }
}

@Composable
fun LoadingList(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        DotsCollision()
    }
}

@Composable
fun DismissedBackground(offsetX: Dp, background: Color){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = background),
        contentAlignment = Alignment.CenterEnd
    ){
        Icon(
            modifier = Modifier
                .padding(16.dp)
                .offset(x = offsetX),
            imageVector = Icons.Filled.Delete,
            tint = if(isSystemInDarkTheme()) Color.Black else Color.White,
            contentDescription = stringResource(R.string.delete_task),
        )
    }
}

@Composable
@Preview()
fun ListItemPreview(){
    ListItem(
        toDoTask = ToDoTask(
            title = "Long Long Again Long Sample Title",
            description = "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...",
            priority = Priority.HIGH
        ),
        toTaskScreen = {}
    )
}
