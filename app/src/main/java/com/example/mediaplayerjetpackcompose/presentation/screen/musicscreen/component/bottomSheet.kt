package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.MusicState
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
  currentMusicState: MusicState,
  currentMusicPosition: Long,
  artworkImage: ImageBitmap,
  sheetState: SheetState,
  onDismissed: () -> Unit,
  onPauseMusic: () -> Unit,
  onResumeMusic: () -> Unit,
  onMoveNextMusic: () -> Unit,
  onMovePreviousMusic: () -> Unit,
  onSeekTo: (Long) -> Unit,
) {

  ModalBottomSheet(
    onDismissRequest = { onDismissed.invoke() },
    Modifier.fillMaxSize(),
    sheetState = sheetState,
    shape = RectangleShape,
  ) {

    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {

      Text(
        text = currentMusicState.metadata.artist?.toString() ?: "Nothing Play",
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(15.dp),
      )

      Image(
        bitmap = artworkImage,
        contentDescription = "",
        modifier = Modifier
          .size(300.dp)
          .clip(RoundedCornerShape(15.dp))
      )

      Spacer(modifier = Modifier.height(25.dp))

      Text(
        text = currentMusicState.metadata.title.toString(),
        fontSize = 20.sp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(10.dp)
          .basicMarquee(),
        fontWeight = FontWeight.Medium,
        maxLines = 1,
      )

      Spacer(modifier = Modifier.height(15.dp))

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
      ) {
        Text(
          text = currentMusicPosition.toInt().convertMilliSecondToTime(),
          fontSize = 13.sp,
          fontWeight = FontWeight.Normal,
        )
        Text(
          text = currentMusicState.metadata.extras?.getInt("Duration")?.convertMilliSecondToTime()
            ?: "--:--",
          fontSize = 13.sp,
          fontWeight = FontWeight.Normal,
        )
      }
      Slider(
        value = currentMusicPosition.toFloat(),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 15.dp),
        onValueChange = {
          onSeekTo.invoke(it.toLong())
        },
        valueRange = 0f..(currentMusicState.metadata.extras?.getInt("Duration")?.toFloat() ?: 0f),
      )

      Spacer(modifier = Modifier.height(15.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
      ) {

        Button(
          onClick = { onMovePreviousMusic.invoke() },
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
          Image(
            painter = painterResource(id = R.drawable.next),
            contentDescription = "",
            modifier = Modifier
              .size(25.dp)
              .rotate(180f)
          )
        }

        Spacer(modifier = Modifier.width(15.dp))

        Button(
          onClick = {
            when (currentMusicState.isPlaying) {
              true -> onPauseMusic.invoke()
              false -> onResumeMusic.invoke()
            }
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
          AnimatedContent(
            targetState = if (currentMusicState.isPlaying) R.drawable.icon_pause else R.drawable.icon_play,
            label = ""
          ) {
            Image(
              painter = painterResource(id = it),
              contentDescription = "",
              modifier = Modifier.size(55.dp),
            )
          }
        }

        Spacer(modifier = Modifier.width(15.dp))

        Button(
          onClick = {
            onMoveNextMusic.invoke()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
          Image(
            painter = painterResource(id = R.drawable.next),
            contentDescription = "",
            modifier = Modifier.size(25.dp)
          )
        }

      }
    }
  }
}
