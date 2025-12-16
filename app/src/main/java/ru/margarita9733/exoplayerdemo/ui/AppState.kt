package ru.margarita9733.exoplayerdemo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.media3.common.Player
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.StateFlow
import ru.margarita9733.exoplayerdemo.utils.AudioAccessPermissionState

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    playerStateFlow: StateFlow<Player?>,
    audioAccessPermissionState: StateFlow<AudioAccessPermissionState>,
    onTogglePermission: (Boolean) -> Unit = {}

): AppState {
    return remember(
        navController,
        playerStateFlow,
        audioAccessPermissionState,
        onTogglePermission,
    ) {
        AppState(navController, playerStateFlow, audioAccessPermissionState, onTogglePermission)
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    val playerStateFlow: StateFlow<Player?>,
    val audioAccessPermissionState: StateFlow<AudioAccessPermissionState>,
    val onTogglePermission: (Boolean) -> Unit = {},
)
