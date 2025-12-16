package ru.margarita9733.exoplayerdemo.ui.home

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.flow.StateFlow
import ru.margarita9733.exoplayerdemo.R
import ru.margarita9733.exoplayerdemo.ui.commonComponents.CommonBlockTextWithButton
import ru.margarita9733.exoplayerdemo.ui.commonComponents.SmallPlayer
import ru.margarita9733.exoplayerdemo.ui.commonComponents.TrackText
import ru.margarita9733.exoplayerdemo.ui.commonComponents.rememberPlaylistState
import ru.margarita9733.exoplayerdemo.ui.model.TrackItem
import ru.margarita9733.exoplayerdemo.ui.modifiers.noRippleClickable
import ru.margarita9733.exoplayerdemo.utils.AudioAccessPermissionState
import ru.margarita9733.exoplayerdemo.utils.extensions.plus
import ru.margarita9733.exoplayerdemo.utils.toggleIsPlaying

sealed interface HomeScreenState {

    data class DataReceived(val player: Player) : HomeScreenState
    data object Loading : HomeScreenState
    data object NoData : HomeScreenState
}

@Composable
fun HomeScreenRoot(
    paddingValues: PaddingValues,
    playerStateFlow: StateFlow<Player?>,
    audioAccessPermissionState: StateFlow<AudioAccessPermissionState>,
    onNavigateToNoPermission: () -> Unit = {},
    onNavigateToPlayer: () -> Unit = {},
) {
    val lazyListState = rememberLazyListState()

    val permissionState by audioAccessPermissionState.collectAsStateWithLifecycle(Lifecycle.State.RESUMED)

    LaunchedEffect(permissionState) {
        if (permissionState == AudioAccessPermissionState.DENIED) onNavigateToNoPermission()
    }

    val player by playerStateFlow.collectAsStateWithLifecycle()

    val state = player?.let {
        it.isLoading == false && it.mediaItemCount != 0
        HomeScreenState.DataReceived(player = it)
    } ?: HomeScreenState.Loading

    HomeScreen(
        onTryGetDataButtonClick = {},
        paddingValues = paddingValues,
        uiState = state,
        lazyListState = lazyListState,
        onNavigateToPlayer = onNavigateToPlayer
    )

}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onTryGetDataButtonClick: () -> Unit = {},
    onNavigateToPlayer: () -> Unit = {},
    paddingValues: PaddingValues,
    uiState: HomeScreenState,
    lazyListState: LazyListState = rememberLazyListState(),
) {

    when (uiState) {
        is HomeScreenState.DataReceived -> {

            HomeDataReceived(
                paddingValues = paddingValues,
                lazyListState = lazyListState,
                player = uiState.player,
                onNavigateToPlayer = onNavigateToPlayer
            )
        }

        is HomeScreenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
            }

        }

        is HomeScreenState.NoData -> {
            CommonBlockTextWithButton(
                infoText = stringResource(R.string.no_tracks_found),
                buttonText = stringResource(R.string.retry),
                onButtonClick = onTryGetDataButtonClick,
            )
        }

    }
}

@OptIn(UnstableApi::class)
@Composable
fun HomeDataReceived(
    modifier: Modifier = Modifier,
    player: Player,
    paddingValues: PaddingValues,
    lazyListState: LazyListState = rememberLazyListState(),
    onNavigateToPlayer: () -> Unit = {},
) {
    val state = rememberPlaylistState(player)
    val currentTrackIdInState = state.currentItem?.mediaId

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            state = lazyListState,
            contentPadding = paddingValues + PaddingValues(bottom = 78.dp) ,
            overscrollEffect = null,
        ) {
            val key: (Int, MediaItem) -> String = { index, mediaItem -> mediaItem.mediaId }
            itemsIndexed(
                items = state.currentPlaylist,
                key = key
            ) { index: Int, track: MediaItem ->
                TrackListItem(
                    trackItem = track.toUIItemByCurrentItem(currentTrackIdInState),
                    onTrackItemClick = { clickedItemId ->

                        if (currentTrackIdInState?.toLong() == clickedItemId) {
                            player.toggleIsPlaying()
                        } else {
                            player.seekTo(index, 0)
                            player.playWhenReady = true
                        }
                    },
                )
            }
        }
        BottomBar(
            modifier = Modifier
                .safeDrawingPadding()
                .align(Alignment.BottomCenter)
                .noRippleClickable(onClick = onNavigateToPlayer),
            player
        )
    }
}


@Composable
fun TrackListItem(
    modifier: Modifier = Modifier,
    trackItem: TrackItem,
    onTrackItemClick: (Long) -> Unit = {},
) {

    val backgroundColor = if (trackItem.isPlaying) {
        Color.LightGray
    } else {
        Color.White
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor
            )
            .clickable(onClick = {
                onTrackItemClick(trackItem.id)
            })
            .padding(vertical = 8.dp, horizontal = 16.dp)

    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(trackItem.imageUri)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_note_24),
            error = painterResource(R.drawable.ic_note_24),
            contentDescription = stringResource(R.string.content_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(Modifier.width(8.dp))
        TrackText(title = trackItem.title, artist = trackItem.artist)
    }
}

@Preview
@Composable
fun TrackListItemPreview() {
    TrackListItem(trackItem = TrackItem(0, "Awesome song", "Awesome artist", ""))
}

private fun MediaItem.toUIItemByCurrentItem(currentTrackMediaId: String?): TrackItem {
    return TrackItem(
        id = mediaId.toLong(),
        title = mediaMetadata.title.toString(),
        artist = mediaMetadata.artist.toString(),
        imageUri = mediaMetadata.artworkUri.toString(),
        isPlaying = mediaId == currentTrackMediaId
    )
}

@Composable
fun BottomBar(
    modifier: Modifier,
    player: Player,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(Color.White.copy(alpha = 0.9f))
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        SmallPlayer(
            player = player
        )
    }
}