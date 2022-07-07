package com.rmw.clientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rmw.clientapp.model.Content
import com.rmw.clientapp.model.User
import com.rmw.clientapp.ui.theme.ClientAppTheme
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
                    ) { MainView(LoggedInViewModel(), navController) }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    var showCreateMessage: Boolean by mutableStateOf(false)

    @Composable
    fun MainView(vm: LoggedInViewModel, navController: NavController) {
        val user: User = navController.currentBackStackEntry?.arguments?.getParcelable("userArg")!!
        val contentList: List<Content> by remember {
            mutableStateOf(vm.content)
        }
        LaunchedEffect(Unit, block = {
            vm.getLoggedInUser(user.username)
            vm.getPublisherContent()
        })

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.profile_picture),
                                contentDescription = "Logged in user profile picture",
                                modifier = Modifier
                                    // Set image size to 60dp
                                    .size(60.dp)
                                    // Clip the image so it's shaped like a circle
                                    .clip(CircleShape)
                                    // Add a border to the image
                                    .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
                            )
                            Text(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(60.dp)
                                    .weight(1f)
                                    .wrapContentHeight()
                                    .align(CenterVertically),
                                text = user.username
                            )
                            ComposeMessageButton(
                                icon = painterResource(id = R.drawable.create_message),
                                isClicked = showCreateMessage,
                                onClick = {
                                    showCreateMessage = true
                                }
                            )
                        }
                    },
                    Modifier
                        .fillMaxWidth()
                        .size(70.dp))
            },
            content = {
                Column {
                    if (showCreateMessage) {
                        CreateMessageView(vm)
                    }
                    if (vm.errorMessage.isEmpty()) {
                        DisplayPublisherMessages(contentList)
                    }
                }
            }
        )
    }

    @Composable
    fun CreateMessageView(vm: LoggedInViewModel){
        var userMessage: TextFieldValue by remember {
            mutableStateOf(TextFieldValue(""))
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = CenterHorizontally) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userMessage,
                    onValueChange = { newMessage ->
                        userMessage = newMessage
                    }
                )
                CreateMessageButton(text = "Create Message", onClick = {
                    showCreateMessage = false
                })
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}