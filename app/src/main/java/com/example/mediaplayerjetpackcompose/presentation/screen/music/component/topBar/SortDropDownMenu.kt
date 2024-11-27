package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel
import com.example.mediaplayerjetpackcompose.domain.model.share.SortState

@Composable
fun SortDropDownMenu(
  modifier: Modifier = Modifier,
  isExpand: Boolean,
  onDismiss: () -> Unit,
  sortState: SortState,
  onSortClick: (SortTypeModel) -> Unit,
  onOrderClick: () -> Unit,
) {
  DropdownMenu(
    modifier = modifier,
    expanded = isExpand,
    shape = RoundedCornerShape(10.dp),
    onDismissRequest = { onDismiss() },
    containerColor = MaterialTheme.colorScheme.primaryContainer,
  ) {
    SortTypeModel.entries.forEachIndexed { _, sortBarModel ->
      DropdownMenuItem(
        modifier = Modifier.background(
          if (sortState.sortType == sortBarModel) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f) else Color.Transparent,
          shape = RoundedCornerShape(10.dp),
        ),
        text = {
          Text(text = sortBarModel.sortName)
        },
        onClick = {
          onSortClick(sortBarModel)
        },
        colors = MenuDefaults.itemColors(
          textColor = MaterialTheme.colorScheme.onPrimary,
        ),
      )
    }
    Spacer(Modifier.height(6.dp))
    DropdownMenuItem(
      text = {
        Text(text = if (sortState.isDec) "Dec" else "Acs")
      },
      trailingIcon = {
        Icon(
          painter = painterResource(id = if (sortState.isDec) R.drawable.icon_sort_desc else R.drawable.icon_sort_asce),
          contentDescription = ""
        )
      },
      colors = MenuDefaults.itemColors(
        textColor = MaterialTheme.colorScheme.onPrimary,
      ),
      onClick = { onOrderClick() },
    )
  }
}
