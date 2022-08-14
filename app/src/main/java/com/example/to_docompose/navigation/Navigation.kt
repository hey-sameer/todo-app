package com.example.to_docompose.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.example.to_docompose.data.models.ToDoTask
import com.example.to_docompose.ui.screens.list.ListScreen
import com.example.to_docompose.ui.screens.login.LoginScreen
import com.example.to_docompose.ui.screens.splash.SplashScreen
import com.example.to_docompose.ui.screens.task.TaskScreen
import com.example.to_docompose.ui.util.CONSTANTS.LIST_SCREEN_ARG_KEY
import com.example.to_docompose.ui.util.CONSTANTS.LIST_SCREEN_ROUTE
import com.example.to_docompose.ui.util.CONSTANTS.LOGIN_SCREEN_ROUTE
import com.example.to_docompose.ui.util.CONSTANTS.SPLASH_SCREEN_ROUTE
import com.example.to_docompose.ui.util.CONSTANTS.TASK_SCREEN_ARG_KEY
import com.example.to_docompose.ui.util.CONSTANTS.TASK_SCREEN_ROUTE
import com.example.to_docompose.ui.util.toAction
import com.example.to_docompose.ui.viewmodels.AuthViewModel
import com.example.to_docompose.ui.viewmodels.SharedViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

var _taskID: Int? = null
var _selectedTask: ToDoTask? = null


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    authViewModel: AuthViewModel,
) {
    //didn't pass controller to remember (...)
    val screen = remember {
        Screens(navController)
    }
    AnimatedNavHost(navController = navController, startDestination = SPLASH_SCREEN_ROUTE) {

        //splash composable
        composable(route = SPLASH_SCREEN_ROUTE) {
            SplashScreen(screen.splash)
        }

        composable(route = LOGIN_SCREEN_ROUTE) {
            LoginScreen(authViewModel, screen.login)
        }

        //to List Screen
        composable(
            route = LIST_SCREEN_ROUTE,
            arguments = listOf(
                navArgument(LIST_SCREEN_ARG_KEY) {
                    type = NavType.StringType
                }
            ),
            exitTransition = { ->
//                fadeOut(animationSpec = tween(300))
                slideOutHorizontally(
                    targetOffsetX = { -1000 },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing,
                    ),
                )
            },
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -1000 },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing,
                    ),
                )
            },
            popEnterTransition = { ->
                slideInHorizontally(
                    initialOffsetX = { -1000 },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing,
                    ),
                )
            }
        ) { navBackStack ->
            val action = navBackStack.arguments!!.getString(LIST_SCREEN_ARG_KEY).toAction()
            LaunchedEffect(key1 = action) {
                sharedViewModel.action.value = action
            }
            ListScreen(onTaskClick = screen.task, sharedViewModel = sharedViewModel)
        }

        //to Task Screen
        composable(
            route = TASK_SCREEN_ROUTE,
            arguments = listOf(
                navArgument(TASK_SCREEN_ARG_KEY) {
                    type = NavType.IntType
                }
            ),
            enterTransition = { ->
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing,
                    )
                )
            },
            exitTransition = { ->
//                fadeOut(animationSpec = tween(300))
                slideOutHorizontally(
                    targetOffsetX = { 1000 },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing,
                    ),
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 1000 },
                    animationSpec = tween(
                        300,
                        easing = FastOutSlowInEasing,
                    ),
                )
            }
        ) { navBackStackEntry ->
            val taskID = navBackStackEntry.arguments!!.getInt(TASK_SCREEN_ARG_KEY)
            LaunchedEffect(key1 = taskID) {
                sharedViewModel.getTask(taskID)
            }
            val selectedTask by sharedViewModel.selectedTask.collectAsState()
            LaunchedEffect(key1 = selectedTask) {
                if (selectedTask != _selectedTask) {
                    if (selectedTask != null || taskID == -1) {
                        sharedViewModel.updateFieldsWithCurrentSelectedTask(selectedTask)
                    }
                    _selectedTask = selectedTask
                }
            }
            TaskScreen(selectedTask, toListScreen = screen.list, sharedViewModel)
        }
    }
}