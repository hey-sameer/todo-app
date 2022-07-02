package com.example.to_docompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val LowPriorityColor = Color(0XFF00C980)
val MediumPriorityColor = Color(0XFFFFC114)
val HighPriorityColor = Color(0XFFFF4646)
val NonePriorityColor = Color.LightGray

val Colors.splashScreenBackground : Color
    @Composable
    get() = if(isSystemInDarkTheme()) Color.Black else Purple700