package com.example.sonora.feature.instruments.data.model

/** Modelo usado pela UI. */
data class Instrument(
    val id: Int,
    val name: String,
    val family: String,
    val origin: String,
    val description: String,
    val famousPlayers: List<String>,
    val curiosity: String
)
