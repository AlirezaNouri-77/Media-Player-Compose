package com.example.core.common.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module

var CommonDispatcherModule = module {

    factory(DispatcherType.MAIN.qualifier) { Dispatchers.Main.immediate }

    factory(DispatcherType.IO.qualifier) { Dispatchers.IO }

    factory(DispatcherType.DEFAULT.qualifier) { Dispatchers.Default }
}

enum class DispatcherType(val qualifier: StringQualifier) {
    MAIN(StringQualifier("Main")),
    IO(StringQualifier("IO")),
    DEFAULT(StringQualifier("Default")),
}
