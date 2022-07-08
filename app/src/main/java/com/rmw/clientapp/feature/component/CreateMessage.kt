package com.rmw.clientapp.feature.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rmw.clientapp.feature.component.CreateMessageButton

@Composable
fun CreateMessage(onClick: () -> Unit){
    var userMessage by remember {
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = userMessage,
                onValueChange = { newMessage ->
                    userMessage = newMessage
                }
            )
            CreateMessageButton(text = "Create Message", onClick = {
                onClick()
            })
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}