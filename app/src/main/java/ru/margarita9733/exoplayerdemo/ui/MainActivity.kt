package ru.margarita9733.exoplayerdemo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import ru.margarita9733.exoplayerdemo.ui.theme.ExoPlayerDemoTheme
import ru.margarita9733.exoplayerdemo.utils.isReadAudioPermissionGranted

class MainActivity : ComponentActivity(){

    val viewModel by viewModels<MainViewModel> { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val appState = rememberAppState(
                playerStateFlow = viewModel.playerStateFlow,
                audioAccessPermissionState = viewModel.permissionStateFlow,
                onTogglePermission = viewModel::setPermissionState,
            )

            ExoPlayerDemoTheme {
                ExoPlayerDemoAppRoot(appState = appState)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.setPermissionState(applicationContext.isReadAudioPermissionGranted())
    }
}