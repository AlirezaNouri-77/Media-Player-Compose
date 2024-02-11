package com.example.mediaplayerjetpackcompose.presentation.screen.musicscreen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.palette.graphics.Palette
import com.example.mediaplayerjetpackcompose.ApplicationClass
import com.example.mediaplayerjetpackcompose.PlayBackHandler
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.domain.model.MusicMediaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
  var showBottomSheet = mutableStateOf(false)

  var currentMusicState = playBackHandler.musicState
  var currentMusicPosition = playBackHandler.currentMusicPosition.stateIn(
    viewModelScope,
    SharingStarted.Eagerly,
    0L
  )

  init {
    getMusic()
  }

  fun sortMusicListByCategory(
    list: MutableList<MusicMediaModel>
  ): MutableList<MusicMediaModel> {
    viewModelScope.launch(Dispatchers.Main) {
      if (!isDec.value) {
        when (currentListSort.value) {
          SortItem.NAME -> list.sortBy { it.name }
          SortItem.ARTIST -> list.sortBy { it.artist }
          SortItem.DURATION -> list.sortBy { it.duration }
          SortItem.SIZE -> list.sortBy { it.size }
          else -> {}
        }
      } else {
        when (currentListSort.value) {
          SortItem.NAME -> list.sortByDescending { it.name }
          SortItem.ARTIST -> list.sortByDescending { it.artist }
          SortItem.DURATION -> list.sortByDescending { it.duration }
          SortItem.SIZE -> list.sortByDescending { it.size }
          else -> {}
        }
      }
    }.invokeOnCompletion {
      updateMediaItemList(list)
    }
    return list
  }

  private fun updateMediaItemList(list: List<MusicMediaModel>) =
    viewModelScope.launch(Dispatchers.Main) {
      val index = list.indexOfFirst { it.musicId.toString() == currentMusicState.value.mediaId }
      playBackHandler.updateMediaList(index, musicList, currentMusicPosition.value)
    }

  fun moveToNext() = playBackHandler.moveToNext()
  fun moveToPrevious() = playBackHandler.moveToPrevious()
  fun pauseMusic() = playBackHandler.pauseMusic()
  fun resumeMusic() = playBackHandler.resumeMusic()
  fun seekToPosition(position: Long) = playBackHandler.seekToPosition(position)

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

  fun getImageArt(uri: Uri): ImageBitmap {
    return runCatching {
      return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        myApplication.applicationContext.contentResolver.loadThumbnail(
          uri,
          Size(250, 250),
          null
        ).asImageBitmap()
      } else {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(myApplication.applicationContext, uri)
        val byteArray = mediaMetadataRetriever.embeddedPicture
        val bitmap = BitmapFactory.decodeByteArray(
          byteArray,
          0,
          byteArray!!.size
        )
        return Bitmap.createScaledBitmap(bitmap, 250, 250, true).asImageBitmap()
      }
    }.getOrElse {
      myApplication.applicationContext.getDrawable(R.drawable.music_wave)!!.toBitmap()
        .asImageBitmap()
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
}