package com.example.core.common.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

var CommonDispatcherModule = module {

  factory(named("Main")) { Dispatchers.Main.immediate }

  factory(named("IO")) { Dispatchers.IO }

  factory(named("Default")) { Dispatchers.Default }

}
