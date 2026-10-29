package com.example.sonora.feature.tracks.ui

import com.example.sonora.feature.tracks.data.model.Track

/** Estados possíveis da tela Top Músicas. */
sealed interface TracksUiState {
    data object Loading : TracksUiState
    data class Success(val tracks: List<Track>) : TracksUiState
    data class Error(val message: String) : TracksUiState
}
