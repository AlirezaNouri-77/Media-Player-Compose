package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediaplayerjetpackcompose.PlayBackHandler
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPageViewModel(
  private var musicMediaStoreRepository: MusicMediaStoreRepository,
  private var myApplication: ApplicationClass,
  private var playBackHandler: PlayBackHandler,
) : AndroidViewModel(myApplication) {

  var musicList = mutableStateListOf<MusicMediaModel>()
  var musicCategoryList = mutableStateListOf<MusicMediaModel>()
  var artistsMusicMap = mapOf<String, List<MusicMediaModel>>()
  var albumMusicMap = mapOf<String, List<MusicMediaModel>>()

  var currentListSort = mutableStateOf(SortItem.NAME)
  var currentTabState by mutableIntStateOf(0)
  var isDec = mutableStateOf(false)

  var currentMusicState = playBackHandler.musicState
  var currentMusicPosition = playBackHandler.currentMusicPosition.stateIn(
    viewModelScope,
    SharingStarted.Eagerly,
    0L
  )

  init {
    getMusic()
  }

  fun sortMusicListByCategory(sort: SortItem) {
    viewModelScope.launch(Dispatchers.Main) {
      when (sort) {
        SortItem.NAME -> {
          musicList.sortBy { it.name }
          musicCategoryList.sortBy { it.name }
        }

        SortItem.ARTIST -> {
          musicList.sortBy { it.artist }
          musicCategoryList.sortBy { it.artist }
        }

        SortItem.DURATION -> {
          musicList.sortBy { it.duration }
          musicCategoryList.sortBy { it.duration }
        }

        SortItem.SIZE -> {
          musicList.sortBy { it.size }
          musicCategoryList.sortBy { it.duration }
        }

        else -> {}
      }
    }.also {
      currentListSort.value = sort
      it.invokeOnCompletion { updateMediaItemList() }
    }
  }

  fun sortMusicByAscOrDec() {
    viewModelScope.launch(Dispatchers.Main) {
      val dumy = musicList.reversed()
      val dumy2 = musicCategoryList.reversed()
      musicList.clear().also { musicList.addAll(dumy) }
      musicCategoryList.clear().also { musicCategoryList.addAll(dumy2) }
      updateMediaItemList()
    }
  }

  private fun updateMediaItemList() = viewModelScope.launch {
    val index = musicList.indexOfFirst { it.musicId.toString() == currentMusicState.value.mediaId }
    playBackHandler.updateMediaList(index, musicList, currentMusicPosition.value)
  }

  fun moveToNext() = playBackHandler.moveToNext()
  fun moveToPrevious() = playBackHandler.moveToPrevious()
  fun pauseMusic() = playBackHandler.pauseMusic()
  fun resumeMusic() = playBackHandler.resumeMusic()
  fun seekToPosition(position: Long) = playBackHandler.seekToPosition(position)

  fun getImageArt(uri: Uri): Bitmap {
    return runCatching {
      val mediaMetadataRetriever = MediaMetadataRetriever()
      mediaMetadataRetriever.setDataSource(myApplication.applicationContext, uri)
      val byteArray = mediaMetadataRetriever.embeddedPicture
      val bitmap = BitmapFactory.decodeByteArray(
        byteArray,
        0,
        byteArray!!.size
      )
      return Bitmap.createScaledBitmap(bitmap, 300, 300, false)
    }.getOrElse {
      myApplication.applicationContext.getDrawable(R.drawable.placeholder_music)!!.toBitmap()
    }
  }

  fun playMusic(index: Int, musicList: List<MusicMediaModel>) =
    viewModelScope.launch(Dispatchers.Main) {
      playBackHandler.playMusic(index, musicList)
    }

  private fun getMusic() {
    viewModelScope.launch {
      musicMediaStoreRepository.getMedia(
        mContentResolver = myApplication.contentResolver,
        context = myApplication.applicationContext,
      ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
        .collectLatest { musicMediaModel ->
          withContext(Dispatchers.Main) {
            musicList.addAll(musicMediaModel)
            artistsMusicMap = musicMediaModel.groupBy { it.artist }
            albumMusicMap = musicMediaModel.groupBy { it.album }
          }
        }
    }
  }

  companion object {
    var Factory: ViewModelProvider.Factory = viewModelFactory {
      initializer {
        val application =
          checkNotNull((this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])) as ApplicationClass
        MusicPageViewModel(
          myApplication = application,
          musicMediaStoreRepository = application.musicMediaStoreRepository,
          playBackHandler = application.playBackHandler,
        )
      }
    }
  }

}

enum class SortItem(var sortName: String) {
  NAME("Name"),
  ARTIST("Artist"),
  DURATION("Duration"),
  SIZE("Size"),
  DATE("Date"),
}