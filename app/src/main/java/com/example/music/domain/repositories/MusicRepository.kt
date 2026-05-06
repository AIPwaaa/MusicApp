package com.example.music.domain.repositories

import com.example.music.domain.entities.Track

// Domain layer: Repository Interface
interface MusicRepository {
    // Получить список всех доступных треков (с сервера)
    suspend fun getServerTracks(): List<Track>

    // Получить список треков с устройства (через MediaStore)
    suspend fun getLocalTracks(): List<Track>

    // Загрузить трек на сервер
    suspend fun uploadTrack(track: Track): Result<Unit>

    // Скачать трек в память устройства
    suspend fun downloadTrack(track: Track): Result<String>
}