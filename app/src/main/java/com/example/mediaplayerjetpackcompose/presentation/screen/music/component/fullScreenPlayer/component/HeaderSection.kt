package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderSection(
  modifier: Modifier = Modifier,
  onBackClick: () -> Unit,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight(),
    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    IconButton(
      modifier = Modifier
        .size(28.dp),
      onClick = { onBackClick.invoke() },
      colors = IconButtonDefaults.iconButtonColors(
        containerColor = Color.Transparent,
        contentColor = Color.White,
      ),
    ) {
      Icon(
        modifier = Modifier
          .fillMaxSize(),
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "",
      )
    }
    Text(
      text = "Now Playing",
      modifier = Modifier,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
      color = Color.White,
    )
  }
}