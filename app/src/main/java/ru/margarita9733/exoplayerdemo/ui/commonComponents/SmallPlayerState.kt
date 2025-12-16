package ru.margarita9733.exoplayerdemo.ui.commonComponents

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.Player
import androidx.media3.common.listen
import androidx.media3.common.util.UnstableApi
import ru.margarita9733.exoplayerdemo.utils.getCurrentTrackArtistOrNull
import ru.margarita9733.exoplayerdemo.utils.getCurrentTrackImageUriOrNull
import ru.margarita9733.exoplayerdemo.utils.getCurrentTrackTitleOrNull

@UnstableApi
@Composable
fun rememberSmallPlayerState(player: Player): SmallPlayerState {

    val smallPlayerState = remember(player) { SmallPlayerState(player) }

    LaunchedEffect(player) { smallPlayerState.observe() }
    return smallPlayerState
}

class SmallPlayerState(private val player: Player) {

    var imageUri by mutableStateOf(player.getCurrentTrackImageUriOrNull())
        private set

    var title by mutableStateOf(player.getCurrentTrackTitleOrNull())
        private set

    var artist by mutableStateOf(player.getCurrentTrackArtistOrNull())
        private set

    var playbackProgressionMillis by mutableLongStateOf(player.currentPosition)
        private set

    suspend fun observe() {
        imageUri = player.getCurrentTrackImageUriOrNull()
        title = player.getCurrentTrackTitleOrNull()
        artist = player.getCurrentTrackArtistOrNull()
        playbackProgressionMillis = player.currentPosition

        player.listen { events ->
            if (
                events.containsAny(
                    Player.EVENT_PLAYBACK_STATE_CHANGED,
                    Player.EVENT_PLAY_WHEN_READY_CHANGED,
                    Player.EVENT_AVAILABLE_COMMANDS_CHANGED,
                    Player.EVENT_MEDIA_ITEM_TRANSITION,
                )
            ) {
                imageUri = player.getCurrentTrackImageUriOrNull()
                title = player.getCurrentTrackTitleOrNull()
                artist = player.getCurrentTrackArtistOrNull()
                playbackProgressionMillis = player.currentPosition
            }
        }
    }

}

