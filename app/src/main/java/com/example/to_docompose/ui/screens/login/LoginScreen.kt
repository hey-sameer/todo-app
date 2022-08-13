package com.example.to_docompose.ui.screens.login

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_docompose.R
import com.example.to_docompose.component.GoogleLoginButton
import com.example.to_docompose.component.MyGoogleButton
import com.example.to_docompose.ui.util.RequestState
import com.example.to_docompose.ui.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(authViewModel: AuthViewModel, onSignInSuccess: () -> Unit) {
    LaunchedEffect(Unit) {
        if (authViewModel.auth.currentUser != null) {
            onSignInSuccess()
        }
    }
    val context = LocalContext.current
    val oneTapSignUpResponse by authViewModel.oneTapSignUpResponse.collectAsState()
    val signInResponse by authViewModel.signInResponse.collectAsState()

    Scaffold(
        content = {
            ScreenContent(modifier = Modifier.padding(it)) {
                authViewModel.oneTapSignInInitiate()
            }
        }
    )
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == RESULT_OK) {
                try {
                    val credential =
                        authViewModel.oneTapClient.getSignInCredentialFromIntent(it.data)
                    val googleCredentials =
                        GoogleAuthProvider.getCredential(credential.googleIdToken, null)
                    authViewModel.signInWithGoogleCredentials(googleCredentials)
                } catch (e: ApiException) {
//                Toast.makeText(this@LoginScreen, "", Toast.LENGTH_LONG).show()
                    println("Api exception---> $e")
                }
            }
        }

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    LaunchedEffect(key1 = oneTapSignUpResponse) {
        when (oneTapSignUpResponse) {
            is RequestState.Error -> {
                println("*****Error******")
                println((oneTapSignUpResponse as RequestState.Error).e)
            }
            RequestState.Idle -> println("****idle****")
            RequestState.Loading -> println("****loading****")
            is RequestState.Success -> {
                println("*****Success******")
                launch((oneTapSignUpResponse as RequestState.Success<BeginSignInResult>).data)
            }
        }
    }
    LaunchedEffect(key1 = signInResponse) {
        when (signInResponse) {
            is RequestState.Error -> {
                println((signInResponse as RequestState.Error).e)
            }
            RequestState.Idle -> println("****firebase: idle****")
            RequestState.Loading -> println("****firebase: loading****")
            is RequestState.Success -> {
                Toast.makeText(context, "Login Success", Toast.LENGTH_LONG).show()
                onSignInSuccess()
            }
        }
    }

}

@Composable
private fun ScreenContent(modifier: Modifier = Modifier, onGoogleClick: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_screen_img),
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            contentDescription = "login image"
        )
        Text(
            text = "Continue with",
            style = TextStyle(fontSize = 24.sp, fontWeight = Bold)
        )
        MyGoogleButton(onClicked = onGoogleClick)
    }

}

@Preview
@Composable
fun LoginScreenPreview() {
//    LoginScreen()
}