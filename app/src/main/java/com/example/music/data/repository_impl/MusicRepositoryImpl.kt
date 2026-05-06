package com.example.music.data.repository_impl

import com.example.music.data.local.MediaStoreTracker
import com.example.music.domain.repositories.MusicRepository
import com.example.music.domain.entities.Track

class MusicRepositoryImpl(
    private val mediaStoreTracker: MediaStoreTracker
) : MusicRepository {
    override suspend fun getLocalTracks(): List<Track> {
        return mediaStoreTracker.fetchLocalTracks()
    }

    override suspend fun getServerTracks(): List<Track> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadTrack(track: Track): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun downloadTrack(track: Track): Result<String> {
        TODO("Not yet implemented")
    }
}