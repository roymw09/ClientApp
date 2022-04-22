package com.rmw.clientapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.rmw.clientapp.model.Content

@Composable
fun DisplayPublisherMessages(messages: List<Content>) {
    LazyColumn {
        items(messages) { message ->
            Column {
                MessageCard(Message("PublisherId: " + message.publisher_id, message.content))
            }
        }
    }
}