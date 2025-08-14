package com.example.feature.music_home.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.R

@Composable
fun VideoIconButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  IconButton(
    onClick = { onClick() },
  ) {
    Icon(
      modifier = modifier.size(24.dp),
      painter = painterResource(id = R.drawable.icon_video),
      contentDescription = "video Icon",
      tint = MaterialTheme.colorScheme.onPrimary,
    )
  }
}