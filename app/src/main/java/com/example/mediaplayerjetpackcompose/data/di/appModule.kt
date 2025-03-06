package com.example.mediaplayerjetpackcompose.data.di

import com.example.feature.music_album.AlbumViewModel
import com.example.feature.music_artist.ArtistViewModel
import com.example.feature.music_categorydetail.CategoryViewModel
import com.example.feature.music_home.HomeViewModel
import com.example.feature.music_player.PlayerViewModel
import com.example.feature.music_search.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var appModule = module {

  viewModelOf(::PlayerViewModel)
 // viewModelOf(::VideoPageViewModel)
  viewModelOf(::AlbumViewModel)
  viewModelOf(::CategoryViewModel)
  viewModelOf(::ArtistViewModel)
  viewModelOf(::SearchViewModel)
  viewModelOf(::HomeViewModel)

}
