package com.example.to_docompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.to_docompose.navigation.Navigation
import com.example.to_docompose.ui.theme.ToDoComposeTheme
import com.example.to_docompose.ui.viewmodels.AuthViewModel
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            
            ToDoComposeTheme {
                navController = rememberNavController()
                Navigation(navController = navController, sharedViewModel, authViewModel)
            }
        }
    }
}

