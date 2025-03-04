package com.example.core.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module


var CoroutineModule = module {

  factory(named("CoroutineMain")) {
    CoroutineScope(SupervisorJob() + get<MainCoroutineDispatcher>(named("Main")))
  }

  factory(named("CoroutineIO")) {
    CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named("IO")))
  }

  factory(named("CoroutineDefault")) {
    CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named("Default")))
  }

}