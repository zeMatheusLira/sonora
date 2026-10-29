package com.example.sonora.feature.decades.data.model

/** Modelo usado pela UI, com gêneros e artistas já separados em listas. */
data class Decade(
    val id: Int,
    val label: String,
    val startYear: Int,
    val description: String,
    val topGenres: List<String>,
    val iconicArtists: List<String>,
    val curiosity: String
)
