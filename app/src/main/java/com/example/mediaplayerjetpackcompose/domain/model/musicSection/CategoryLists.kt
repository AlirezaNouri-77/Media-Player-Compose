package com.example.mediaplayerjetpackcompose.domain.model.musicSection

data class CategoryLists(
  var artist: List<CategoryMusicModel>,
  var album: List<CategoryMusicModel>,
  var folder: List<CategoryMusicModel>,
) {
  companion object {
    var Empty = CategoryLists(emptyList(), emptyList(), emptyList())
  }
}
