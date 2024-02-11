package com.example.mediaplayerjetpackcompose.presentation.screenComponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mediaplayerjetpackcompose.data.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.extractFileExtension
import com.example.mediaplayerjetpackcompose.data.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel

@Composable
fun VideoMediaItem(
  item: VideoMediaModel,
  imageBitmap: ImageBitmap,
  onItemClick: (VideoMediaModel) -> Unit,
) {
  Surface(
    onClick = { onItemClick.invoke(item) },
    modifier = Modifier
      .fillMaxWidth()
      .padding(5.dp),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start,
    ) {
      ConstraintLayout {
        val (imageArt, duration) = createRefs()
        Image(
          bitmap = imageBitmap,
          modifier = Modifier
            .constrainAs(imageArt) {
              top.linkTo(parent.top)
              bottom.linkTo(parent.bottom)
              end.linkTo(parent.end)
              start.linkTo(parent.start)
            }
            .size(width = 130.dp, height = 90.dp)
            .clip(RoundedCornerShape(8.dp)),
          contentScale = ContentScale.FillBounds,
          contentDescription = "",
        )
        Text(
          text = item.duration.convertMilliSecondToTime(),
          modifier = Modifier
            .constrainAs(duration) {
              bottom.linkTo(parent.bottom, 7.dp)
              end.linkTo(parent.end, 7.dp)
            }
            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(5.dp)),
          textAlign = TextAlign.End,
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
          fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.height(10.dp))
        val detail = listOf(
          item.width.toString() + "x" + item.height.toString(),
          item.size.convertByteToReadableSize(),
          item.name.extractFileExtension(),

        )
        Text(
          text = detail.reduce{acc, string -> "$acc, $string" }.toString(),
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Start,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
        )
      }
    }
  }

}