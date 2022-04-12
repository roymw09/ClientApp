package com.rmw.clientapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.rmw.clientapp.repository.AuthAPIService.GithubConstants
import com.rmw.clientapp.repository.AuthAPIService
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class AuthViewModel(context: Context, navController: NavController) : ViewModel() {
    val state = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    val githubAuthURLFull: String = GithubConstants.AUTHURL + "?client_id=" + GithubConstants.CLIENT_ID +
            "&scope=" + GithubConstants.SCOPE + "&redirect_uri=" + GithubConstants.REDIRECT_URI +
            "&state=" + state
    var authApi: AuthAPIService
    init {
        authApi = AuthAPIService(context, navController)
    }

    fun authenticateUser() {
        viewModelScope.launch {
            authApi.setupGithubWebViewDialog(githubAuthURLFull)
        }
    }
}