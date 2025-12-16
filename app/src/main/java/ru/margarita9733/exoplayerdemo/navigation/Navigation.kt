package ru.margarita9733.exoplayerdemo.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import kotlinx.serialization.Serializable
import ru.margarita9733.exoplayerdemo.ui.AppState
import ru.margarita9733.exoplayerdemo.ui.audioAccessRequired.AudioAccessRequiredScreenRoot
import ru.margarita9733.exoplayerdemo.ui.home.HomeScreenRoot
import ru.margarita9733.exoplayerdemo.ui.player.PlayerScreenRoot

@Serializable
object HomeScreenRoute

@Serializable
object AudioAccessRequiredScreenRoute

@Serializable
object PlayerScreenRoute

fun NavController.navigateToAudioAccessRequiredScreenRoute(navOptions: NavOptions? = null) {
    navigate(route = AudioAccessRequiredScreenRoute, navOptions)
}

fun NavController.navigateToHomeScreenRoute(navOptions: NavOptions? = null) {
    navigate(route = HomeScreenRoute, navOptions)
}

fun NavController.navigateToPlayerScreenRoute(navOptions: NavOptions? = null) {
    navigate(route = PlayerScreenRoute, navOptions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    appState: AppState,
    paddingValues: PaddingValues,
) {
    val navController = appState.navController

    val onNavigateToAudioAccessRequired: () -> Unit = {
        navController.navigateToAudioAccessRequiredScreenRoute(
            navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        )
    }

    val onReturnFromNavigateToAudioAccessRequired: () -> Unit = {
        navController.navigateToHomeScreenRoute(navOptions {
            popUpTo(AudioAccessRequiredScreenRoute) { inclusive = true }
            launchSingleTop = true
        })
    }

    NavHost(navController = navController, startDestination = HomeScreenRoute)
    {
        composable<HomeScreenRoute> {
            HomeScreenRoot(
                onNavigateToPlayer = {
                    navController.navigateToPlayerScreenRoute(
                        navOptions {
                            launchSingleTop = true
                        })
                },
                paddingValues = paddingValues,
                playerStateFlow = appState.playerStateFlow,
                audioAccessPermissionState = appState.audioAccessPermissionState,
                onNavigateToNoPermission = onNavigateToAudioAccessRequired,
            )
        }

        composable<AudioAccessRequiredScreenRoute> {
            AudioAccessRequiredScreenRoot(
                onNavigateToHome = onReturnFromNavigateToAudioAccessRequired,
                onTogglePermission = appState.onTogglePermission
            )
        }

        composable<PlayerScreenRoute>(enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Up
            )
        }, popExitTransition = {
            fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                animationSpec = tween(300, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.Down
            )
        }) {
            PlayerScreenRoot(
                toolbarHeight = TopAppBarDefaults.MediumAppBarCollapsedHeight,
                playerStateFlow = appState.playerStateFlow,
            )
        }
    }
}

