package com.example.mediaplayerjetpackcompose.data.di

import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.example.mediaplayerjetpackcompose.data.database.databaseClass.AppDataBase
import com.example.mediaplayerjetpackcompose.data.musicManager.FavoriteMusicManager
import com.example.mediaplayerjetpackcompose.data.musicManager.SearchMusicManager
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.data.repository.MusicSource
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.data.util.DeviceVolumeManager
import com.example.mediaplayerjetpackcompose.data.util.GetMediaMetaData
import com.example.mediaplayerjetpackcompose.data.util.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.VideoItemModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.ShareViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.album.AlbumViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.artist.ArtistViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.category.CategoryViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.home.HomeViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.feature.search.SearchViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

var appModule = module {

  single {
    Room.databaseBuilder(
      context = androidApplication().applicationContext,
      AppDataBase::class.java, "DataBase_MediaPlayer",
    ).build()
  }

  factory { ExoPlayer.Builder(androidApplication().applicationContext).build() }
  single { get<AppDataBase>().dataBaseDao() }
  single { MediaThumbnailUtil(androidApplication().applicationContext) }

  single { DeviceVolumeManager(androidApplication().applicationContext) }

  single { MediaThumbnailUtil(androidApplication().applicationContext) }
  single { GetMediaMetaData(get()) }

  factory(named("CoroutineMain")) { CoroutineScope(Dispatchers.Main) }

  single {
    MusicMediaStoreRepository(
      androidApplication().applicationContext.contentResolver,
    )
  }

  single { SearchMusicManager(get()) }
  single { FavoriteMusicManager(get()) }
  single { MusicSource(get(),get()) }

  single<MediaStoreRepositoryImpl<VideoItemModel>> { VideoMediaStoreRepository(androidApplication().applicationContext.contentResolver) }
  single { MusicServiceConnection(androidApplication().applicationContext, get(named("CoroutineMain"))) }

  viewModelOf(::ShareViewModel)
  viewModelOf(::VideoPageViewModel)
  viewModelOf(::AlbumViewModel)
  viewModelOf(::CategoryViewModel)
  viewModelOf(::ArtistViewModel)
  viewModelOf(::SearchViewModel)
  viewModelOf(::HomeViewModel)

}
