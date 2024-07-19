package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect

@Composable
fun PlayerStateControllerButton(
  icon: Int,
  modifier: Modifier = Modifier,
  backgroundColor: Color = Color.White,
  backgroundAlpha: Float = 0.2f,
  radius: Float = 1f,
  size: Dp,
  onClick: () -> Unit,
) {
  Image(
    painter = painterResource(id = icon),
    contentDescription = "",
    modifier
      .size(size)
      .drawBehind {
        drawCircle(
          backgroundColor,
          center = this.center,
          radius = this.size.minDimension / radius,
          alpha = backgroundAlpha
        )
      }
      .clickable(interactionSource = NoRippleEffect, indication = null) {
        onClick.invoke()
      },
  )
}
