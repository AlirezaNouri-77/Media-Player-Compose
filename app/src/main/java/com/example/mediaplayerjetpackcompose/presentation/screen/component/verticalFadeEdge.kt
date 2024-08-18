package com.example.mediaplayerjetpackcompose.presentation.screen.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.verticalFadeEdge(): Modifier {
  return this.then(
    this
      .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
      .drawWithCache {
        onDrawWithContent {
          drawContent()
          drawRect(
            Brush.horizontalGradient(
              0f to Color.Transparent,
              0.1f to Color.White,
              0.9f to Color.White,
              1f to Color.Transparent,
            ),
            blendMode = BlendMode.DstIn
          )
        }
      }
  )
}