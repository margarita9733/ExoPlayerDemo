package ru.margarita9733.exoplayerdemo.domain

import kotlinx.coroutines.flow.Flow
import ru.margarita9733.exoplayerdemo.data.model.Track

interface AppRepository{

    fun getTracksAsFlow(): Flow<List<Track>>
    suspend fun loadTracks()
}