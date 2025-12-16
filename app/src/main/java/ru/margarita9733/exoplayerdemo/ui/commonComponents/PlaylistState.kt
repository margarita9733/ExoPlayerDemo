package ru.margarita9733.exoplayerdemo.ui.commonComponents

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.Player
import androidx.media3.common.listen
import ru.margarita9733.exoplayerdemo.utils.getCurrentMediaItems


@Composable
fun rememberPlaylistState(player: Player): PlaylistState {

    val playlistState = remember(player) { PlaylistState(player) }

    LaunchedEffect(player) { playlistState.observe() }

    return playlistState
}

class PlaylistState(private val player: Player) {

    var currentItem by mutableStateOf(player.currentMediaItem)
        private set

    var currentPlaylist by mutableStateOf(player.getCurrentMediaItems())
        private set

    private fun updateState() {
        currentItem = player.currentMediaItem
        currentPlaylist = player.getCurrentMediaItems()
    }

    suspend fun observe() {

        updateState()

        player.listen { events ->
            if (
                events.containsAny(
                    Player.EVENT_TRACKS_CHANGED,
                    Player.EVENT_MEDIA_ITEM_TRANSITION,
                )
            ) {
                updateState()
            }
        }
    }

}