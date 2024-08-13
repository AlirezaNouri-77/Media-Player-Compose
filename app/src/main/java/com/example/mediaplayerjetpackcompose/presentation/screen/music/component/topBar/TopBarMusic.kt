package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.resource.bitmap.UnitBitmapDecoder
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.SortState
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.TabBarPosition
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.NoRippleEffect
import com.example.mediaplayerjetpackcompose.presentation.screen.component.util.sortBar
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun TopBarMusic(
  currentTabPosition: TabBarPosition,
  onSearch: (String) -> Unit,
  onVideoIconClick: () -> Unit,
  onSortIconClick: () -> Unit,
  sortIconOffset: (DpOffset) -> Unit,
  density: Density,
) {

  //var showSortBar by remember { mutableStateOf(false) }
  var showSearch by remember { mutableStateOf(false) }

  val textFieldValue = remember {
    mutableStateOf("")
  }

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

  Column(
    Modifier
      .fillMaxWidth()
      .animateContentSize()
      .background(color = MaterialTheme.colorScheme.primary)
  ) {

    TopBarSection(
      currentTabPosition = currentTabPosition,
      isSearchSectionShow = showSearch,
      density = density,
      onVideoIconClick = { onVideoIconClick() },
      onSearchIconClick = { showSearch = !showSearch },
      onSortIconClick = { onSortIconClick() },
      sortIconOffset = { sortIconOffset(it) },
    )

    AnimatedVisibility(
      visible = showSearch,
    ) {
      SearchSection(
        textFieldValue.value,
        onTextFieldChange = {
          textFieldValue.value = it
        },
      )
    }

  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSection(
  currentTabPosition: TabBarPosition,
  isSearchSectionShow: Boolean,
  onSortIconClick: () -> Unit,
  onSearchIconClick: () -> Unit,
  onVideoIconClick: () -> Unit,
  sortIconOffset: (DpOffset) -> Unit,
  density: Density,
) {

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
        Row {
          if (!isSearchSectionShow) {
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
                onClick = { onSearchIconClick.invoke() },
              ),
            tint = MaterialTheme.colorScheme.onPrimary,
          )
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
    )
  )

}

@Composable
fun SearchSection(
  textFieldValue: String,
  onTextFieldChange: (String) -> Unit,
) {
  TextField(
    value = textFieldValue,
    onValueChange = { value ->
      onTextFieldChange.invoke(value)
    },
    singleLine = true,
    maxLines = 1,
    modifier = Modifier
      .fillMaxWidth()
      .height(50.dp)
      .padding(horizontal = 15.dp),
    trailingIcon = {
      if (textFieldValue.isNotEmpty()) {
        Icon(
          imageVector = Icons.Rounded.Clear,
          contentDescription = "Clear Search Field",
          modifier = Modifier
            .size(20.dp)
            .clickable {
              onTextFieldChange.invoke("")
            },
        )
      }
    },
    shape = RoundedCornerShape(15.dp),
    colors = TextFieldDefaults.colors(
      focusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
      unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
      focusedTextColor = MaterialTheme.colorScheme.onPrimary,
      cursorColor = MaterialTheme.colorScheme.onPrimary,
      focusedLabelColor = Color.Transparent,
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent,
      disabledIndicatorColor = Color.Transparent
    )
  )
}

@Composable
private fun SortSection(
  sortState: SortState,
  onSortClick: (SortTypeModel) -> Unit,
  onOrderClick: () -> Unit,
) {
  AnimatedVisibility(
    visible = sortState.isDec,
    enter = fadeIn(tween(300, 30)) + slideInVertically(
      animationSpec = tween(200),
      initialOffsetY = { int -> int / 2 }),
    exit = slideOutVertically(
      animationSpec = tween(300),
      targetOffsetY = { int -> -int / 2 }) + fadeOut(tween(200, 30))
  ) {
    LazyRow(
      modifier = Modifier
        .padding(vertical = 10.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
    ) {
      sortBar(
        sortState = sortState,
        onSortClick = { onSortClick(it) },
        onDecClick = { onOrderClick() },
      )
    }
  }

}