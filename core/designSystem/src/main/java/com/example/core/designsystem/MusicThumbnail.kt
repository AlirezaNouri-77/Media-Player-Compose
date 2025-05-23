package com.example.core.designsystem

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.SubcomposeAsyncImage

@Composable
fun MusicThumbnail(
  modifier: Modifier = Modifier,
  uri: Uri?,
) {

  val isDark = isSystemInDarkTheme()
  val imageBackgroundAlpha = remember(isSystemInDarkTheme()) {
    if (isDark) Color.DarkGray else Color.Black
  }

  SubcomposeAsyncImage(
    model = uri,
    contentDescription = "",
    contentScale = ContentScale.FillBounds,
    modifier = modifier
      .background(color = imageBackgroundAlpha),
    loading = {
      PlaceHolder(modifier = Modifier.matchParentSize())
    },
    error = {
      PlaceHolder(modifier = Modifier.matchParentSize())
    }
  )

}

@Composable
private fun PlaceHolder(modifier: Modifier = Modifier) {
  Box(modifier = modifier, contentAlignment = Alignment.Center) {
    Image(
      painter = painterResource(R.drawable.icon_music_note),
      colorFilter = ColorFilter.tint(Color.White),
      contentDescription = ""
    )
  }
}