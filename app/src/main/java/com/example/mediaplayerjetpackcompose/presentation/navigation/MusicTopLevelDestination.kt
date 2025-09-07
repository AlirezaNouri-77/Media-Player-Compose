package com.example.mediaplayerjetpackcompose.presentation.navigation

import androidx.annotation.Keep
import com.example.core.model.MediaCategory
import kotlinx.serialization.Serializable

@Keep
sealed interface MusicTopLevelDestination {
    @Serializable
    data object Parent : MusicTopLevelDestination

    @Serializable
    data object Home : MusicTopLevelDestination

    @Serializable
    data object Album : MusicTopLevelDestination

    @Serializable
    data object Artist : MusicTopLevelDestination

    @Serializable
    data object Search : MusicTopLevelDestination

    @Serializable
    data class Category(
        val name: String,
        val mediaCategory: MediaCategory,
        val displayWithVisuals: Boolean = true,
    ) : MusicTopLevelDestination
}
