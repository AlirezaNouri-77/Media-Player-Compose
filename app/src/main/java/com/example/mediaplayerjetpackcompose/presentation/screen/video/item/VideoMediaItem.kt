package com.example.mediaplayerjetpackcompose.presentation.screen.video.item

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mediaplayerjetpackcompose.data.util.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.util.extractFileExtension
import com.example.mediaplayerjetpackcompose.data.util.onIoDispatcher
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.VideoModel
import com.example.mediaplayerjetpackcompose.domain.model.VideoThumbnailModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun VideoMediaItem(
  item: VideoModel,
  videoPageViewModel: VideoPageViewModel,
  contextColor: Color = MaterialTheme.colorScheme.onPrimary,
  onItemClick: () -> Unit,
) {

  val imageBitmap: MutableState<Bitmap?> = remember {
    mutableStateOf(null)
  }

  LaunchedEffect(key1 = item, block = {
    onIoDispatcher {

      if ((videoPageViewModel.videoThumbnailBitmap.find { it.musicId == item.videoId }?.musicId
          ?: "") == item.videoId
      ) {
        imageBitmap.value = videoPageViewModel.videoThumbnailBitmap.first { it.musicId == item.videoId }.bitmap
      } else {
        val bitmap = videoPageViewModel.getVideoThumbNail(item.uri)
        videoPageViewModel.videoThumbnailBitmap.add(VideoThumbnailModel(bitmap!!, item.videoId))
        imageBitmap.value = bitmap
      }

    }
  })

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
        Box(
          modifier = Modifier
            .constrainAs(imageArt) {
              top.linkTo(parent.top)
              bottom.linkTo(parent.bottom)
              end.linkTo(parent.end)
              start.linkTo(parent.start)
            }
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .size(width = 130.dp, height = 90.dp)
            .clip(RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center,
        ) {
          if (imageBitmap.value != null) {
            Image(
              bitmap = imageBitmap.value!!.asImageBitmap(),
              modifier = Modifier
                .size(width = 130.dp, height = 90.dp)
                .clip(RoundedCornerShape(8.dp)),
              contentScale = ContentScale.FillBounds,
              contentDescription = "",
            )
          } else {
            Text(
              text = "Loading",
              fontSize = 12.sp,
            )
          }
        }

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
          maxLines = 2,
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
