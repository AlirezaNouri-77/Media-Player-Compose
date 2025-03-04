package com.example.feature.video.playerScreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.core.designsystem.NoRippleEffect

@Composable
fun PlayerControllerButton(
  icon: Int,
  onClick: () -> Unit,
  modifier: Modifier,
) {
  Image(
    painter = painterResource(id = icon),
    contentDescription = "",
    modifier = modifier
      .clickable(NoRippleEffect, null) {
        onClick.invoke()
      },
    colorFilter = ColorFilter.tint(Color.White),
  )
}