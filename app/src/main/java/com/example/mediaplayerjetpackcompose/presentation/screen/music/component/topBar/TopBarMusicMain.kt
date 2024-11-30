package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarPosition
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun TopBarMusic(
  modifier: Modifier = Modifier,
  currentTabPosition: TabBarPosition,
  onSearch: (String) -> Unit,
  onVideoIconClick: () -> Unit,
  onSortIconClick: () -> Unit,
  isDropDownMenuSortExpand: Boolean,
  onDismiss: () -> Unit,
  sortState: () -> SortState,
  onSortClick: (SortTypeModel) -> Unit,
  onOrderClick: () -> Unit,
) {

  var showSearch by remember { mutableStateOf(false) }
  val textFieldValue = remember { mutableStateOf("") }

  LaunchedEffect(
    key1 = textFieldValue.value,
    block = {
      snapshotFlow { textFieldValue }
        .debounce(500)
        .collectLatest {
          onSearch(it.value.trim())
        }
    },
  )

  TopAppBar(
    modifier = modifier.fillMaxWidth(),
    title = {
      AnimatedVisibility(
        visible = !showSearch,
        enter = fadeIn(tween(100, 360, LinearEasing)) + slideInHorizontally(tween(150, 360)) { it / 2 },
        exit = fadeOut(tween(80)),
        label = "Title AnimatedVisibility"
      ) {
        Text(
          text = "Music",
          modifier = Modifier,
          fontWeight = FontWeight.Bold,
          fontSize = 38.sp,
        )
      }
    },
    actions = {

      AnimatedVisibility(
        visible = currentTabPosition == TabBarPosition.MUSIC,
        exit = fadeOut(),
        enter = fadeIn(),
        label = "Actions AnimatedVisibility"
      ) {

        AnimatedVisibility(
          visible = showSearch,
          enter = slideInHorizontally(tween(150, 80, LinearEasing)) { it * 2 },
          exit = slideOutHorizontally(tween(150, 80, LinearEasing)) { it * 2 },
          label = "Search AnimatedVisibility"
        ) {
          SearchSection(
            textFieldValue.value,
            onTextFieldChange = { textFieldValue.value = it },
            onDismiss = {
              textFieldValue.value = ""
              showSearch = false
            },
          )
        }

        AnimatedVisibility(
          visible = !showSearch,
          enter = fadeIn(tween(100, 360, LinearEasing)) + slideInHorizontally(tween(150, 360)) { -it },
          exit = slideOutHorizontally(tween(50, 0, FastOutLinearInEasing)) { -it },
          label = "ActionRow AnimatedVisibility"
        ) {
          Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center,
          ) {
            Row(
              modifier = Modifier,
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
                  onDismiss = { onDismiss() },
                  sortState = sortState(),
                  onSortClick = { onSortClick(it) },
                  onOrderClick = { onOrderClick() }
                )
              }
              IconButton(
                onClick = { showSearch = true },
              ) {
                Icon(
                  painter = painterResource(id = R.drawable.icon_search_24),
                  contentDescription = "Search Icon",
                  modifier = Modifier
                    .size(24.dp),
                  tint = MaterialTheme.colorScheme.onPrimary,
                )
              }
            }
          }
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
    )
  )

}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun PreviewTopBarMusic() {
  MediaPlayerJetpackComposeTheme {
    TopBarMusic(
      currentTabPosition = TabBarPosition.MUSIC,
      onSearch = {},
      onVideoIconClick = {},
      onSortIconClick = {},
      isDropDownMenuSortExpand = false,
      onDismiss = {},
      sortState = {
        SortState(
          SortTypeModel.SIZE,
          false,
        )
      },
      onSortClick = {},
      onOrderClick = {},
    )
  }
}

