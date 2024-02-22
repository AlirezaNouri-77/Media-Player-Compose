package com.example.mediaplayerjetpackcompose.presentation.screenComponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.Constant
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import com.example.mediaplayerjetpackcompose.presentation.screen.component.MyTabIndicator
import com.example.mediaplayerjetpackcompose.presentation.screen.component.myCustomTabIndicator
import com.example.mediaplayerjetpackcompose.presentation.screen.component.sortBar
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.util.NoRippleEffect
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun TopBarMusic(
  musicPageViewModel: MusicPageViewModel,
  showSortBar: Boolean,
  showSearch: Boolean,
  currentTabPosition: Int,
  onSortIconClick: () -> Unit,
  onSearchIconClick: () -> Unit,
  onTabBarClick: (Int) -> Unit,
) {

  val textFieldValue = remember {
    mutableStateOf("")
  }
  LaunchedEffect(key1 = textFieldValue.value, block = {
    snapshotFlow { textFieldValue }
      .debounce(500)
      .collectLatest {
        musicPageViewModel.searchMusic(it.value)
      }
  })

  Surface (
    color = MaterialTheme.colorScheme.primary,
  ) {
    Column {
      TopBarSection(
        currentTabPosition = currentTabPosition,
        isSearchSectionShow = !showSearch,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        onSortIconClick = { onSortIconClick.invoke() },
        onSearchIconClick = { onSearchIconClick.invoke() },
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
      AnimatedVisibility(
        visible = !showSearch,
      ) {
        TabBarSection(
          currentTabState = musicPageViewModel.currentTabState,
          onTabClick = {
            onTabBarClick.invoke(it)
            musicPageViewModel.currentTabState = it
          }
        )
      }
      SortSection(
        musicPageViewModel = musicPageViewModel,
        showSortBar = showSortBar,
      )
      Spacer(modifier = Modifier.height(10.dp))
    }
  }

}

@Composable
fun TopBarSection(
  currentTabPosition: Int,
  contentColor:Color,
  isSearchSectionShow: Boolean,
  onSortIconClick: () -> Unit,
  onSearchIconClick: () -> Unit,
) {

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
      Row {
        if (isSearchSectionShow) {
          Icon(
            painter = painterResource(id = R.drawable.icon_sort),
            contentDescription = "Sort Icon",
            modifier = Modifier
              .padding(10.dp)
              .size(30.dp)
              .clickable {
                onSortIconClick.invoke()
              },
            tint = contentColor,
          )
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
          tint = contentColor,
        )
      }
    }
  }

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
      focusedTextColor = Color.Black,
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
  musicPageViewModel: MusicPageViewModel,
  showSortBar: Boolean,
) {
  AnimatedVisibility(
    visible = showSortBar,
    enter = fadeIn(tween(300, 100)) + slideInVertically(
      animationSpec = tween(300),
      initialOffsetY = { int -> int / 4 }),
    exit = slideOutVertically(
      animationSpec = tween(300, 100),
      targetOffsetY = { int -> int / 4 }) + fadeOut(tween(300, 100))
  ) {
    LazyRow(
      modifier = Modifier
        .padding(horizontal = 15.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
    ) {
      sortBar(
        musicPageViewModel = musicPageViewModel,
        onSortClick = {
          musicPageViewModel.currentListSort.value = it
          musicPageViewModel.sortMusicListByCategory(
            list = musicPageViewModel.musicList
          ).also { resultList ->
            musicPageViewModel.musicList =
              resultList as SnapshotStateList<MusicMediaModel>
          }
        },
        onDecClick = {
          musicPageViewModel.isDec.value = !musicPageViewModel.isDec.value
          musicPageViewModel.sortMusicListByCategory(
            list = musicPageViewModel.musicList
          ).also { resultList ->
            musicPageViewModel.musicList =
              resultList as SnapshotStateList<MusicMediaModel>
          }
        }
      )
    }
  }

}

@Composable
fun TabBarSection(
  currentTabState: Int,
  onTabClick: (Int) -> Unit,
) {
  val indicator = @Composable { tabPosition: List<TabPosition> ->
    MyTabIndicator(
      Modifier.myCustomTabIndicator(tabPosition[currentTabState])
    )
  }
  TabRow(
    selectedTabIndex = currentTabState,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 30.dp),
    indicator = indicator,
    contentColor = MaterialTheme.colorScheme.onPrimary,
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    divider = {}
  ) {
    Constant.tabBarListItem.forEachIndexed { index, string ->
      Tab(
        text = {
          Text(
            text = string,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
          )
        },
        selected = currentTabState == index,
        onClick = {
          onTabClick.invoke(index)
        },
        interactionSource = NoRippleEffect
      )
    }
  }
}