package com.example.core.data.di

import com.example.core.data.repository.FavoriteRepository
import com.example.core.domain.respository.FavoriteRepositoryImpl
import com.example.core.data.repository.SearchMusicRepository
import com.example.core.domain.respository.SearchMusicRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

var DataModule = module {

  single { SearchMusicRepository(get(), get(named("IO"))) } bind SearchMusicRepositoryImpl::class

  single { FavoriteRepository(get(), get(), get(named("IO"))) } bind FavoriteRepositoryImpl::class

  single {
    FavoriteRepository(
      get(),
      get(),
      get(named("IO"))
    )
  } bind FavoriteRepository::class

}