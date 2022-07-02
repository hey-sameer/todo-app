package com.example.to_docompose.ui.util

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun String?.toAction(): Action{
    return when(this){
        "ADD" -> Action.ADD
        "UPDATE" -> Action.UPDATE
        "DELETE" -> Action.DELETE
        "DELETE_ALL" -> Action.DELETE_ALL
        "UNDO" -> Action.UNDO
        "NO_ACTION" -> Action.NO_ACTION
        else -> Action.NO_ACTION
    }
}
