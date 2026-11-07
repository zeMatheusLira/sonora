package com.example.sonora.feature.instruments.ui

import com.example.sonora.feature.instruments.data.model.Instrument

/** Estados possíveis da tela Instrumentos Musicais. */
sealed interface InstrumentsUiState {
    data object Loading : InstrumentsUiState

    /**
     * @param families famílias disponíveis para filtrar (vindas do banco)
     * @param selectedFamily família selecionada; null = "Todos"
     */
    data class Success(
        val families: List<String>,
        val selectedFamily: String?,
        val instruments: List<Instrument>
    ) : InstrumentsUiState

    data class Error(val message: String) : InstrumentsUiState
}
