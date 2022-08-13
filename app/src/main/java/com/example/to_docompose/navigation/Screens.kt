package com.example.to_docompose.navigation

import androidx.navigation.NavHostController
import com.example.to_docompose.ui.util.Action
import com.example.to_docompose.ui.util.CONSTANTS

class Screens(navController: NavHostController) {

    val splash : () -> Unit = {
        navController.navigate(route = CONSTANTS.LOGIN_SCREEN_ROUTE){
            popUpTo(CONSTANTS.SPLASH_SCREEN_ROUTE){
                inclusive = true
            }
        }
    }
    val login: () -> Unit = {
        navController.navigate(route = "list/${Action.NO_ACTION.name}"){
            popUpTo(route = CONSTANTS.LOGIN_SCREEN_ROUTE){
                inclusive = true
            }
        }
    }

    //its a function which will navigate to ListScreen
    //It takes a param action which will trigger necessary action in ListScreen
    val list: (Action) -> Unit = { action: Action ->
        navController.navigate(route = "list/${action.name}") {
            popUpTo(CONSTANTS.LIST_SCREEN_ROUTE) {
                inclusive = true
            }
        }
    }

    //its a function which will navigate to TaskScreen
    //It takes a param action which will trigger necessary action in TaskScreen
    val task: (Int) -> Unit = { taskID ->
        navController.navigate(route = "task/${taskID}")
    }

}
