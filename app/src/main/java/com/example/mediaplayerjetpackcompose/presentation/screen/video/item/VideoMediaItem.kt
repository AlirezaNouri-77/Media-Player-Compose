package com.example.mediaplayerjetpackcompose.presentation.screen.video.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.example.mediaplayerjetpackcompose.data.util.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.util.extractFileExtension
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.VideoModel

@Composable
fun VideoMediaItem(
  item: VideoModel,
  contextColor: Color = MaterialTheme.colorScheme.onPrimary,
  onItemClick: () -> Unit,
) {

  Surface(
    onClick = { onItemClick.invoke() },
    modifier = Modifier
      .fillMaxWidth(),
    color = Color.Transparent,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
      ConstraintLayout {
        val (imageArt, duration) = createRefs()

        AsyncImage(
          modifier = Modifier
            .constrainAs(imageArt) {
              top.linkTo(parent.top)
              bottom.linkTo(parent.bottom)
              end.linkTo(parent.end)
              start.linkTo(parent.start)
            }
            .size(100.dp, 80.dp)
            .clip(RoundedCornerShape(10.dp)),
          model = ImageRequest.Builder(LocalContext.current).data(item.uri).videoFrameMillis(15_000L).build(),
          contentDescription = "",
        )

        Text(
          text = item.duration.convertMilliSecondToTime(),
          modifier = Modifier
            .constrainAs(duration) {
              bottom.linkTo(parent.bottom, 5.dp)
              end.linkTo(parent.end, 5.dp)
            }
            .drawBehind {
              drawRoundRect(
                color = Color.Black,
                alpha = 0.5f,
                size = this.size,
                cornerRadius = CornerRadius(x = 15f, y = 15f),
              )
            },
          fontSize = 14.sp,
          color = Color.White,
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(
          text = item.name.removeFileExtension(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          fontSize = 16.sp,
          color = contextColor,
          fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(10.dp))
        val detail = listOf(
          item.width.toString() + "x" + item.height.toString(),
          item.size.convertByteToReadableSize(),
          item.name.extractFileExtension(),
          )
        Text(
          text = detail.reduce { acc, string -> "$acc, $string" }.toString(),
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Start,
          fontSize = 13.sp,
          color = contextColor,
          fontWeight = FontWeight.Medium,
        )
      }
    }
  }

}
