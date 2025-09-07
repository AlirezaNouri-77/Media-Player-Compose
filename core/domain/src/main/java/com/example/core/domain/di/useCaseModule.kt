package com.example.core.domain.di

import com.example.core.domain.useCase.GetMusicPlayerStateUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetMusicPlayerStateUseCase(get()) }
}
