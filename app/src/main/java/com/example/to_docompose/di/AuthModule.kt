package com.example.to_docompose.di

import android.app.Application
import android.content.Context
import com.example.to_docompose.R
import com.example.to_docompose.data.repository.auth.AuthRepository
import com.example.to_docompose.data.repository.auth.AuthRepositoryImpl
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    companion object {
        @Provides
        fun provideOneTapClient(
            context: Application
        ) = Identity.getSignInClient(context)

        @Provides
        fun providesFirebaseAuth() = FirebaseAuth.getInstance()

        @Provides
        fun providesSignUpRequest(app: Application) = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(app.getString(R.string.web_client_id))
                    .build()
            )
            .build()

        @Provides
        fun providesGoogleSignInOptions(app: Application) = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(app.getString(R.string.web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        @Provides
        fun providesGoogleSignInClient(
            app: Application,
            options: GoogleSignInOptions,
        ) = GoogleSignIn.getClient(app, options)
    }
    @Binds
    abstract fun bindsAuthRepository(repo: AuthRepositoryImpl): AuthRepository
}