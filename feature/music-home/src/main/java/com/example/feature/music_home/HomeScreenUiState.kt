package com.example.feature.music_home

import com.example.core.model.MusicModel
import com.example.core.model.PlayerStateModel
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.CategorizedSortType
import com.example.core.model.datastore.SongSortModel
import com.example.core.model.datastore.SongsSortType
import com.example.feature.music_home.model.TabBarModel

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val isSortDropDownMenuShow: Boolean = false,
    val lastHomeScrollState: Int = 0,
    val lastFavoriteScrollState: Int = 0,
    val lastFolderScrollState: Int = 0,
    val playerStateModel: PlayerStateModel = PlayerStateModel.Initial,
    val currentTabBarPosition: TabBarModel = TabBarModel.All,
    val songsList: List<MusicModel> = emptyList(),
    val favoritesList: List<MusicModel> = emptyList(),
    val folderSongsList: List<Pair<String, List<MusicModel>>> = emptyList(),
    val songsSortState: SongSortModel = SongSortModel(SongsSortType.NAME, false),
    val folderSortState: CategorizedSortModel = CategorizedSortModel(CategorizedSortType.NAME, false),
)
