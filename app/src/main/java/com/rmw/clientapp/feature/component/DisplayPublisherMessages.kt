package com.rmw.clientapp.feature.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.rmw.clientapp.feature.component.MessageCard
import com.rmw.clientapp.model.Content
import com.rmw.clientapp.model.Message

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