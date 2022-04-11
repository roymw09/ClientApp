package com.rmw.clientapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rmw.clientapp.repository.AuthAPIService.GithubConstants
import com.rmw.clientapp.repository.AuthAPIService
import com.rmw.clientapp.repository.AuthUser
import java.util.concurrent.TimeUnit

class AuthViewModel(context: Context) : ViewModel() {
    val state = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    val githubAuthURLFull: String = GithubConstants.AUTHURL + "?client_id=" + GithubConstants.CLIENT_ID +
            "&scope=" + GithubConstants.SCOPE + "&redirect_uri=" + GithubConstants.REDIRECT_URI +
            "&state=" + state
    var authApi: AuthAPIService
    init {
        authApi = AuthAPIService(context)
    }

    fun authenticateUser() {
        authApi.setupGithubWebViewDialog(githubAuthURLFull)
    }
}