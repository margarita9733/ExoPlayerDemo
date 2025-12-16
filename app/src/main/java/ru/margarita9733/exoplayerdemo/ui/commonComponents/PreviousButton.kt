package ru.margarita9733.exoplayerdemo.ui.commonComponents

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPreviousButtonState
import ru.margarita9733.exoplayerdemo.R

@OptIn(UnstableApi::class)
@Composable
fun PreviousButton(player: Player, modifier: Modifier = Modifier) {

    val state = rememberPreviousButtonState(player)

    Icon(
        Icons.Rounded.SkipPrevious,
        contentDescription = stringResource(R.string.content_description),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable(enabled = state.isEnabled, onClick = state::onClick),
    )
}