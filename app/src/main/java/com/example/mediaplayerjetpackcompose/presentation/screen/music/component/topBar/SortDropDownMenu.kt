package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState

@Composable
fun SortDropDownMenu(
  modifier: Modifier = Modifier,
  isExpand: Boolean,
  offset: DpOffset,
  onDismiss: () -> Unit,
  sortState: SortState,
  onSortClick: (SortTypeModel) -> Unit,
  onOrderClick: () -> Unit,
) {

  val orderIcon = remember(sortState.isDec) {
    if (sortState.isDec) R.drawable.icon_sort_desc else R.drawable.icon_sort_asce
  }

  Box(
    modifier = modifier
      .wrapContentSize(Alignment.TopEnd)
  ) {
    DropdownMenu(
      expanded = isExpand,
      offset = offset,
      onDismissRequest = { onDismiss() },
    ) {
      SortTypeModel.entries.forEachIndexed { _, sortBarModel ->
        DropdownMenuItem(
          text = {
            Text(text = sortBarModel.sortName)
          },
          onClick = {
            onSortClick(sortBarModel)
          },
          colors = MenuDefaults.itemColors(
            textColor = MaterialTheme.colorScheme.onPrimary,
          ),
          trailingIcon = {
            if (sortState.sortType == sortBarModel) {
              Icon(painter = painterResource(id = R.drawable.icon_tick), contentDescription = "")
            }
          }
        )
      }
      HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f))
      DropdownMenuItem(
        text = {
          Text(text = if (sortState.isDec) "Dec" else "Acs")
        },
        trailingIcon = {
          Icon(painter = painterResource(id = orderIcon), contentDescription = "")
        },
        onClick = { onOrderClick() },
      )
    }
  }


}
