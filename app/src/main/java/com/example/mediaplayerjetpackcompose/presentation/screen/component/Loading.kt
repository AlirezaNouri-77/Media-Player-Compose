package com.example.mediaplayerjetpackcompose.presentation.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Loading(
  modifier: Modifier,
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Text(text = "Loading")
  }
}