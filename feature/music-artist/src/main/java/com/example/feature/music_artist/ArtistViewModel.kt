package com.example.feature.music_artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.util.sortMusic
import com.example.core.domain.respository.MusicSourceImpl
import com.example.core.domain.useCase.GetMusicPlayerStateUseCase
import com.example.core.model.datastore.CategorizedSortModel
import com.example.core.model.datastore.CategorizedSortType
import com.example.datastore.SortDataStoreManagerImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArtistViewModel(
    private var musicSource: MusicSourceImpl,
    private var artistSortDataStoreManager: SortDataStoreManagerImpl<CategorizedSortModel>,
    private val getMusicPlayerStateUseCase: GetMusicPlayerStateUseCase,
) : ViewModel() {
    private var mUiState = MutableStateFlow(ArtistScreenUiState())
    val artistScreenUiState = mUiState
        .onStart {
            loadArtistDate()
            getSortState()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            ArtistScreenUiState(),
        )

    fun onEvent(event: ArtistUiEvent) {
        when (event) {
            ArtistUiEvent.HideSortDropDownMenu -> mUiState.update { it.copy(isSortDropDownMenuShow = false) }
            ArtistUiEvent.ShowSortDropDownMenu -> mUiState.update { it.copy(isSortDropDownMenuShow = true) }
            is ArtistUiEvent.UpdateSortOrder -> updateSortOrder(event.isDec)
            is ArtistUiEvent.UpdateSortType -> updateSortType(event.sortType)
        }
    }

    private fun getSortState() = viewModelScope.launch {
        artistSortDataStoreManager.sortState.collectLatest { sort ->
            mUiState.update { it.copy(sortState = sort) }
        }
    }

    private fun observePlayerState() = viewModelScope.launch {
        getMusicPlayerStateUseCase.invoke().collect { playerStateModel ->
            mUiState.update { it.copy(isPlayerHasMediaItem = playerStateModel.currentMediaInfo.musicID.isNotEmpty()) }
        }
    }

    private fun loadArtistDate() = viewModelScope.launch {
        combine(
            musicSource.artist(),
            artistSortDataStoreManager.sortState,
        ) { songs, sortState ->
            sortMusic(list = songs, isDescending = sortState.isDec, sortType = sortState.sortType)
        }.collect { artistData ->
            mUiState.update {
                it.copy(isLoading = false, artistList = artistData)
            }
        }
    }

    private fun updateSortType(songsSortType: CategorizedSortType) = viewModelScope.launch {
        artistSortDataStoreManager.updateSortType(songsSortType)
    }

    private fun updateSortOrder(boolean: Boolean) = viewModelScope.launch {
        artistSortDataStoreManager.updateSortOrder(boolean)
    }
}
