package com.example.feature.music_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.util.sortMusic
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.domain.useCase.GetMusicPlayerStateUseCase
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.SongSortModel
import com.example.core.model.datastore.SortType
import com.example.datastore.ScrollListDataStoreManager
import com.example.datastore.SortDataStoreManagerImpl
import com.example.feature.music_home.model.TabBarModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val musicSource: MusicSourceImpl,
    private val songsSortDataStoreManager: SortDataStoreManagerImpl<SongSortModel>,
    private val folderSortDataStoreManager: SortDataStoreManagerImpl<CategorizedSortModel>,
    private val scrollListDataStoreManager: ScrollListDataStoreManager,
    private val getMusicPlayerStateUseCase: GetMusicPlayerStateUseCase,
) : ViewModel() {
    private var mUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState = mUiState
        .onStart {
            getScrollState()
            getFavoriteSongs()
            getSongs()
            getFolderSongs()
            getSortStates()
            observePlayerState()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            HomeScreenUiState(isLoading = true),
        )

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.UpdateSortOrder -> updateSortOrder(event.tabBarPosition)
            is HomeUiEvent.UpdateSortState -> updateSortType(event.tabBarPosition, event.sortType)
            is HomeUiEvent.UpdateTabBarPosition -> mUiState.update { it.copy(currentTabBarPosition = event.tabBarPosition) }
            is HomeUiEvent.UpdateHomeScrollIndex -> updateHomeScrollIndex(event.index)
            is HomeUiEvent.UpdateFavoriteScrollIndex -> updateFavoriteScrollIndex(event.index)
            is HomeUiEvent.UpdateFolderScrollIndex -> updateFolderScrollIndex(event.index)
            HomeUiEvent.HideSortDropDownMenu -> mUiState.update { it.copy(isSortDropDownMenuShow = false) }
            HomeUiEvent.ShowSortDropDownMenu -> mUiState.update { it.copy(isSortDropDownMenuShow = true) }
        }
    }

    private fun updateHomeScrollIndex(int: Int) {
        viewModelScope.launch {
            scrollListDataStoreManager.updateHomeScroll(int)
        }
    }

    private fun updateFavoriteScrollIndex(int: Int) {
        viewModelScope.launch {
            scrollListDataStoreManager.updateFavoriteScroll(int)
        }
    }

    private fun updateFolderScrollIndex(int: Int) {
        viewModelScope.launch {
            scrollListDataStoreManager.updateFavoriteScroll(int)
        }
    }

    private fun getScrollState() = viewModelScope.launch {
        val scroll = scrollListDataStoreManager.scrollDataStoreState.first()
        mUiState.update {
            it.copy(
                lastHomeScrollState = scroll.homeMusic,
                lastFavoriteScrollState = scroll.favorite,
                lastFolderScrollState = scroll.folderMusic,
            )
        }
    }

    private fun getSongs() = viewModelScope.launch {
        combine(
            musicSource.songs(),
            songsSortDataStoreManager.sortState,
        ) { songs, sortState ->
            sortMusic(list = songs, isDescending = sortState.isDec, sortType = sortState.sortType)
        }.collect { songs ->
            mUiState.update { it.copy(isLoading = false, songsList = songs) }
        }
    }

    private fun getFolderSongs() = viewModelScope.launch {
        combine(
            musicSource.folder(),
            folderSortDataStoreManager.sortState,
        ) { songs, sortState ->
            sortMusic(list = songs, isDescending = sortState.isDec, sortType = sortState.sortType)
        }.collect { favorite ->
            mUiState.update { it.copy(folderSongsList = favorite) }
        }
    }

    private fun observePlayerState() = viewModelScope.launch {
        getMusicPlayerStateUseCase().collect { playerStateModel ->
            mUiState.update { it.copy(playerStateModel = playerStateModel) }
        }
    }

    private fun getSortStates() {
        combine(
            songsSortDataStoreManager.sortState,
            folderSortDataStoreManager.sortState,
        ) { songs, folder ->
            mUiState.update { it.copy(songsSortState = songs, folderSortState = folder) }
        }.launchIn(viewModelScope)
    }

    private fun getFavoriteSongs() = viewModelScope.launch {
        musicSource.favorite().collect { favoriteSongs ->
            mUiState.update { it.copy(favoritesList = favoriteSongs) }
        }
    }

    private fun updateSortType(tabBarPosition: TabBarModel, sortType: SortType) =
        viewModelScope.launch {
            if (tabBarPosition == TabBarModel.All) {
                songsSortDataStoreManager.updateSortType(sortType)
            } else if (tabBarPosition == TabBarModel.Folder) {
                folderSortDataStoreManager.updateSortType(sortType)
            }
        }

    private fun updateSortOrder(tabBarPosition: TabBarModel) = viewModelScope.launch {
        if (tabBarPosition == TabBarModel.All) {
            songsSortDataStoreManager.updateSortOrder(!mUiState.value.songsSortState.isDec)
        } else if (tabBarPosition == TabBarModel.Folder) {
            folderSortDataStoreManager.updateSortOrder(!mUiState.value.folderSortState.isDec)
        }
    }
}
