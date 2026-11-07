package com.example.sonora.feature.artists.data.model

/** Modelo usado pela UI, já sem os detalhes do JSON da Deezer. */
data class Artist(
    val id: Long,
    val position: Int,
    val name: String,
    val pictureUrl: String?
)
