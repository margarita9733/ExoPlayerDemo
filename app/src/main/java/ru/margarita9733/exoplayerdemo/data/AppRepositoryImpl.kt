package ru.margarita9733.exoplayerdemo.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import ru.margarita9733.exoplayerdemo.data.model.Track
import ru.margarita9733.exoplayerdemo.domain.AppRepository

class AppRepositoryImpl(
    private val appContext: Context,
    private val ioDispatcher: CoroutineDispatcher
) : AppRepository {

    private val _tracksFlow: MutableStateFlow<List<Track>> = MutableStateFlow(emptyList())
    val tracksFlow: Flow<List<Track>> = _tracksFlow

    override fun getTracksAsFlow(): Flow<List<Track>> = tracksFlow

    override suspend fun loadTracks() {
        _tracksFlow.value = getUserTracks()
    }

    private suspend fun getUserTracks(): List<Track> =
        withContext(ioDispatcher) {

            val tracks = mutableListOf<Track>()

            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Audio.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.MIME_TYPE
            )

            val selection =
                "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val selectionArgs = null
            val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

            appContext.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val albumIdColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)

                while (cursor.moveToNext()) {
                    val mimeType = cursor.getString(mimeTypeColumn)

                    if (mimeType == "audio/mpeg") {// отфильтровать только mp3
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn)
                        val artist = cursor.getString(artistColumn)
                        val duration = cursor.getInt(durationColumn)
                        val albumId = cursor.getLong(albumIdColumn)

                        val contentUri: Uri = ContentUris.withAppendedId(
                            collection,
                            id
                        )

                        val imageUriBase = "content://media/external/audio/albumart".toUri()
                        val imageUri = ContentUris.withAppendedId(imageUriBase, albumId)

                        tracks.add(
                            Track(
                                id = id,
                                uri = contentUri.toString(),
                                title = name.substringBeforeLast('.'), // убрать формат
                                artist = artist,
                                duration = duration,
                                imageUri = imageUri.toString()
                            )
                        )
                    }
                }
            }
            return@withContext tracks
        }
}
