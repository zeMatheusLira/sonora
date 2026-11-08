package com.example.sonora.feature.albums.ui

import com.example.sonora.feature.albums.data.model.Album

/** Estados possíveis da tela Top Álbuns. */
sealed interface AlbumsUiState {
    data object Loading : AlbumsUiState
    data class Success(val albums: List<Album>) : AlbumsUiState
    data class Error(val message: String) : AlbumsUiState
}
