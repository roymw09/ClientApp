package com.rmw.clientapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rmw.clientapp.repository.User
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
                    ) { TodoView(LoggedInViewModel(), navController) }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}

@Composable
fun TodoView(vm: LoggedInViewModel, navController: NavController) {
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
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.profile_picture),
                            contentDescription = "Logged in user profile picture",
                            modifier = Modifier
                                // Set image size to 40dp
                                .size(60.dp)
                                // Clip the image so it's shaped like a circle
                                .clip(CircleShape)
                                // Add a border to the image
                                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
                        )
                        Text(
                            modifier = Modifier.padding(5.dp).size(60.dp).fillMaxSize(),
                            text = user.username
                        )
                    }
                }, Modifier.fillMaxWidth().size(70.dp))
        },
        content = {
            if (vm.errorMessage.isEmpty()) {
                Column {
                    PublisherMessages(contentList)
                }
            } else {
                Log.i("API ERROR: ", vm.errorMessage)
            }
        }
    )
}

@Composable
fun PublisherMessages(messages: List<Content>) {
    LazyColumn {
        items(messages) { message ->
            Column {
                MessageCard(Message("PublisherId: " + message.publisher_id, message.content))
            }
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    // Add padding around the image
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Publisher profile picture",
            modifier = Modifier
                // Set image size to 40dp
                .size(40.dp)
                // Clip the image so it's shaped like a circle
                .clip(CircleShape)
                // Add a border to the image
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(8.dp))

        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor: Color by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        )

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            // Set the color of the author text
            Text(
                text = msg.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            // Add a vertical space between the author and message
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun PreviewConversation() {
    ClientAppTheme {
        //TodoView(vm = TodoViewModel())
    }
}