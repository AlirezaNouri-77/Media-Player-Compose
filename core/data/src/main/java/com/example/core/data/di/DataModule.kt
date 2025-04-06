package com.example.core.data.di

import com.example.core.data.repository.FavoriteMusicSource
import com.example.core.data.repository.FavoriteMusicSourceImpl
import com.example.core.data.repository.SearchMusicRepository
import com.example.core.data.repository.SearchMusicRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var DataModule = module {

  single { SearchMusicRepository(get(), get(named("IO"))) } bind SearchMusicRepositoryImpl::class

  single { FavoriteMusicSource(get(), get(), get(named("IO"))) } bind FavoriteMusicSourceImpl::class

  single {
    FavoriteMusicSource(
      get(),
      get(),
      get(named("IO"))
    )
  } bind FavoriteMusicSource::class

}