package ru.margarita9733.exoplayerdemo.ui.commonComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player


/**
 * Minimal playback controls for a [Player].
 *
 * Includes buttons for seeking to a previous/next items and playing/pausing the playback.
 */
@Composable
fun MinimalControls(
    player: Player,
    modifier: Modifier = Modifier,
) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.weight(1f))
        PreviousButton(
            player,
            Modifier
                .size(42.dp)
        )
        Spacer(Modifier.weight(0.3f))
        PlayPauseButton(
            player,
            Modifier
                .size(58.dp)
        )
        Spacer(Modifier.weight(0.3f))
        NextButton(
            player, Modifier
                .size(42.dp)
        )
        Spacer(Modifier.weight(1f))
    }
}