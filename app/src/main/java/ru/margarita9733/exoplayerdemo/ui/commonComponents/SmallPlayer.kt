package ru.margarita9733.exoplayerdemo.ui.commonComponents

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import ru.margarita9733.exoplayerdemo.R

@OptIn(UnstableApi::class)
@Composable
fun SmallPlayer(
    player: Player
) {
    Row(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(corner = CornerSize(10.dp)))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val state = rememberSmallPlayerState(player)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(state.imageUri)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_note_24),
            error = painterResource(R.drawable.ic_note_24),
            contentDescription = stringResource(R.string.content_description),
            alignment = Alignment.TopStart,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.Top),
        )
        Spacer(Modifier.width(8.dp))
        TrackText(
            modifier = Modifier.weight(1f),
            title = state.title?.toString() ?: "",
            artist = state.artist?.toString() ?: "",
        )
        Spacer(Modifier.width(8.dp))
        PlayPauseButton(
            player, Modifier
                .size(48.dp)

        )
    }
}

