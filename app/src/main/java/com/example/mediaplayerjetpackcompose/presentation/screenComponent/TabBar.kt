package com.example.mediaplayerjetpackcompose.presentation.screenComponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.Constant
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.MyTabIndicator
import com.example.mediaplayerjetpackcompose.presentation.screen.component.myCustomTabIndicator
import com.example.mediaplayerjetpackcompose.presentation.util.NoRippleEffect


@Composable
fun TabBar(
  musicPageViewModel: MusicPageViewModel,
  tabItemList: List<String> = Constant.tabBarListItem,
  currentTabPosition: Int,
  onTabClick: (Int) -> Unit,
  onSortIconClick: () -> Unit,
) {
  val indicator = @Composable { tabPosition: List<TabPosition> ->
    MyTabIndicator(
      Modifier.myCustomTabIndicator(tabPosition[musicPageViewModel.currentTabState])
    )
  }
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Absolute.SpaceBetween,
    ) {
      Text(
        text = "Music",
        modifier = Modifier
          .padding(10.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
      )
      AnimatedVisibility(visible = currentTabPosition == 0) {
        Image(
          painter = painterResource(id = R.drawable.icon_sort),
          contentDescription = "Sort Icon",
          modifier = Modifier
            .padding(10.dp)
            .size(30.dp)
            .clickable {
              onSortIconClick.invoke()
            },
        )
      }
    }
    Spacer(modifier = Modifier.height(10.dp))
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
            onTabClick.invoke(index)
            musicPageViewModel.currentTabState = index
          },
          interactionSource = NoRippleEffect
        )
      }
    }
  }

}