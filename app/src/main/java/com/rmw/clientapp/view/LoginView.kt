package com.rmw.clientapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rmw.clientapp.R
import com.rmw.clientapp.components.SignInButton
import com.rmw.clientapp.ui.theme.Shapes
import com.rmw.clientapp.viewmodel.LoginViewModel

@ExperimentalMaterialApi
@Composable
fun AuthView(vm: LoginViewModel, navController: NavController) {
    var isLoading by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text("Login")
                    }
                })
        },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color.LightGray)
                    .fillMaxSize()
            ) {
                SignInButton(
                    text = "Sign in with Github",
                    loadingText = "Signing in...",
                    isLoading = isLoading,
                    icon = painterResource(id = R.drawable.github_96),
                    onClick = {
                        isLoading = true
                        vm.authenticateUser()
                    }
                )
            }
        }
    )
}