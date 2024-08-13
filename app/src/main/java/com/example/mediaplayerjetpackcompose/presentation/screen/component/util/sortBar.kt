package com.example.mediaplayerjetpackcompose.presentation.screen.component.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.domain.model.SortState
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.SortTypeModel

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.sortBar(
  sortState: SortState,
  onSortClick: (sortType: SortTypeModel) -> Unit,
  onDecClick: () -> Unit,
) {

  stickyHeader {
    Icon(
      painter = painterResource(
        id = if ((sortState.isDec)) {
          R.drawable.icon_sort_desc
        } else {
          R.drawable.icon_sort_asce
        }
      ),
      contentDescription = "",
      modifier = Modifier
        .padding(5.dp)
        .size(25.dp)
        .clickable { onDecClick.invoke() },
    )
    Spacer(modifier = Modifier.width(10.dp))
  }
  itemsIndexed(items = SortTypeModel.entries.filter { it != SortTypeModel.NAME }) { _, sort ->
    Text(
      text = sort.sortName,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier
        .clickable {
          onSortClick.invoke(if (sortState.sortType == sort) SortTypeModel.NAME else sort)
        }
        .background(
          color = if (sortState.sortType == sort)  Color(0xFF2A2D2C) else Color.Transparent,
          shape = RoundedCornerShape(8.dp),
        )
        .padding(vertical = 4.dp, horizontal = 4.dp),
    )
    Spacer(modifier = Modifier.width(4.dp))
  }
}