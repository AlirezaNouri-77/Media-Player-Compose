package com.example.core.common.di

import com.example.core.common.DispatcherType
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

var CommonDispatcherModule = module {
    factory(DispatcherType.MAIN.qualifier) { Dispatchers.Main.immediate }

    factory(DispatcherType.IO.qualifier) { Dispatchers.IO }

    factory(DispatcherType.DEFAULT.qualifier) { Dispatchers.Default }
}
