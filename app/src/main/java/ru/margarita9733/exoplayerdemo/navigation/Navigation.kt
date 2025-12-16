package ru.margarita9733.exoplayerdemo.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
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

@Serializable
object HomeScreenRoute

@Serializable
object AudioAccessRequiredScreenRoute

fun NavController.navigateToAudioAccessRequiredScreenRoute(navOptions: NavOptions? = null) {
    navigate(route = AudioAccessRequiredScreenRoute, navOptions)
}

fun NavController.navigateToHomeScreenRoute(navOptions: NavOptions? = null) {
    navigate(route = HomeScreenRoute, navOptions)
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
                onNavigateToPlayer = { },
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
    }
}

