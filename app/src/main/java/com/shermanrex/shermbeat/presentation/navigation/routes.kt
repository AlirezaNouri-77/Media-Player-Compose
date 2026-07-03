package com.shermanrex.shermbeat.presentation.navigation

import androidx.navigation3.runtime.NavKey
import com.shermanrex.core.model.MediaCategory
import kotlinx.serialization.Serializable

@Serializable
data object SplashScreen : NavKey

@Serializable
data object Music : NavKey

@Serializable
data object HomeMusic : NavKey

@Serializable
data object ArtistMusic : NavKey

@Serializable
data object AlbumMusic : NavKey

@Serializable
data object SearchMusic : NavKey

@Serializable
data object VideoHome : NavKey

@Serializable
data object VideoPlayer : NavKey

@Serializable
data class DetailMusic(
    val name: String,
    val mediaCategory: MediaCategory,
    val displayWithVisuals: Boolean = true,
) : NavKey

@Serializable
data object Video : NavKey
