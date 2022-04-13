package com.rmw.clientapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmw.clientapp.APIService
import com.rmw.clientapp.repository.User
import kotlinx.coroutines.*
import java.lang.Exception

class TodoViewModel : ViewModel() {
    private var _user: User by mutableStateOf(User(0, "", ""))
    var errorMessage: String by mutableStateOf("")
    val user: User
        get() = _user

    fun getLoggedInUser(username: String) {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            try {
                _user = apiService.getUser(username)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                println(errorMessage)
            }
        }
    }
}