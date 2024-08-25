package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarPosition
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.MyTabIndicator
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.myCustomTabIndicator

@Composable
fun TabBarSection(
  modifier: Modifier = Modifier,
  currentTabState: TabBarPosition,
  onTabClick: (TabBarPosition, Int) -> Unit,
) {
  val indicator = @Composable { tabPosition: List<TabPosition> ->
    MyTabIndicator(
      Modifier.myCustomTabIndicator(tabPosition[TabBarPosition.entries.indexOf(currentTabState)])
    )
  }
  Card(
    modifier
      .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
    shape = RoundedCornerShape(bottomEnd = 25.dp, bottomStart = 25.dp)
  ) {
    ScrollableTabRow(
      selectedTabIndex = TabBarPosition.entries.indexOf(TabBarPosition.MUSIC),
      edgePadding = 10.dp,
      indicator = indicator,
      contentColor = MaterialTheme.colorScheme.onPrimary,
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      divider = {}
    ) {
      TabBarPosition.entries.forEach {
        Tab(
          modifier = Modifier,
          text = {
            Text(
              text = it.enuName,
              fontSize = 13.sp,
              fontWeight = FontWeight.Medium,
            )
          },
          selected = currentTabState == it,
          onClick = { onTabClick.invoke(it, TabBarPosition.entries.indexOf(currentTabState)) },
          interactionSource = NoRippleEffect
        )
      }
    }
  }

}