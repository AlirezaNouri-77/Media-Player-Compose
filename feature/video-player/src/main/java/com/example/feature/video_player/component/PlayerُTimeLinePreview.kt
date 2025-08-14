package com.example.feature.video_player.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.common.util.convertMilliSecondToTime

@Composable
fun PlayerTimeLinePreview(
  modifier: Modifier = Modifier,
  shouldShow: Boolean,
  previewBitmap: ImageBitmap?,
  videoPosition: Int,
) {

  AnimatedVisibility(
    modifier = modifier,
    visible = shouldShow,
  ) {
    Card(
      modifier = Modifier
        .size(width = 170.dp, height = 140.dp),
      shape = RoundedCornerShape(5.dp),
      colors = CardDefaults.cardColors(
        containerColor = Color.Black.copy(alpha = 0.5f),
      )
    ) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        if (LocalInspectionMode.current) {
          Box(modifier = Modifier
            .matchParentSize()
            .background(Color.Red)
          )
        } else if (previewBitmap != null) {
          Image(
            bitmap = previewBitmap,
            modifier = Modifier.matchParentSize(),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
          )
        }
        Text(
          text = videoPosition.convertMilliSecondToTime(),
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 5.dp)
            .background(color = Color.Black.copy(0.7f), RoundedCornerShape(5.dp))
            .padding(horizontal = 4.dp),
          fontSize = 15.sp,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Medium,
          color = Color.White,
        )
      }
    }

  }

}

@Preview
@Composable
private fun PreviewPlayerControllerLayout() {
  MediaPlayerJetpackComposeTheme {
    PlayerTimeLinePreview(
      modifier = Modifier,
      shouldShow = true,
      previewBitmap = null,
      videoPosition = 10000,
    )
  }
}
