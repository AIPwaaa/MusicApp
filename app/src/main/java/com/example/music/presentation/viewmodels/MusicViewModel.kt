package com.example.music.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music.domain.entities.Track
import com.example.music.domain.repositories.AudioPlayer
import com.example.music.domain.usecases.GetCombinedTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel(
    private val getCombinedTracksUseCase: GetCombinedTracksUseCase,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> = _tracks.asStateFlow()

    val isPlaying = audioPlayer.isPlaying
    val currentTrack = audioPlayer.currentTrack
    val duration = audioPlayer.duration
    val currentPosition = audioPlayer.currentPosition
    val shuffleModeEnabled = audioPlayer.shuffleModeEnabled
    val repeatModeEnabled = audioPlayer.repeatModeEnabled

    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun loadTracks() {
        viewModelScope.launch {
            _tracks.value = getCombinedTracksUseCase.execute()
        }
    }

    fun onTrackClick(index: Int, tracks: List<Track>) {
        audioPlayer.addTracks(tracks)
        audioPlayer.playTrack(index)
    }

    fun togglePlay() {
        if (isPlaying.value) audioPlayer.pause() else audioPlayer.resume()
    }

    fun next() = audioPlayer.next()
    fun previous() = audioPlayer.previous()
    fun seekTo(position: Long) = audioPlayer.seekTo(position)
    fun toggleShuffle() = audioPlayer.toggleShuffle()
    fun toggleRepeat() = audioPlayer.toggleRepeat()
}
