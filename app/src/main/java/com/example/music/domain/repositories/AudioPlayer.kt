package com.example.music.domain.repositories

import com.example.music.domain.entities.Track
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayer {
    val isPlaying: StateFlow<Boolean>
    val currentTrack: StateFlow<Track?>
    val duration: StateFlow<Long>
    val currentPosition: StateFlow<Long>
    val shuffleModeEnabled: StateFlow<Boolean>
    val repeatModeEnabled: StateFlow<Boolean>

    fun addTracks(tracks: List<Track>)
    fun playTrack(index: Int)
    fun pause()
    fun resume()
    fun stop()
    fun next()
    fun previous()
    fun seekTo(position: Long)
    fun toggleShuffle()
    fun toggleRepeat()
}
