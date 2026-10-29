package com.example.sonora.feature.decades.ui

import com.example.sonora.feature.decades.data.model.Decade

/** Estados possíveis da tela Décadas da Música. */
sealed interface DecadesUiState {
    data object Loading : DecadesUiState
    data class Success(val decades: List<Decade>) : DecadesUiState
    data class Error(val message: String) : DecadesUiState
}
