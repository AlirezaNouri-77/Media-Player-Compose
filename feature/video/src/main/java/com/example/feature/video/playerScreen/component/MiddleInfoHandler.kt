package com.example.feature.video.playerScreen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.util.convertMilliSecondToTime
import com.example.feature.video.model.MiddleVideoPlayerIndicator

@Composable
fun MiddleInfoHandler(
  modifier: Modifier,
  showInfoMiddleScreen: Boolean,
  seekPosition: Long,
  middleVideoPlayerIndicator: MiddleVideoPlayerIndicator,
) {
  AnimatedVisibility(
    visible = showInfoMiddleScreen,
    enter = fadeIn(),
    exit = fadeOut(),
    modifier = modifier
  ) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      var text = ""
      var icon: Int? = 0
      when (middleVideoPlayerIndicator) {
        is MiddleVideoPlayerIndicator.FastSeek -> {
          text = middleVideoPlayerIndicator.seekMode.message
          icon = middleVideoPlayerIndicator.seekMode.icon
        }

        is MiddleVideoPlayerIndicator.Seek -> {
          text = seekPosition.toInt().convertMilliSecondToTime()
          icon = null
        }

        else -> {}
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
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
        icon?.let {
          Icon(
            painter = painterResource(id = icon), contentDescription = "", Modifier.size(25.dp),
            tint = Color.White
          )
          Spacer(modifier = Modifier.width(10.dp))
        }
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