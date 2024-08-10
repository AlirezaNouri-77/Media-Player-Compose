package com.example.mediaplayerjetpackcompose.data.di

import androidx.media3.exoplayer.ExoPlayer
import com.example.mediaplayerjetpackcompose.data.GetMediaArt
import com.example.mediaplayerjetpackcompose.data.database.databaseClass.AppDataBase
import com.example.mediaplayerjetpackcompose.data.repository.MusicMediaStoreRepository
import com.example.mediaplayerjetpackcompose.data.repository.VideoMediaStoreRepository
import com.example.mediaplayerjetpackcompose.data.service.MusicServiceConnection
import com.example.mediaplayerjetpackcompose.domain.api.MediaStoreRepositoryImpl
import com.example.mediaplayerjetpackcompose.domain.model.musicScreen.MusicModel
import com.example.mediaplayerjetpackcompose.domain.model.videoSection.VideoModel
import com.example.mediaplayerjetpackcompose.presentation.screen.music.MusicPageViewModel
import com.example.mediaplayerjetpackcompose.presentation.screen.video.VideoPageViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

var appModule = module {

  single { AppDataBase.getAppDataBase(androidContext()) }
  single { ExoPlayer.Builder(androidApplication().applicationContext).build() }
  single { get<AppDataBase>().dataBaseDao() }
  single { GetMediaArt(androidApplication().applicationContext) }

  single { GetMediaArt(androidContext()) }

  single<MediaStoreRepositoryImpl<MusicModel>>(named("musicRepo")) { MusicMediaStoreRepository(androidContext().contentResolver) }
  single<MediaStoreRepositoryImpl<VideoModel>> { VideoMediaStoreRepository(androidContext().contentResolver) }
  single { MusicServiceConnection(androidApplication().applicationContext) }

  viewModel { MusicPageViewModel(get(named("musicRepo")), get(), get(), get()) }
  viewModel { VideoPageViewModel(get(), get()) }

}
