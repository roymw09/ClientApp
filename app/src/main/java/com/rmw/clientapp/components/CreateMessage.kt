package com.rmw.clientapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rmw.clientapp.CreateMessageButton
import com.rmw.clientapp.viewmodel.LoggedInViewModel

@Composable
fun CreateMessage(vm: LoggedInViewModel){
    var userMessage by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var showCreateMessage = true
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = userMessage,
                onValueChange = { newMessage ->
                    userMessage = newMessage
                }
            )
            CreateMessageButton(text = "Create Message", onClick = {
                // TODO - create a function in MainActivity that changes the value of this variable
                showCreateMessage = false
            })
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}