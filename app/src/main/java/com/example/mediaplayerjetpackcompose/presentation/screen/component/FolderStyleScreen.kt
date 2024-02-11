package com.example.mediaplayerjetpackcompose.presentation.screen.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel

@Composable
fun FolderStyleScreen(
  dataList: Map<String, List<MusicMediaModel>>,
  onItemClick: (String) -> Unit,
) {
  LazyColumn {
    dataList.forEach { (key, list) ->
      item {
        FolderStyleMusicItem(
          name = key,
          musicListSize = list.size,
          onClick = { string ->
            onItemClick.invoke(string)
          },
        )
      }
    }
  }
}