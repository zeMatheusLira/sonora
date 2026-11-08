package com.example.sonora.feature.festivals.data.model

/** Modelo usado pela UI. */
data class Festival(
    val id: Int,
    val name: String,
    val city: String,
    val country: String,
    val region: String,
    val firstEdition: Int,
    val genres: List<String>,
    val description: String,
    val iconicArtists: List<String>,
    val curiosity: String
)
