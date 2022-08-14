package com.example.to_docompose.ui.screens.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.to_docompose.R
import com.example.to_docompose.ui.theme.splashScreenBackground
import kotlinx.coroutines.delay

@Composable
@Preview
fun SplashScreen(toNextScreen: () -> Unit = {}) {
    var animationStarted by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        animationStarted = true
        delay(2000)
        toNextScreen()
    }

    val alpha by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0f,
        animationSpec = tween(1000)
    )
    val enterOffset by animateDpAsState(
        targetValue = if (animationStarted) 0.dp else 100.dp,
        animationSpec = tween(1000)
    )

    Surface() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.splashScreenBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = getPainterRes()),
                    tint = Color.Unspecified,
                    contentDescription = "",
                    modifier = Modifier
                        .size(120.dp)
                        .alpha(alpha)
                        .offset(y = enterOffset),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Get Productive",
                    color = Color.White,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.alpha(alpha),
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Composable
fun getPainterRes(): Int {
    return if (isSystemInDarkTheme())
        R.drawable.todo_logo_dark
    else R.drawable.todo_logo_light
}

