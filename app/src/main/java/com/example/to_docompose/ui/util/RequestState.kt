package com.example.to_docompose.ui.util

sealed class RequestState<out T>{
    object Idle: RequestState<Nothing>()
    object Loading: RequestState<Nothing>()
    data class Success<T>(val data: T): RequestState<T>()
    class Error(e: Throwable): RequestState<Nothing>()
}
