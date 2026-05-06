package com.example.music.data.local

import android.content.Context
import android.provider.MediaStore
import androidx.compose.runtime.neverEqualPolicy
import com.example.music.domain.entities.Track

class MediaStoreTracker(private val context: Context) {
    fun fetchLocalTracks(): List<Track> {
        val tracks = mutableListOf<Track>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.DURATION} >= ?"
        val selectionArgs = arrayOf("30000")

        val query = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Audio.Media.TITLE} ASC"
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (cursor.moveToNext()) {
                tracks.add(
                    Track(
                        id = cursor.getLong(idColumn).toString(),
                        title = cursor.getString(titleColumn),
                        artist = cursor.getString(artistColumn),
                        duration = cursor.getLong(durationColumn),
                        url = cursor.getString(dataColumn),
                        isLocal = true
                    )
                )
            }
        }
        return tracks
    }
}