package com.example.mediaplayerjetpackcompose.domain.model

import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.SortTypeModel

data class SortState(
  val sortType:SortTypeModel,
  val isDec:Boolean,
)
