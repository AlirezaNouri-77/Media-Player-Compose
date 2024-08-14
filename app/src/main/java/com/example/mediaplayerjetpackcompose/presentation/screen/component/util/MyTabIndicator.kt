package com.example.mediaplayerjetpackcompose.presentation.screen.component.util

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap

fun Modifier.myCustomTabIndicator(
  currentTapPosition: TabPosition,
): Modifier = composed {
  val currentTabWidth by animateDpAsState(
    targetValue = currentTapPosition.width,
    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing), label = "",
  )
  val currentTabIndicator by animateDpAsState(
    targetValue = currentTapPosition.left,
    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing), label = "",
  )
  wrapContentSize(Alignment.CenterStart)
    .offset(x = currentTabIndicator)
    .width(currentTabWidth)
}

@Composable
fun MyTabIndicator(modifier: Modifier = Modifier) {
  val lineColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
  Box(
    modifier
      .fillMaxSize()
      .drawWithCache {
        onDrawBehind {
          drawLine(
            color = lineColor,
            start = Offset(x =  25f, y = this.size.height - 18f),
            end = Offset(x = this.size.width - 25f , y = this.size.height - 18f),
            strokeWidth = 6f,
            cap = StrokeCap.Round,
          )
        }
      }
  )
}