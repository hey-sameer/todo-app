package com.example.to_docompose.ui.screens.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_docompose.component.PriorityDropDown
import com.example.to_docompose.data.models.Priority

@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String)-> Unit,
    description: String,
    onDescriptionChange: (String)-> Unit,
    priority: Priority,
    onPriorityChange: (Priority)->Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { onTitleChange(it) },
            textStyle = MaterialTheme.typography.body1,
            label = { Text(text = "Title(max 40 chars)")},
            singleLine = true,

        )
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = { onPriorityChange(it) }
        )
        OutlinedTextField(
            value = description,
            onValueChange = { onDescriptionChange(it) },
            modifier = Modifier.fillMaxSize(),
            label = { Text(text = "Description")},
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TaskContentPreview(){
    TaskContent(
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        priority = Priority.MEDIUM,
        onPriorityChange = {}
    )
}