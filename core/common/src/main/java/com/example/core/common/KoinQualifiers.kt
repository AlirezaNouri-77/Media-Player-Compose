package com.example.core.common

import org.koin.core.qualifier.StringQualifier

enum class CoroutineType(val qualifier: StringQualifier) {
    MAIN(StringQualifier("CoroutineMain")),
    IO(StringQualifier("CoroutineIO")),
    DEFAULT(StringQualifier("CoroutineDefault")),
}

enum class DispatcherType(val qualifier: StringQualifier) {
    MAIN(StringQualifier("Main")),
    IO(StringQualifier("IO")),
    DEFAULT(StringQualifier("Default")),
}

enum class ExoPlayerType(val qualifier: StringQualifier) {
    MUSIC(StringQualifier("Music")),
    VIDEO(StringQualifier("Video")),
}
