package com.rmw.clientapp.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmw.clientapp.model.Content
import com.rmw.clientapp.LoggedInAPIService
import com.rmw.clientapp.model.User
import com.rmw.clientapp.model.UserRole
import kotlinx.coroutines.*
import java.lang.Exception

class LoggedInViewModel : ViewModel() {
    private var _user: User by mutableStateOf(User(0, "", ""))
    val user: User
        get() = _user

    private val _content = mutableStateListOf<Content>()
    val content: List<Content>
        get() = _content

    private var _userPublisherRole: UserRole by mutableStateOf(UserRole(0, "", "", "", ""))
    val userPublisherRole: UserRole
        get() = _userPublisherRole

    var errorMessage: String by mutableStateOf("")

    init {
        getPublisherContent()
    }

    fun getPublisherContent() {
        viewModelScope.launch {
            val apiService = LoggedInAPIService.getInstance()
            try {
                _content.clear()
                _content.addAll(apiService.getPublisherContent())
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                Log.e("getPublisherContent", errorMessage)
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
                Log.e("getLoggedInUserError", errorMessage)
            }
        }
    }

    fun createContent(publisherId: Int, message: String) {
        viewModelScope.launch {
            // Services are currently not online, so for now all content
            // is only being stored in an array and not in the publisher service database
            /* TODO - uncomment apiService variable and make a call to the apiService's create content
                method once services are online
             */
            val apiService = LoggedInAPIService.getInstance()
            try {
                val content = Content(null, publisherId, message)
                apiService.createContent(content)
                _content.add(content)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                Log.e("createContent", errorMessage)
            }
        }
    }
}