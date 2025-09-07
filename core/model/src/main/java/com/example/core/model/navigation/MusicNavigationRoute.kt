package com.example.core.model.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed interface MusicNavigationRoute {
    @Serializable
    data object ParentMusic

    @Serializable
    data object Home : MusicNavigationRoute

    @Serializable
    data object Album : MusicNavigationRoute

    @Serializable
    data object Artist : MusicNavigationRoute

    @Serializable
    data object Search : MusicNavigationRoute

    @Serializable
    data class Category(
        val name: String,
        val parentCategoryRoute: ParentCategoryRoute,
        val displayWithVisuals: Boolean = true,
    ) : MusicNavigationRoute
}

@Keep
enum class ParentCategoryRoute {
    FOLDER,
    ARTIST,
    ALBUM,
}
