package com.example.music.domain.entities

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val url: String,
    val isLocal: Boolean,
    val localUri: String? = null
)