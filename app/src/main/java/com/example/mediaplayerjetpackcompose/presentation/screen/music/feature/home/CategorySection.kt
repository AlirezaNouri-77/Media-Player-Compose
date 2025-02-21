package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@Composable
fun CategorySection(
  modifier: Modifier = Modifier,
  currentTabState: TabBarModel,
  onTabClick: (TabBarModel, Int) -> Unit,
) {

  Row(
    modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally)
  ) {
    TabBarModel.entries.forEachIndexed { index, item ->
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .fillMaxWidth()
          .weight(0.5f)
          .background(
            color = if (currentTabState.id == index) {
              if (isSystemInDarkTheme()) Color.White else Color.Black
            } else MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(10.dp),
          )
          .clickable(
            onClick = {
              onTabClick(item, index)
            },
          )

      ) {
        Text(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
          text = item.enuName,
          color = if (currentTabState.id == index) {
            if (isSystemInDarkTheme()) Color.Black else Color.White
          } else MaterialTheme.colorScheme.onSecondary,
          fontSize = 14.sp,
          fontWeight = FontWeight.SemiBold,
        )
      }
    }
  }

}

@Preview(showBackground = true)
@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
  name = "Dark", showBackground = true
)
@Composable
private fun Preview() {
  MediaPlayerJetpackComposeTheme {
    Surface(color = MaterialTheme.colorScheme.background) {
      CategorySection(
        currentTabState = TabBarModel.All,
        onTabClick = { _, _ -> },
      )
    }
  }
}