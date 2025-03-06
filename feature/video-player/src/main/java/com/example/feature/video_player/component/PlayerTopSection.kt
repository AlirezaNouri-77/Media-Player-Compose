package com.example.feature.video_player.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.video.R

@Composable
fun PlayerTopSection(
  modifier: Modifier = Modifier,
  controllerLayoutPadding: PaddingValues,
  onBack: () -> Unit,
  title: String,
) {
  Row(
    modifier = modifier
      .background(Color.Transparent)
      .fillMaxWidth()
      .drawBehind {
        drawRoundRect(
          color = Color.Black,
          size = this.size,
          alpha = 0.4f,
          cornerRadius = CornerRadius(25f, 25f),
        )
      }
      .padding(controllerLayoutPadding),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(15.dp),
  ) {
    Image(
      painter = painterResource(id = R.drawable.icon_back_24), contentDescription = "",
      modifier = Modifier
        .padding(start = 15.dp)
        .weight(0.1f)
        .clickable {
          onBack()
        },
    )
    Text(
      text = title,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
      color = Color.White,
      maxLines = 1,
      modifier = Modifier
        .fillMaxWidth()
        .basicMarquee()
        .padding(vertical = 5.dp)
        .weight(0.9f),
    )
  }
}