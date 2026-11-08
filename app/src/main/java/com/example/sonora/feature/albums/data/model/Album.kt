package com.example.sonora.feature.albums.data.model

/** Modelo usado pela UI, já sem os detalhes do JSON da Deezer. */
data class Album(
    val id: Long,
    val position: Int,
    val title: String,
    val artistName: String,
    /** "album", "single", "ep" ou "compile", como vem da Deezer. */
    val recordType: String,
    val explicit: Boolean,
    val coverUrl: String?
)
