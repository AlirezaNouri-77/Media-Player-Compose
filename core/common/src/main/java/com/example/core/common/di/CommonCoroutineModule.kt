package com.example.core.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module


var CommonCoroutineModule = module {

  factory(CoroutineType.MAIN.qualifier) {
    CoroutineScope(SupervisorJob() + get<MainCoroutineDispatcher>(DispatcherType.MAIN.qualifier))
  }

  factory(CoroutineType.IO.qualifier) {
    CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(DispatcherType.IO.qualifier))
  }

  factory(CoroutineType.DEFAULT.qualifier) {
    CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(DispatcherType.DEFAULT.qualifier))
  }

}

enum class CoroutineType(val qualifier: StringQualifier) {
  MAIN(StringQualifier("CoroutineMain")),
  IO(StringQualifier("CoroutineIO")),
  DEFAULT(StringQualifier("CoroutineDefault")),
}