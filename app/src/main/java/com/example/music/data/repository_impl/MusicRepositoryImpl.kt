package com.example.music.data.repository_impl

import com.example.music.data.local.MediaStoreTracker
import com.example.music.domain.repositories.MusicRepository
import com.example.music.domain.entities.Track
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import android.util.Log

class MusicRepositoryImpl(
    private val mediaStoreTracker: MediaStoreTracker,
    private val httpClient: HttpClient
) : MusicRepository {
    
    private val baseUrl = "http://10.0.2.2:8080"

    override suspend fun getLocalTracks(): List<Track> {
        return mediaStoreTracker.fetchLocalTracks()
    }

    override suspend fun getServerTracks(): List<Track> {
        return try {
            val response = httpClient.get("$baseUrl/tracks")
            val tracks = response.body<List<Track>>()
            if (tracks.isEmpty()) {
                Log.d("MusicRepository", "Successfully connected to $baseUrl/tracks, but the track list is EMPTY.")
            } else {
                Log.d("MusicRepository", "Successfully fetched ${tracks.size} tracks from server.")
            }
            tracks
        } catch (e: Exception) {
            Log.e("MusicRepository", "CONNECTION ERROR: Could not reach server at $baseUrl/tracks. Reason: ${e.message}")
            emptyList()
        }
    }

    override suspend fun uploadTrack(track: Track): Result<Unit> {
        return try {
            httpClient.post("$baseUrl/upload") {
                setBody(track)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downloadTrack(track: Track): Result<String> {
        TODO("Not yet implemented")
    }
}
