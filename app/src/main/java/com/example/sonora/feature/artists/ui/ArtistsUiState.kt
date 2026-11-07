package com.example.sonora.feature.artists.ui

import com.example.sonora.feature.artists.data.model.Artist

/** Estados possíveis da tela Top Artistas. */
sealed interface ArtistsUiState {
    data object Loading : ArtistsUiState
    data class Success(val artists: List<Artist>) : ArtistsUiState
    data class Error(val message: String) : ArtistsUiState
}
