package com.example.to_docompose.data.repository.auth

import com.example.to_docompose.ui.util.RequestState
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private var oneTapClient: SignInClient,
    private var signInRequest: BeginSignInRequest,
    private val auth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient,

): AuthRepository {
    override suspend fun oneTapSignUpWithGoogle(): Flow<RequestState<BeginSignInResult>> {
        return flow {
            try {
                emit(RequestState.Loading)
                val result = oneTapClient.beginSignIn(signInRequest).await()
                emit(RequestState.Success(result))
            }catch (e: Exception){
                emit(RequestState.Error(e))
            }
        }
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Flow<RequestState<Boolean>> = flow{
            emit(RequestState.Loading)
            try {
                auth.signInWithCredential(googleCredential).await()
                emit(RequestState.Success(true))
            }catch (e: Exception){
                emit(RequestState.Error(e))
            }
        }


    override suspend fun signOut(): Flow<RequestState<Boolean>> = flow{
        try {
            emit(RequestState.Loading)
            auth.signOut()
            oneTapClient.signOut().await()
            emit(RequestState.Success(true))
        }catch (e: Exception){
            emit(RequestState.Error(e))
        }
    }

    override suspend fun revokeAccess(): Flow<RequestState<Boolean>> = flow{
        try {
            emit(RequestState.Loading)
            auth.signOut()
            googleSignInClient.revokeAccess().await()
            emit(RequestState.Success(true))
        }catch (e: Exception){
            emit(RequestState.Error(e))
        }
    }
}