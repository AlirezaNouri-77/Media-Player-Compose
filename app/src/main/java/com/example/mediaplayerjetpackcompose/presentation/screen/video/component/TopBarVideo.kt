package com.example.mediaplayerjetpackcompose.presentation.screen.video.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarVideo(
  modifier: Modifier = Modifier,
  onBackClick: () -> Unit,
) {

  ElevatedCard(
    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 10.dp,
    )
  ) {
    TopAppBar(
      title = {
        Text(
          text = "Video",
          modifier = Modifier,
          fontWeight = FontWeight.Bold,
          fontSize = 36.sp,
        )
      },
      navigationIcon = {
        IconButton(
          modifier = Modifier
            .size(35.dp),
          onClick = { onBackClick.invoke() },
        ) {
          Icon(
            painter = painterResource(id = R.drawable.icon_back_24),
            contentDescription = "",
          )
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
      )
    )
  }

}