package com.rmw.clientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rmw.clientapp.ui.theme.ClientAppTheme
import com.rmw.clientapp.view.AuthView
import com.rmw.clientapp.view.CurrentUserProfileView
import com.rmw.clientapp.view.LoggedInView
import com.rmw.clientapp.viewmodel.LoginViewModel
import com.rmw.clientapp.viewmodel.LoggedInViewModel
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalMaterialApi
@ExperimentalSerializationApi
class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val context = this
        super.onCreate(savedInstanceState)
        setContent {
            ClientAppTheme() {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { AuthView(LoginViewModel(context, navController), navController) }
                    composable(
                        "home/{userArg}",
                        arguments = listOf(navArgument("userArg") {
                            type = UserParameterType()
                        })
                    ) { LoggedInView(LoggedInViewModel(), navController) }
                    composable("currentUserProfile") { CurrentUserProfileView() }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}