package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect

@Composable
fun HeaderSection(
  modifier: Modifier = Modifier,
  onBackClick: () -> Unit,
) {
  Row(
    modifier = modifier.fillMaxWidth().wrapContentHeight(),
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Button(
      modifier = Modifier
        .size(40.dp),
      onClick = { onBackClick.invoke() },
      interactionSource = NoRippleEffect,
      colors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = Color.White,
      ),
      contentPadding = PaddingValues(0.dp)
    ) {
      Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = "",
        tint = Color.White,
      )
    }
    Spacer(modifier = Modifier.width(30.dp))
    Text(
      text = "Now Playing",
      modifier = Modifier,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
      color = Color.White,
    )
  }
}