package com.example.mediaplayerjetpackcompose.presentation.screenComponent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MyNewIndicator
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.myCustomTabIndicator


@Composable
fun TabBar(
  musicPageViewModel: MusicPageViewModel,
  tabItemList: List<String> = listOf("Music", "Artist", "Album"),
) {
  val indicator = @Composable { tabPosition: List<TabPosition> ->
    MyNewIndicator(
      Modifier.myCustomTabIndicator(tabPosition[musicPageViewModel.currentTabState])
    )
  }
  TabRow(
    selectedTabIndex = musicPageViewModel.currentTabState,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 30.dp),
    indicator = indicator,
    contentColor = Color.Black,
    divider = {}
  ) {
    tabItemList.forEachIndexed { index, string ->
      Tab(
        text = {
          Text(
            text = string,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
          )
        },
        selected = musicPageViewModel.currentTabState == index,
        onClick = {
          musicPageViewModel.currentTabState = index
        },
      )
    }
  }
}