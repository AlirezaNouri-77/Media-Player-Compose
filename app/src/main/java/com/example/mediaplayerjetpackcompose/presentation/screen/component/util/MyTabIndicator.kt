package com.example.mediaplayerjetpackcompose.presentation.screen.component.util

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
  Column(
    modifier
      .fillMaxSize()
      .background(
        color = Color(0xFF37393a).copy(alpha = 0.3f),
        RoundedCornerShape(15.dp),
      ),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
  }
}