package com.example.core.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import com.example.core.model.FolderSortType
import com.example.core.model.SongsSortType
import com.example.core.model.SortType

@Composable
fun Sort(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  onSortClick: (SortType) -> Unit,
  onOrderClick: () -> Unit,
  isDropDownMenuSortExpand: Boolean,
  isOrderDec: Boolean,
  sortType: SortType,
  onDismissDropDownMenu: () -> Unit,
) {
  Box(
    modifier = modifier
      .wrapContentSize(Alignment.TopEnd)
  ) {
    IconButton(
      onClick = { onClick() },
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
      sortTypeList = SongsSortType.entries.toList(),
      isSortDescending = isOrderDec,
      currentSortType = sortType,
      onSortClick = { onSortClick(it) },
      onOrderClick = { onOrderClick() },
      onDismiss = { onDismissDropDownMenu() },
    )
  }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
  MediaPlayerJetpackComposeTheme {
    var isDropDownMenuShow by remember { mutableStateOf(false) }
    Sort(
      modifier = Modifier.fillMaxWidth(),
      onClick = { isDropDownMenuShow = true },
      onSortClick = {},
      onOrderClick = {},
      isDropDownMenuSortExpand = isDropDownMenuShow,
      isOrderDec = false,
      sortType = SongsSortType.DURATION,
      onDismissDropDownMenu = { isDropDownMenuShow = false },
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
  MediaPlayerJetpackComposeTheme {
    var isDropDownMenuShow by remember { mutableStateOf(false) }
    Sort(
      modifier = Modifier.fillMaxWidth(),
      onClick = { isDropDownMenuShow = true },
      onSortClick = {},
      onOrderClick = {},
      isDropDownMenuSortExpand = isDropDownMenuShow,
      isOrderDec = false,
      sortType = FolderSortType.NAME,
      onDismissDropDownMenu = { isDropDownMenuShow = false },
    )
  }
}