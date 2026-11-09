package com.example.sonora.feature.genres.data.model

/** Modelo usado pela UI. */
data class Genre(
    val id: Int,
    val name: String,
    val originCountry: String,
    val originYear: Int,
    val roots: List<String>,
    val description: String,
    /** Faixa típica de andamento (batidas por minuto). */
    val bpmRange: IntRange,
    val keyArtists: List<String>,
    val curiosity: String
)
