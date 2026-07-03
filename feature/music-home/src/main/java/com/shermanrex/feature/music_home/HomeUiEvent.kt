package com.shermanrex.feature.music_home

import com.shermanrex.core.model.datastore.SortType
import com.shermanrex.feature.music_home.model.TabBarModel

sealed interface HomeUiEvent {
    object HideSortDropDownMenu : HomeUiEvent

    object ShowSortDropDownMenu : HomeUiEvent

    data class UpdateSortState(val tabBarPosition: TabBarModel, val sortType: SortType) : HomeUiEvent

    data class UpdateSortOrder(val tabBarPosition: TabBarModel) : HomeUiEvent

    data class UpdateTabBarPosition(val tabBarPosition: TabBarModel) : HomeUiEvent

    data class UpdateHomeScrollIndex(val index: Int) : HomeUiEvent

    data class UpdateFolderScrollIndex(val index: Int) : HomeUiEvent

    data class UpdateFavoriteScrollIndex(val index: Int) : HomeUiEvent
}
