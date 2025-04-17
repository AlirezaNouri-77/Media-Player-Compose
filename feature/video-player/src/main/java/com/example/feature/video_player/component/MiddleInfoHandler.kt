package com.example.feature.video_player.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.video.R
import com.example.feature.video.model.MiddleVideoPlayerIndicator

@Composable
fun MiddleInfoHandler(
  modifier: Modifier,
  showInfoMiddleScreen: Boolean,
  middleVideoPlayerIndicator: MiddleVideoPlayerIndicator,
) {

  val text by remember {
    mutableStateOf("15")
  }
  var icon by remember {
    mutableIntStateOf(R.drawable.icon_fast_forward_24)
  }

  LaunchedEffect(middleVideoPlayerIndicator) {
    when (middleVideoPlayerIndicator) {
      is MiddleVideoPlayerIndicator.FastForward -> icon = middleVideoPlayerIndicator.icon
      is MiddleVideoPlayerIndicator.FastRewind -> icon = middleVideoPlayerIndicator.icon
      MiddleVideoPlayerIndicator.Initial -> {}
    }
  }

  AnimatedVisibility(
    visible = showInfoMiddleScreen,
    enter = fadeIn(),
    exit = fadeOut(),
    modifier = modifier
  ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        modifier = Modifier
          .drawBehind {
            drawRoundRect(
              Color.Black,
              size = this.size,
              alpha = 0.5f,
              cornerRadius = CornerRadius(x = 25f, y = 25f)
            )
          }
          .padding(5.dp)
      ) {
        Icon(
          modifier = Modifier.size(25.dp),
          painter = painterResource(id = icon),
          contentDescription = "",
          tint = Color.White
        )
        Text(
          text = text,
          fontSize = 18.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
        )
      }
    }
  }

}