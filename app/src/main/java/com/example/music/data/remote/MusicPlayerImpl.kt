package com.example.music.data.remote

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import androidx.media3.session.SessionToken
import com.example.music.domain.entities.Track
import com.example.music.domain.repositories.AudioPlayer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicPlayerImpl(
    private val context: Context
) : AudioPlayer {

    private var browser: MediaBrowser? = null

    private val sessionToken = SessionToken(
        context,
        ComponentName(context, MusicService::class.java)
    )

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying = _isPlaying.asStateFlow()

    private val _currentTrack = MutableStateFlow<Track?>(null)
    override val currentTrack = _currentTrack.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    override val duration = _duration.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    override val currentPosition = _currentPosition.asStateFlow()

    private val _shuffleModeEnabled = MutableStateFlow(false)
    override val shuffleModeEnabled = _shuffleModeEnabled.asStateFlow()

    private val _repeatModeEnabled = MutableStateFlow(false)
    override val repeatModeEnabled = _repeatModeEnabled.asStateFlow()

    private var tracksList: List<Track> = emptyList()
    private var job: Job? = null

    init {
        val controllerFuture = MediaBrowser.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                browser = controllerFuture.get().apply {
                    addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            _isPlaying.value = isPlaying
                            if (isPlaying) {
                                startUpdatingPosition()
                            } else {
                                stopUpdatingPosition()
                            }
                        }

                        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                            _currentTrack.value = tracksList.find { it.id.toString() == mediaItem?.mediaId }
                            _duration.value = browser?.duration?.coerceAtLeast(0L) ?: 0L
                        }

                        override fun onPlaybackStateChanged(playbackState: Int) {
                            if (playbackState == Player.STATE_READY) {
                                _duration.value = browser?.duration?.coerceAtLeast(0L) ?: 0L
                            }
                        }

                        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                            _shuffleModeEnabled.value = shuffleModeEnabled
                        }

                        override fun onRepeatModeChanged(repeatMode: Int) {
                            _repeatModeEnabled.value = repeatMode == Player.REPEAT_MODE_ONE
                        }
                    })
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun startUpdatingPosition() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                browser?.let {
                    _currentPosition.value = it.currentPosition
                }
                delay(1000)
            }
        }
    }

    private fun stopUpdatingPosition() {
        job?.cancel()
    }

    override fun addTracks(tracks: List<Track>) {
        this.tracksList = tracks
        val mediaItems = tracks.map { track ->
            MediaItem.Builder()
                .setMediaId(track.id.toString())
                .setUri(track.url)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(track.artist)
                        .build()
                )
                .build()
        }
        browser?.setMediaItems(mediaItems)
        browser?.prepare()
    }

    override fun playTrack(index: Int) {
        browser?.seekTo(index, 0)
        browser?.play()
    }

    override fun pause() { browser?.pause() }
    override fun resume() { browser?.play() }
    override fun stop() { browser?.stop() }

    override fun next() {
        browser?.seekToNext()
    }

    override fun previous() {
        browser?.let {
            if (it.currentPosition < 6700) {
                it.seekToPreviousMediaItem()
            } else {
                it.seekTo(0)
            }
        }
    }

    override fun seekTo(position: Long) {
        browser?.seekTo(position)
    }

    override fun toggleShuffle() {
        browser?.shuffleModeEnabled = !(browser?.shuffleModeEnabled ?: false)
    }

    override fun toggleRepeat() {
        browser?.let {
            it.repeatMode = if (it.repeatMode == Player.REPEAT_MODE_ONE) {
                Player.REPEAT_MODE_OFF
            } else {
                Player.REPEAT_MODE_ONE
            }
        }
    }
}
