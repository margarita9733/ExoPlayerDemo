package ru.margarita9733.exoplayerdemo.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val id: Long,
    val uri: String = "",
    val title: String ="Unknown title",
    val artist: String = "Unknown artist",
    val duration: Int = 0,
    val imageUri: String =""
)