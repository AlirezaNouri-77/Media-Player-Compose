package com.example.core.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun EmptyPage(
  modifier: Modifier = Modifier,
  message: String = "Empty",
  textSize: TextUnit = 24.sp,
  textAlpha: Float = 0.5f
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Companion.Center,
  ) {
    Text(
      text = message,
      fontSize = textSize,
      fontWeight = FontWeight.Companion.Bold,
      textAlign = TextAlign.Companion.Center,
      color = MaterialTheme.colorScheme.onPrimary.copy(alpha = textAlpha),
    )
  }
}