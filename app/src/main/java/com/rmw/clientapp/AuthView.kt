package com.rmw.clientapp

import android.content.Context
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rmw.clientapp.ui.theme.ClientAppTheme
import com.rmw.clientapp.ui.theme.Shapes
import com.rmw.clientapp.viewmodel.AuthViewModel

@ExperimentalMaterialApi
@Composable
fun AuthView(vm: AuthViewModel, navController: NavController) {
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

@ExperimentalMaterialApi
@Composable
fun SignInButton(
    text: String,
    loadingText: String = "Signing in...",
    icon: Painter,
    isLoading: Boolean = false,
    shape: Shape = Shapes.medium,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = MaterialTheme.colors.surface,
    progressIndicatorColor: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(
            enabled = !isLoading,
            onClick = onClick
        ),
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = "SignInButton",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(text = if (isLoading) loadingText else text)
            if (isLoading) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(showSystemUi = true)
@Composable
fun PreviewAuth() {
    ClientAppTheme {
        val context: Context? = null
        //AuthView(vm = AuthViewModel(context = context!!))
    }
}