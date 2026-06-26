package com.example.mediaplayerjetpackcompose.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowMessage(
    modifier: Modifier = Modifier,
    message: String,
    actionMessage: String,
    onAction: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
        TextButton(
            onClick = onAction,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Text(text = actionMessage, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
