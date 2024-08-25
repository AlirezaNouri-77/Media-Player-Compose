package com.example.mediaplayerjetpackcompose.domain.model.share

import com.example.mediaplayerjetpackcompose.domain.model.musicSection.SortTypeModel

data class SortState(
  val sortType:SortTypeModel,
  val isDec:Boolean,
)
