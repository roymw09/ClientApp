package com.rmw.clientapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rmw.clientapp.R
import com.rmw.clientapp.feature.component.CreateMessageIcon

@Composable
fun CurrentUserProfileView() {
    Scaffold (
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
                            text = "Test"
                        )
                        CreateMessageIcon(
                            icon = painterResource(id = R.drawable.create_message),
                            //isClicked = showCreateMessageBox,
                            onClick = {

                            }
                        )
                    }
                },
                Modifier
                    .fillMaxWidth()
                    .size(70.dp)
            )
        },
        content = {

        }
    )
}