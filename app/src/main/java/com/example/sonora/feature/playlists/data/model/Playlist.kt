package com.example.sonora.feature.playlists.data.model

/** Modelo usado pela UI, já sem os detalhes do JSON da Deezer. */
data class Playlist(
    val id: Long,
    val position: Int,
    val title: String,
    val trackCount: Int,
    /** Nome de quem montou a playlist (geralmente um editor da Deezer). */
    val curator: String,
    val coverUrl: String?
)
