package com.example.music.domain.usecases

import com.example.music.domain.repositories.MusicRepository
import com.example.music.domain.entities.Track

class GetCombinedTracksUseCase(private val repository: MusicRepository) {
    suspend fun execute(): List<Track> {
        val local = repository.getLocalTracks()
        val remote = repository.getServerTracks()
        return local + remote
    }
}