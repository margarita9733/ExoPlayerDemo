package ru.margarita9733.exoplayerdemo.ui

import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import ru.margarita9733.exoplayerdemo.utils.initializePlayer

class PlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()

        val player = initializePlayer(this)

        mediaSession = MediaSession.Builder(this, player)
            .build()

    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {

        return when (controllerInfo.packageName) {
            "ru.margarita9733.exoplayerdemo",
            "com.android.systemui" -> mediaSession

            else -> null
        }

    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}