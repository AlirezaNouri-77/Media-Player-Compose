package com.example.mediaplayerjetpackcompose.presentation.screenComponent


import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel

@Composable
fun MusicMediaItem(
  item: MusicMediaModel,
  artworkImage: ImageBitmap,
  currentMediaId: String,
  onItemClick: () -> Unit,
) {
  Surface(
    onClick = { onItemClick.invoke() },
    modifier = Modifier
      .fillMaxWidth(),
    color = if (currentMediaId == item.musicId.toString()) Color.Gray else Color.White
  ) {
    Row(
      modifier = Modifier
        .padding(vertical = 7.dp, horizontal = 5.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
    ) {
      Image(
        bitmap = artworkImage,
        contentDescription = "",
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center,
        modifier = Modifier
          .size(60.dp)
          .background(Color.Black,RoundedCornerShape(10.dp))
          .clip(RoundedCornerShape(10.dp)),
      )
      Spacer(modifier = Modifier.width(10.dp))
      Column(
        modifier = Modifier
          .weight(1f)
          .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = item.name,
          fontSize = 16.sp,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          softWrap = true
        )
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = item.artist,
          fontSize = 16.sp,
          fontWeight = FontWeight.Normal,
          maxLines = 1,
        )
      }
      Column(Modifier.weight(0.4f)) {
        Text(
          text = item.size.convertByteToReadableSize(),
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.End,
        )
        Text(
          text = item.duration.convertMilliSecondToTime(),
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.End,
        )
      }

    }
  }
}