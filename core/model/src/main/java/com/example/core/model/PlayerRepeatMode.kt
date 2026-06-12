package com.example.core.model

enum class PlayerRepeatMode {
    MODE_OFF,
    MODE_ONE,
    MODE_ALL,
}

fun Int.toRepeatMode(): PlayerRepeatMode {
    return when (this) {
        0 -> PlayerRepeatMode.MODE_OFF
        1 -> PlayerRepeatMode.MODE_ONE
        2 -> PlayerRepeatMode.MODE_ALL
        else -> PlayerRepeatMode.MODE_OFF
    }
}

fun PlayerRepeatMode.toId(): Int {
    return when (this) {
        PlayerRepeatMode.MODE_OFF -> 0
        PlayerRepeatMode.MODE_ONE -> 1
        PlayerRepeatMode.MODE_ALL -> 2
    }
}
