package com.rmw.clientapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rmw.clientapp.CreateMessageIcon
import com.rmw.clientapp.DisplayPublisherMessages
import com.rmw.clientapp.model.Content
import com.rmw.clientapp.model.User
import com.rmw.clientapp.viewmodel.LoggedInViewModel
import com.rmw.clientapp.R
import com.rmw.clientapp.components.CreateMessage

@Composable
fun LoggedInView(vm: LoggedInViewModel, navController: NavController) {
    val user: User = navController.currentBackStackEntry?.arguments?.getParcelable("userArg")!!
    val contentList: List<Content> by remember {
        mutableStateOf(vm.content)
    }
    LaunchedEffect(Unit, block = {
        vm.getLoggedInUser(user.username)
        vm.getPublisherContent()
    })
    var showCreateMessageBox by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
                                .align(Alignment.CenterVertically),
                            text = user.username
                        )
                        CreateMessageIcon(
                            icon = painterResource(id = R.drawable.create_message),
                            isClicked = showCreateMessageBox,
                            onClick = {
                                showCreateMessageBox = true
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
                if (showCreateMessageBox) {
                    CreateMessage(onClick = {
                        showCreateMessageBox = false;
                    })
                }
                if (vm.errorMessage.isEmpty()) {
                    DisplayPublisherMessages(contentList)
                }
            }
        }
    )
}