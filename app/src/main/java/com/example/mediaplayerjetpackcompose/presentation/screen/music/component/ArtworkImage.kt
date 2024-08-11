package com.example.mediaplayerjetpackcompose.presentation.screen.music.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
  inset: Float,
) {
  AsyncImage(
    model = uri(),
    contentDescription = "",
    contentScale = ContentScale.FillBounds,
    modifier = modifier
      .background(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f)),
    placeholder = forwardingPainterCoil(
      painter = painterResource(id = R.drawable.icon_music_note),
      colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
      onDraw = { info ->
        inset(inset, inset) {
          with(info.painter) {
            draw(size, info.alpha, info.colorFilter)
          }
        }
      },
    ),
    error = forwardingPainterCoil(
      painter = painterResource(id = R.drawable.icon_music_note),
      colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
      onDraw = { info ->
        inset(inset, inset) {
          with(info.painter) {
            draw(size, info.alpha, info.colorFilter)
          }
        }
      },
    ),
  )

}