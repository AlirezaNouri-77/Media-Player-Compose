package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.TabBarPosition
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun TopBarMusic(
  currentTabPosition: TabBarPosition,
  onSearch: (String) -> Unit,
  onVideoIconClick: () -> Unit,
  onSortIconClick: () -> Unit,
  sortIconOffset: (DpOffset) -> Unit,
  density: Density = LocalDensity.current,
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
    title = {
      Text(
        text = "Music",
        modifier = Modifier
          .padding(10.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
      )
    },
    actions = {
      AnimatedVisibility(visible = currentTabPosition == TabBarPosition.MUSIC, enter = fadeIn(), exit = fadeOut()) {

        AnimatedVisibility(
          visible = showSearch,
          enter = fadeIn(),
          exit = fadeOut(),
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
          enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
          exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it }),
        ) {
          Row {
            if (!showSearch) {
              IconButton(
                modifier = Modifier
                  .padding(5.dp)
                  .size(35.dp),
                onClick = { onVideoIconClick.invoke() },
              ) {
                Icon(
                  painter = painterResource(id = R.drawable.icon_video),
                  contentDescription = "video Icon",
                  tint = MaterialTheme.colorScheme.onPrimary,
                )
              }
              IconButton(
                modifier = Modifier
                  .padding(5.dp)
                  .size(35.dp)
                  .onGloballyPositioned {
                    with(density) {
                      val dpOffset = DpOffset(
                        x = it.positionInRoot().x.toDp(),
                        y = it.positionInRoot().y.toDp(),
                      )
                      sortIconOffset(dpOffset)
                    }
                  },
                onClick = { onSortIconClick.invoke() },
              ) {
                Icon(
                  painter = painterResource(id = R.drawable.icon_sort),
                  contentDescription = "Sort Icon",
                  tint = MaterialTheme.colorScheme.onPrimary,
                )
              }

            }

            Icon(
              painter = painterResource(id = R.drawable.icon_search_24),
              contentDescription = "Search Icon",
              modifier = Modifier
                .padding(10.dp)
                .size(30.dp)
                .clickable(
                  interactionSource = NoRippleEffect,
                  indication = null,
                  onClick = { showSearch = true },
                ),
              tint = MaterialTheme.colorScheme.onPrimary,
            )
          }
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
    )
  )

}

@Preview(showBackground = true)
@Composable
private fun PreviewTopBarMusic() {
  MediaPlayerJetpackComposeTheme {
    TopBarMusic(
      currentTabPosition = TabBarPosition.MUSIC,
      onSearch = {},
      onVideoIconClick = {},
      onSortIconClick = {},
      sortIconOffset = {},
    )
  }
}

