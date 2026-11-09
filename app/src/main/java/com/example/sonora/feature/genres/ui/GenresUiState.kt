package com.example.sonora.feature.genres.ui

import com.example.sonora.feature.genres.data.model.Genre

/** Estados possíveis da tela Gêneros Musicais. */
sealed interface GenresUiState {
    data object Loading : GenresUiState

    /** @param genres gêneros que combinam com a busca atual (todos, se a busca estiver vazia) */
    data class Success(val genres: List<Genre>) : GenresUiState

    data class Error(val message: String) : GenresUiState
}
