package com.example.mediaplayerjetpackcompose.presentation.screen.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

fun Modifier.verticalFadeEdge(): Modifier {
  return this.then(
    this
      .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
      .drawWithCache {
        onDrawWithContent {
          drawContent()
          drawRect(
            brush = Brush.horizontalGradient(
              0f to Color.Transparent,
              0.02f to Color.White,
              0.95f to Color.White,
              1f to Color.Transparent,
          ),
            blendMode = BlendMode.DstIn,
            size = Size(width = this.size.width + 20.dp.value , height = this.size.height)
          )
        }
      }
  )
}