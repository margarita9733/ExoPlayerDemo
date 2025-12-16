package ru.margarita9733.exoplayerdemo.ui

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.margarita9733.exoplayerdemo.ExoPlayerDemoApplication
import ru.margarita9733.exoplayerdemo.data.model.Track
import ru.margarita9733.exoplayerdemo.domain.AppRepository
import ru.margarita9733.exoplayerdemo.utils.AudioAccessPermissionState

class MainViewModel(
    private val appContext: Context,
    private val appRepository: AppRepository
) : ViewModel() {

    private var sessionToken: SessionToken? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null

    private val _playerStateFlow: MutableStateFlow<Player?> = MutableStateFlow(null)

    private val _permissionsStateFlow: MutableStateFlow<AudioAccessPermissionState> =
        MutableStateFlow(AudioAccessPermissionState.UNKNOWN)


    val permissionStateFlow: StateFlow<AudioAccessPermissionState> =
        _permissionsStateFlow
            .onEach { permissionState ->
                if (permissionState == AudioAccessPermissionState.GRANTED) {
                    appRepository.loadTracks()
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AudioAccessPermissionState.UNKNOWN
            )

    val tracksFlow: StateFlow<List<Track>> =
        appRepository.getTracksAsFlow()
            .combine(permissionStateFlow) { tracks, permissionState ->
                if (permissionState == AudioAccessPermissionState.GRANTED) {
                    tracks
                } else {
                    emptyList()
                }
            }.catch { throwable ->
                Log.d(
                    "HomeViewModel tracksFlow ",
                    "Ошибка: ${throwable.message ?: "Неизвестная ошибка"}"
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )


    val playerStateFlow: StateFlow<Player?> = combine(
        tracksFlow,
        _playerStateFlow,
        permissionStateFlow
    ) { tracks, player, permissionState ->
        when {
            player == null -> null

            else -> player.also {
                if (player.mediaItemCount == 0) {
                    player.setMediaItems(tracks.map { it.toMediaItem() })
                }
                if (permissionState == AudioAccessPermissionState.DENIED) {
                    player.pause()
                }
            }
        }
    }.catch { throwable ->
        Log.d(
            "HomeViewModel playerStateFlow ",
            "Ошибка: ${throwable.message ?: "Неизвестная ошибка"}"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    init {
        sessionToken =
            SessionToken(appContext, ComponentName(appContext, PlaybackService::class.java))
                .also { connectToPlayer(it) }
    }

    private fun connectToPlayer(sessionToken: SessionToken) {
        controllerFuture = MediaController
            .Builder(appContext, sessionToken)
            .buildAsync()
            .also { controllerFuture ->
                controllerFuture.addListener(
                    {
                        _playerStateFlow.value = controllerFuture.get() as Player?
                    },
                    MoreExecutors.directExecutor()
                )
            }
    }


    private fun Track.toMediaItem(): MediaItem {

        val metadata = MediaMetadata.Builder()
            .setArtworkUri(imageUri.toUri())
            .setTitle(title)
            .setArtist(artist)
            .build()

        val mediaItem = MediaItem.Builder()
            .setMediaId(id.toString())
            .setUri(uri)
            .setMediaMetadata(metadata)
            .build()

        return mediaItem
    }

    fun setPermissionState(isGranted: Boolean) {

        _permissionsStateFlow.value = if (isGranted) {
            AudioAccessPermissionState.GRANTED
        } else {
            AudioAccessPermissionState.DENIED
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return MainViewModel(
                    appContext = application.applicationContext,
                    appRepository = (application as ExoPlayerDemoApplication).appContainer.appRepository,
                ) as T
            }
        }
    }
}
