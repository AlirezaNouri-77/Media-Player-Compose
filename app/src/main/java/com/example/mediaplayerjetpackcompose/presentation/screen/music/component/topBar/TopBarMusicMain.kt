package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
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
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun TopBarMusic(
  modifier: Modifier = Modifier,
  currentTabPosition: TabBarModel,
  onSearch: (String) -> Unit,
  onVideoIconClick: () -> Unit,
  onSortIconClick: () -> Unit,
  isDropDownMenuSortExpand: Boolean,
  isSearchShow: () -> Boolean,
  onDismissDropDownMenu: () -> Unit,
  sortState: () -> SortState,
  onSortClick: (SortTypeModel) -> Unit,
  onOrderClick: () -> Unit,
  onKeyboardFocusChange: (Boolean) -> Unit,
  onSearchClick: (Boolean) -> Unit,
  onDismissSearch: () -> Unit,
) {

  val searchTextFieldValue = rememberSaveable { mutableStateOf("") }
  val topBarColor = animateFloatAsState(
    if (isSearchShow()) 0f else 1f, label = "",
    animationSpec = tween(50)
  )

  LaunchedEffect(
    key1 = searchTextFieldValue.value,
    block = {
      snapshotFlow { searchTextFieldValue }
        .debounce(500)
        .collectLatest {
          onSearch(it.value.trim())
        }
    },
  )

  AnimatedVisibility(
    visible = isSearchShow(),
    enter = fadeIn(tween(100)) + slideInHorizontally(tween(150, 50, easing = LinearEasing)) { it },
    exit = slideOutHorizontally(tween(150, easing = LinearEasing)) { it } + fadeOut(tween(100,80)),
    label = "Search AnimatedVisibility"
  ) {
    SearchSection(
      textFieldValue = searchTextFieldValue.value,
      onTextFieldChange = { searchTextFieldValue.value = it },
      onDismiss = {
        onDismissSearch()
      },
      onKeyboardFocusChange = {
        onKeyboardFocusChange(it)
      },
      onClear = {
        searchTextFieldValue.value = ""
      }
    )
  }

  AnimatedVisibility(
    visible = !isSearchShow(),
    enter = fadeIn(tween(100)) + slideInHorizontally(tween(150, 80)) { -it },
    exit = slideOutHorizontally(tween(150, easing = LinearEasing)) { -it } + fadeOut(tween(100,80)),
    label = "TopAppBar AnimatedVisibility"
  ) {
    TopAppBar(
      modifier = modifier.fillMaxWidth(),
      title = {
        Text(
          text = "Music",
          modifier = Modifier,
          fontWeight = FontWeight.Bold,
          fontSize = 38.sp,
        )
      },
      actions = {

        if (currentTabPosition == TabBarModel.MUSIC) {
          Box(
            contentAlignment = Alignment.Center,
          ) {
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
              IconButton(
                onClick = { onSearchClick(true) },
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
      },
      colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = topBarColor.value),
      )
    )
  }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun PreviewTopBarMusic() {
  MediaPlayerJetpackComposeTheme {
    var isSearchShow = remember {
      mutableStateOf(false)
    }
    TopBarMusic(
      currentTabPosition = TabBarModel.MUSIC,
      onSearch = {},
      onVideoIconClick = {},
      onSortIconClick = {},
      isDropDownMenuSortExpand = false,
      onDismissDropDownMenu = { isSearchShow.value = false },
      sortState = {
        SortState(
          SortTypeModel.SIZE,
          false,
        )
      },
      onSortClick = {},
      onOrderClick = {},
      onKeyboardFocusChange = {},
      isSearchShow = { isSearchShow.value },
      onSearchClick = { isSearchShow.value = true },
      onDismissSearch = { isSearchShow.value = false },
    )
  }
}

