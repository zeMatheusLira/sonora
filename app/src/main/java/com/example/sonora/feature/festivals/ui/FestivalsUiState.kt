package com.example.sonora.feature.festivals.ui

import com.example.sonora.feature.festivals.data.model.Festival

/** Estados possíveis da tela Festivais de Música. */
sealed interface FestivalsUiState {
    data object Loading : FestivalsUiState

    /**
     * @param regions regiões disponíveis para filtrar (vindas do banco)
     * @param selectedRegion região selecionada; null = "Todos"
     */
    data class Success(
        val regions: List<String>,
        val selectedRegion: String?,
        val festivals: List<Festival>
    ) : FestivalsUiState

    data class Error(val message: String) : FestivalsUiState
}
