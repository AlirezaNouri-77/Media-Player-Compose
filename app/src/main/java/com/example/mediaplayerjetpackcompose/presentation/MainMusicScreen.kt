package com.example.mediaplayerjetpackcompose.presentation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.core.model.MediaCategory
import com.example.feature.music_album.AlbumRoute
import com.example.feature.music_artist.ArtistRoute
import com.example.feature.music_categorydetail.CategoryDetailRoute
import com.example.feature.music_categorydetail.CategoryViewModel
import com.example.feature.music_home.HomeMusic
import com.example.feature.music_player.PlayerActions
import com.example.feature.music_player.PlayerViewModel
import com.example.feature.music_search.SearchRoot
import com.example.mediaplayerjetpackcompose.presentation.navigation.MusicTopLevelDestination
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.musicNavGraph(
    navController: NavHostController,
    playerViewModel: PlayerViewModel,
    sharedTransitionScope: SharedTransitionScope,
    onNavigateToVideoScreen: () -> Unit,
) {
    navigation<MusicTopLevelDestination.Parent>(
        startDestination = MusicTopLevelDestination.Home,
    ) {
        composable<MusicTopLevelDestination.Home> {
            with(sharedTransitionScope) {
                HomeMusic(
                    onNavigateToVideoScreen = onNavigateToVideoScreen,
                    animatedVisibilityScope = this@composable,
                    navigateToCategoryPage = {
                        navController.navigate(MusicTopLevelDestination.Category(it, MediaCategory.FOLDER, false))
                    },
                    onMusicClick = { index, list ->
                        playerViewModel.onPlayerAction(PlayerActions.PlaySongs(index, list))
                    },
                )
            }
        }

        composable<MusicTopLevelDestination.Artist> {
            with(sharedTransitionScope) {
                ArtistRoute(
                    modifier = Modifier,
                    animatedVisibilityScope = this@composable,
                    navigateToCategory = {
                        navController.navigate(MusicTopLevelDestination.Category(it, MediaCategory.ARTIST))
                    },
                )
            }
        }

        composable<MusicTopLevelDestination.Album> {
            with(sharedTransitionScope) {
                AlbumRoute(
                    modifier = Modifier,
                    navigateToCategory = {
                        navController.navigate(MusicTopLevelDestination.Category(it, MediaCategory.ALBUM))
                    },
                    animatedVisibilityScope = this@composable,
                )
            }
        }

        composable<MusicTopLevelDestination.Search> {
            // val currentMusicState by playerViewModel.currentMusicState.collectAsStateWithLifecycle()
            SearchRoot(
                modifier = Modifier.imePadding(),
                onMusicClick = { index, list ->
                    playerViewModel.onPlayerAction(PlayerActions.PlaySongs(index, list))
                },
            )
        }

        composable<MusicTopLevelDestination.Category> { backStackEntry ->
            // for name of parent such as folder name or artist name ...
            val categoryName = backStackEntry.toRoute<MusicTopLevelDestination.Category>().name
            // for example if user in folder or artist page ...
            val parentRoute =
                backStackEntry.toRoute<MusicTopLevelDestination.Category>().mediaCategory

            val displayWithVisuals =
                backStackEntry.toRoute<MusicTopLevelDestination.Category>().displayWithVisuals

            val categoryViewModel: CategoryViewModel = koinViewModel<CategoryViewModel>(
                parameters = { parametersOf(categoryName, parentRoute, displayWithVisuals) },
            )

            with(sharedTransitionScope) {
                CategoryDetailRoute(
                    categoryViewModel = categoryViewModel,
                    categoryName = categoryName,
                    onBackClick = navController::popBackStack,
                    animatedVisibilityScope = this@composable,
                    displayWithVisuals = displayWithVisuals,
                    onMusicClick = { index, list ->
                        playerViewModel.onPlayerAction(PlayerActions.PlaySongs(index, list))
                    },
                )
            }
        }
    }
}
