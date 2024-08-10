package com.example.mediaplayerjetpackcompose.presentation.screen.music.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryListItem(
  categoryName: String,
  musicListSize: Int,
  onClick: (String) -> Unit,
) {
  Surface(
    onClick = { onClick.invoke(categoryName) },
    color = Color.Transparent
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp, horizontal = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start,
    ) {
      Spacer(modifier = Modifier.width(10.dp))
      Column {
        Text(text = categoryName, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, maxLines = 2)
        Text(text = "$musicListSize Music", fontSize = 15.sp, fontWeight = FontWeight.Medium)
      }
    }
  }
}