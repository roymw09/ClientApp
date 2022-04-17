package com.rmw.clientapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun ComposeMessageButton(
    icon: Painter,
    onClick: () -> Unit,
    isClicked: Boolean = false
) {
    Surface(
        modifier = Modifier.clickable(
            enabled = !isClicked,
            onClick = onClick
        ),
        color = Color.Unspecified
    ) {
        Row(
            modifier = Modifier
                .padding(
                    end = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterVertically)
                    .size(30.dp),
                painter = icon, contentDescription = "Message icon"
            )
        }
    }
}