package com.rmw.clientapp.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmw.clientapp.Content
import com.rmw.clientapp.LoggedInAPIService
import com.rmw.clientapp.repository.User
import kotlinx.coroutines.*
import java.lang.Exception

class LoggedInViewModel : ViewModel() {
    private var _user: User by mutableStateOf(User(0, "", ""))
    val user: User
        get() = _user

    var _content = mutableStateListOf<Content>()
    val content: List<Content>
        get() = _content

    var errorMessage: String by mutableStateOf("")

    fun getPublisherContent() {
        viewModelScope.launch {
            val apiService = LoggedInAPIService.getInstance()
            try {
                _content.clear()
                _content.addAll(apiService.getPublisherContent())
                Log.i("API SUCCESS: ", content.size.toString())
                //content.add(apiService.getPublisherContent()) // TODO figure out how to flux to list

            } catch (e: Exception) {
                errorMessage = e.message.toString()
                println(errorMessage)
            }
        }
    }

    fun getLoggedInUser(username: String) {
        viewModelScope.launch {
            val apiService = LoggedInAPIService.getInstance()
            try {
                _user = apiService.getLoggedInUser(username)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                println(errorMessage)
            }
        }
    }
}