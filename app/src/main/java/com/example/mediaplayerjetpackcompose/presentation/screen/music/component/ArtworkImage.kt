package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.forwardingPainterCoil

@Composable
fun ArtworkImage(
  modifier: Modifier = Modifier,
  uri: () -> Uri?,
  verticalInset: Float,
  horizontalInset: Float,
) {

  val isDark = isSystemInDarkTheme()
  val imageBackgroundAlpha = remember(isSystemInDarkTheme()) {
    if (isDark) Color.DarkGray else Color.Black
  }

  AsyncImage(
    model = uri(),
    contentDescription = "",
    contentScale = ContentScale.FillBounds,
    modifier = modifier
      .background(color = imageBackgroundAlpha),
    placeholder = forwardingPainterCoil(
      painter = painterResource(id = R.drawable.icon_music_note),
      colorFilter = ColorFilter.tint(Color.White),
      onDraw = { info ->
        inset(horizontal = horizontalInset, vertical = verticalInset) {
          with(info.painter) {
            draw(size, info.alpha, info.colorFilter)
          }
        }
      },
    ),
    error = forwardingPainterCoil(
      painter = painterResource(id = R.drawable.icon_music_note),
      colorFilter = ColorFilter.tint(Color.White),
      onDraw = { info ->
        inset(horizontal = horizontalInset, vertical = verticalInset) {
          with(info.painter) {
            draw(size, info.alpha, info.colorFilter)
          }
        }
      },
    ),
  )

}