package com.example.sonora.feature.playlists.ui

import com.example.sonora.feature.playlists.data.model.Playlist

/** Estados possíveis da tela Top Playlists. */
sealed interface PlaylistsUiState {
    data object Loading : PlaylistsUiState
    data class Success(val playlists: List<Playlist>) : PlaylistsUiState
    data class Error(val message: String) : PlaylistsUiState
}
