package com.example.mediaplayerjetpackcompose.presentation.screenComponent


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel

@Composable
fun MusicMediaItem(
  item: MusicMediaModel,
  currentMediaID: String,
  onItemClick: () -> Unit,
  modifier: Modifier,
) {
  Surface(
    onClick = { onItemClick.invoke() },
    modifier = modifier
      .fillMaxWidth()
      .background(color = if (currentMediaID == item.musicId.toString()) Color.Gray else Color.White),
  ) {
    Row(
      modifier = modifier
        .padding(vertical = 7.dp, horizontal = 5.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Column(Modifier.weight(1f)) {
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = item.name,
          fontSize = 18.sp,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
        )
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = item.artist,
          fontSize = 16.sp,
          fontWeight = FontWeight.Normal,
          maxLines = 1,
        )
      }
      Column(Modifier.weight(0.3f)) {
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