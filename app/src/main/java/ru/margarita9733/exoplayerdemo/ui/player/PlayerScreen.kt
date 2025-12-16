package ru.margarita9733.exoplayerdemo.ui.player

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.flow.StateFlow
import ru.margarita9733.exoplayerdemo.R
import ru.margarita9733.exoplayerdemo.ui.commonComponents.MinimalControls
import ru.margarita9733.exoplayerdemo.ui.commonComponents.rememberSmallPlayerState

@Composable
fun PlayerScreenRoot(
    toolbarHeight: Dp,
    playerStateFlow: StateFlow<Player?>,
) {
    val player by playerStateFlow.collectAsStateWithLifecycle()

    player?.let {
        @OptIn(UnstableApi::class)
        val state = rememberSmallPlayerState(it)

        PlayerScreen(
            toolbarHeight = toolbarHeight,
            player = it,
            imageUri = state.imageUri,
            title = state.title?.toString() ?: "",
            artist = state.artist?.toString() ?: "",
        )
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(64.dp))
    }
}

@Composable
private fun PlayerScreen(
    toolbarHeight: Dp,
    player: Player,
    imageUri: Uri?,
    title: String,
    artist: String
) {
    val context = LocalContext.current
    Column(
        Modifier
            .padding(horizontal = 32.dp)
            .padding(
                WindowInsets.systemBars.add(WindowInsets(top = toolbarHeight)).asPaddingValues()
            )
    ) {
        Spacer(Modifier.size(16.dp))
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .aspectRatio(1.0f)
                .clip(RoundedCornerShape(12.dp)),
            model = ImageRequest.Builder(context)
                .data(imageUri)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_note_24),
            error = painterResource(R.drawable.ic_note_24),
            contentDescription = stringResource(R.string.content_description),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.size(16.dp))
        TrackText(title, artist)
        Spacer(Modifier.size(48.dp))

        MinimalControls(
            modifier = Modifier
                .padding(horizontal = 48.dp),
            player = player
        )
    }
}

@Composable
fun TrackText(title: String, artist: String) {
    Column {
        Text(
            modifier = Modifier.height(24.dp),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight(800),
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.height(24.dp),
            text = artist,
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.gray),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight(600),
            overflow = TextOverflow.Ellipsis
        )
    }
}







