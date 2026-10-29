package com.example.sonora.feature.decades.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sonora.SonoraApplication
import com.example.sonora.feature.decades.data.DecadesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DecadesViewModel(
    repository: DecadesRepository
) : ViewModel() {

    // Converte o Flow do Room em StateFlow; para de coletar 5s depois que a tela sai de cena
    val uiState: StateFlow<DecadesUiState> = repository.getAllDecades()
        .map<_, DecadesUiState> { DecadesUiState.Success(it) }
        .catch { emit(DecadesUiState.Error("Não foi possível carregar as décadas.")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DecadesUiState.Loading
        )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SonoraApplication
                DecadesViewModel(app.container.decadesRepository)
            }
        }
    }
}
