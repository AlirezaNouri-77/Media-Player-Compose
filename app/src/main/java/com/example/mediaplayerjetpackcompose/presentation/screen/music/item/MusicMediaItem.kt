package com.example.mediaplayerjetpackcompose.presentation.screen.music.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.util.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.data.util.removeFileExtension
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.ArtworkImage

@Composable
fun MusicMediaItem(
  item: MusicModel,
  isFav: Boolean,
  currentMediaId: String,
  contentColor: Color = MaterialTheme.colorScheme.onPrimary,
  onItemClick: () -> Unit,
) {

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onItemClick.invoke() }
      .background(if (currentMediaId == item.musicId.toString()) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f) else Color.Transparent),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp),
    ) {
      ArtworkImage(
        uri = { item.artworkUri },
        modifier = Modifier
          .size(size = 55.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(color = MaterialTheme.colorScheme.primary),
        inset = 50f,
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = item.duration.convertMilliSecondToTime(),
          color = contentColor,
          textAlign = TextAlign.Center,
        )
        if (isFav) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onPrimary
          )
        }
      }
    }
  }
}
