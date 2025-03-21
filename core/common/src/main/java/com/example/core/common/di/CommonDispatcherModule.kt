package com.example.core.common.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

var CommonDispatcherModule = module {

  single(named("Main")) { Dispatchers.Main.immediate }

  single(named("IO")) { Dispatchers.IO }

  single(named("Default")) { Dispatchers.Default }

}
