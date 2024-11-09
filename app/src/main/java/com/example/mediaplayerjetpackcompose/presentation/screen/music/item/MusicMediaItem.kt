package com.example.mediaplayerjetpackcompose.presentation.screen.music.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.WaveForm
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.ThumbnailImage

@Composable
fun MusicMediaItem(
  item: MusicModel,
  isPlaying: Boolean,
  isFav: Boolean,
  currentMediaId: String,
  contentColor: Color = MaterialTheme.colorScheme.onPrimary,
  onItemClick: () -> Unit,
) {
  val isPlayingThisItemColor =
    if (currentMediaId == item.musicId.toString()) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f) else Color.Transparent

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onItemClick.invoke() }
      .background(color = isPlayingThisItemColor, shape = RoundedCornerShape(20.dp)),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp),
    ) {
      Box(contentAlignment = Alignment.Center) {
        ThumbnailImage(
          uri = item.artworkUri,
          modifier = Modifier
            .size(size = 55.dp)
            .clip(RoundedCornerShape(5.dp))
            .then(if (currentMediaId == item.musicId.toString()) Modifier.blur(5.dp) else Modifier)
            .background(color = MaterialTheme.colorScheme.primary),
        )
        if (currentMediaId == item.musicId.toString()) {
          WaveForm(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
            size = 55.dp,
            enable = isPlaying,
            waveColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
          )
        }
      }

      Spacer(modifier = Modifier.width(10.dp))
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.8f),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = item.name.removeFileExtension(),
          fontSize = 16.sp,
          fontWeight = FontWeight.Medium,
          maxLines = 1,
          color = contentColor,
          overflow = TextOverflow.Ellipsis,
          softWrap = true
        )
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = item.artist,
          fontSize = 16.sp,
          color = contentColor,
          fontWeight = FontWeight.Normal,
          maxLines = 1,
        )
      }
      Column(
        Modifier.weight(0.2f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End,
      ) {
        Text(
          modifier = Modifier.fillMaxWidth(),
          text = item.duration.convertMilliSecondToTime(),
          color = contentColor,
          textAlign = TextAlign.End,
        )
        if (isFav) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "",
            modifier = Modifier.size(15.dp),
            tint = MaterialTheme.colorScheme.onPrimary
          )
        }
      }
    }
  }
}