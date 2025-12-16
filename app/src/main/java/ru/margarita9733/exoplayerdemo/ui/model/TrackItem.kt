package ru.margarita9733.exoplayerdemo.ui.model

data class TrackItem(
    val id: Long,
    val title: String,
    val artist: String,
    val imageUri: String,
    val isPlaying: Boolean = false
)

