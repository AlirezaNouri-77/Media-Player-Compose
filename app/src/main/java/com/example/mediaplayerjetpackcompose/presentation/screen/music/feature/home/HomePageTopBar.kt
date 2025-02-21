package com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarModel
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar.SortDropDownMenu
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun HomePageTopBar(
  modifier: Modifier = Modifier,
  currentTabPosition: TabBarModel,
  onVideoIconClick: () -> Unit,
  onSortIconClick: () -> Unit,
  isDropDownMenuSortExpand: Boolean,
  onDismissDropDownMenu: () -> Unit,
  sortState: () -> SortState,
  onSortClick: (SortTypeModel) -> Unit,
  onOrderClick: () -> Unit,
) {

  TopAppBar(
    modifier = modifier.fillMaxWidth(),
    title = {
      Text(
        text = "Home",
        modifier = Modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp,
      )
    },
    actions = {

      Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center,
      ) {
        AnimatedContent(targetState = currentTabPosition == TabBarModel.All, label = "") { boolean ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            IconButton(
              onClick = { onVideoIconClick.invoke() },
            ) {
              Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.icon_video),
                contentDescription = "video Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
              )
            }
            if (boolean) {
              Box(
                modifier = Modifier
                  .wrapContentSize(Alignment.TopEnd)
              ) {
                IconButton(
                  onClick = { onSortIconClick.invoke() },
                ) {
                  Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.icon_sort),
                    contentDescription = "Sort Icon",
                    tint = MaterialTheme.colorScheme.onPrimary,
                  )
                }
                SortDropDownMenu(
                  isExpand = isDropDownMenuSortExpand,
                  onDismiss = { onDismissDropDownMenu() },
                  sortState = sortState(),
                  onSortClick = { onSortClick(it) },
                  onOrderClick = { onOrderClick() }
                )
              }
            }

          }
        }

      }

    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = Color.Transparent,
    )
  )

}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun PreviewTopBarMusic() {
  MediaPlayerJetpackComposeTheme {
    HomePageTopBar(
      currentTabPosition = TabBarModel.All,
      onVideoIconClick = {},
      onSortIconClick = {},
      isDropDownMenuSortExpand = false,
      sortState = {
        SortState(
          SortTypeModel.SIZE,
          false,
        )
      },
      onSortClick = {},
      onOrderClick = {},
      onDismissDropDownMenu = {},
    )
  }
}

