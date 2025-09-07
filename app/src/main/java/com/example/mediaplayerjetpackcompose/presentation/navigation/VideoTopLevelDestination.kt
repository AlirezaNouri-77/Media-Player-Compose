package com.example.mediaplayerjetpackcompose.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface VideoTopLevelDestination {
    @Serializable
    data object Parent : VideoTopLevelDestination

    @Serializable
    data object VideoPage : VideoTopLevelDestination

    @Serializable
    data object VideoPlayer : VideoTopLevelDestination
}
