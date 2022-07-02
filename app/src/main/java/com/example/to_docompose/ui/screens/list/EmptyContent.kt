package com.example.to_docompose.ui.screens.list

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R

@Composable
@Preview
fun EmptyContent() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.empty_note),
                contentDescription = "Sad Face",
                modifier = Modifier.alpha(ContentAlpha.medium).size(128.dp)
            )
            Text(
                text = stringResource(R.string.empty_container_string),
                modifier = Modifier.alpha(ContentAlpha.medium),
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

