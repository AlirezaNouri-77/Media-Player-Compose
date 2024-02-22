package com.example.mediaplayerjetpackcompose.presentation.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// used for album and artist tabview
@Composable
fun FolderStyleMusicItem(
  name: String,
  musicListSize: Int,
  onClick: (String) -> Unit,
) {
  Surface(onClick = { onClick.invoke(name) }) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp, horizontal = 10.dp),
    ) {
      Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
      Text(text = "$musicListSize Music", fontSize = 15.sp, fontWeight = FontWeight.Medium)
    }
  }
}