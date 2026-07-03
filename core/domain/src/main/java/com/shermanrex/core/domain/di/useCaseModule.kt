package com.shermanrex.core.domain.di

import com.shermanrex.core.domain.useCase.GetMusicPlayerStateUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetMusicPlayerStateUseCase(get()) }
}
