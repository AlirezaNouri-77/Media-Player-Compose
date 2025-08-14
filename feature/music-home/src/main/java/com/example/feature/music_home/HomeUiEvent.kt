package com.example.feature.music_home

import com.example.feature.music_home.model.TabBarModel
import com.example.core.model.datastore.SortType

sealed interface HomeUiEvent {
  object HideSortDropDownMenu : HomeUiEvent
  object ShowSortDropDownMenu : HomeUiEvent
  data class UpdateSortState(val tabBarPosition: TabBarModel, val sortType: SortType) : HomeUiEvent
  data class UpdateSortOrder(val tabBarPosition: TabBarModel) : HomeUiEvent
  data class UpdateTabBarPosition(val tabBarPosition: TabBarModel) : HomeUiEvent
}