package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen.SortItem

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.sortBar(
  musicPageViewModel: MusicPageViewModel,
  onSortClick: (sort:SortItem) -> Unit,
  onDec: () -> Unit,
) {
  stickyHeader {
    Text(
      text = "Dec",
      fontSize = 15.sp,
      fontWeight = FontWeight.Medium,
      color = if (musicPageViewModel.isDec.value) Color.Black else Color.Black.copy(alpha = 0.5f),
      modifier = Modifier
        .padding(vertical = 2.dp)
        .clickable {
          onDec.invoke()
        },
    )
    Spacer(modifier = Modifier.width(15.dp))
  }
  itemsIndexed(items = SortItem.entries.filter { it != SortItem.NAME }) { _, sort ->
    Text(
      text = sort.sortName,
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier
        .clickable {
          onSortClick.invoke(if (musicPageViewModel.currentListSort.value==sort) SortItem.NAME else sort)
        }
        .background(
          color = if (musicPageViewModel.currentListSort.value == sort) Color.Gray else Color.White,
          shape = RoundedCornerShape(8.dp),
        )
        .padding(vertical = 2.dp, horizontal = 3.dp),
    )
    Spacer(modifier = Modifier.width(4.dp))
  }
}