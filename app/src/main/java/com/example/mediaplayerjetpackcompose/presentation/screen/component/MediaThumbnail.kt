package com.example.mediaplayerjetpackcompose.presentation.screen.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mediaplayerjetpackcompose.R

@Composable
fun MediaThumbnail(
  modifier: Modifier = Modifier,
  uri: Uri?,
  size: Dp,
  roundCornerSize: Dp = 10.dp,
) {

  val color = MaterialTheme.colorScheme.onPrimary
  var isErrorOrLoading by remember { mutableStateOf(false) }
  val colorFilter = remember(isErrorOrLoading) { if (isErrorOrLoading) color else null }
  val contentScale = if (isErrorOrLoading) ContentScale.Inside else ContentScale.Fit

  AsyncImage(
    model = uri,
    contentDescription = "",
    modifier = modifier
      .size(size)
      .clip(RoundedCornerShape(roundCornerSize))
      .background(color = MaterialTheme.colorScheme.primary),
    colorFilter = colorFilter?.let { ColorFilter.tint(it) },
    contentScale = contentScale,
    error = painterResource(id = R.drawable.icon_music_note),
    onSuccess = { isErrorOrLoading = false },
    onError = { isErrorOrLoading = true },
    onLoading = { isErrorOrLoading = true }
  )

}