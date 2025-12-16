package ru.margarita9733.exoplayerdemo.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.extractor.mp3.Mp3Extractor

@OptIn(UnstableApi::class)
fun initializePlayer(context: Context): Player {

    val dataSourceFactory = DefaultDataSource.Factory(context)

    val extractorsFactory = ExtractorsFactory {
        arrayOf(
            Mp3Extractor()
        )
    }

    val renderersFactory = RenderersFactory { eventHandler, _, rendererListener, _, _ ->
        arrayOf(
            MediaCodecAudioRenderer(
                context,
                MediaCodecSelector.DEFAULT,
                eventHandler,
                rendererListener
            )
        )
    }

    val mediaSourceFactory =
        ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory)


    val audioAttributes =
        AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

    val handleAudioFocus = true

    val exoPlayer = ExoPlayer.Builder(context)
        .setRenderersFactory(renderersFactory)
        .setMediaSourceFactory(mediaSourceFactory)
        .setHandleAudioBecomingNoisy(true)
        .build()

    exoPlayer
        .setAudioAttributes(audioAttributes, handleAudioFocus)

    exoPlayer.prepare()
    return exoPlayer
}

enum class AudioAccessPermissionState {
    GRANTED,
    DENIED,
    UNKNOWN
}

fun getReadAudioPermissionTypeBySDK(): String {
    return if (Build.VERSION.SDK_INT <= 32) {
        Manifest.permission.READ_EXTERNAL_STORAGE
    } else {
        Manifest.permission.READ_MEDIA_AUDIO
    }
}

fun Context.isReadAudioPermissionGranted(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        getReadAudioPermissionTypeBySDK()
    ) == PackageManager.PERMISSION_GRANTED
}

fun Player.toggleIsPlaying() {
    run {
        if (isPlaying) {
            pause()
            playWhenReady = false
        } else {
            play()
        }
    }
}

fun Player.getCurrentTrackImageUriOrNull(): Uri? = currentMediaItem?.mediaMetadata?.artworkUri

fun Player.getCurrentTrackArtistOrNull(): String? =
    currentMediaItem?.mediaMetadata?.artist?.toString()

fun Player.getCurrentTrackTitleOrNull(): String? =
    currentMediaItem?.mediaMetadata?.title?.toString()

fun Player.getCurrentMediaItems(): List<MediaItem> {

    return List(mediaItemCount) { index ->
        getMediaItemAt(index)
    }
}
