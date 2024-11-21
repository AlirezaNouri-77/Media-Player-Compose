package com.example.mediaplayerjetpackcompose.data.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.example.mediaplayerjetpackcompose.data.DeviceVolumeManager
import com.example.mediaplayerjetpackcompose.data.GetMediaMetaData
import com.example.mediaplayerjetpackcompose.data.MediaThumbnailUtil
import com.example.mediaplayerjetpackcompose.data.database.databaseClass.AppDataBase
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.musicSection.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.VideoItemModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

var appModule = module {

  single {
    Room.databaseBuilder(
      context = androidApplication().applicationContext,
      AppDataBase::class.java, "DataBase_MediaPlayer",
    ).build()
  }
  single { ExoPlayer.Builder(androidApplication().applicationContext).build() }
  single { get<AppDataBase>().dataBaseDao() }
  single { MediaThumbnailUtil(androidApplication().applicationContext) }

  single { DeviceVolumeManager(androidApplication().applicationContext) }

  single { MediaThumbnailUtil(androidContext()) }
  single { GetMediaMetaData(get()) }

  single<MediaStoreRepositoryImpl<MusicModel>>(named("musicRepo")) { MusicMediaStoreRepository(androidContext().contentResolver) }
  single<MediaStoreRepositoryImpl<VideoItemModel>> { VideoMediaStoreRepository(androidContext().contentResolver) }
  single { MusicServiceConnection(androidApplication().applicationContext) }

  viewModel { MusicPageViewModel(get(named("musicRepo")), get(), get(), get(), get()) }
  viewModel { VideoPageViewModel(get(), get(), get(), get()) }

}
