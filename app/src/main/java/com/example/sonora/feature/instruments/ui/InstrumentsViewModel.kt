package com.example.sonora.feature.instruments.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sonora.SonoraApplication
import com.example.sonora.feature.instruments.data.InstrumentsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class InstrumentsViewModel(
    repository: InstrumentsRepository
) : ViewModel() {

    // Filtro escolhido nos chips. É só estado de tela: muda a CONSULTA, não altera dados.
    private val selectedFamily = MutableStateFlow<String?>(null)

    // Sempre que o filtro muda, flatMapLatest cancela a consulta anterior e assina a nova no Room
    private val instruments = selectedFamily.flatMapLatest { family ->
        repository.getInstruments(family)
    }

    val uiState: StateFlow<InstrumentsUiState> = combine(
        repository.getFamilies(),
        selectedFamily,
        instruments
    ) { families, family, list ->
        InstrumentsUiState.Success(families, family, list) as InstrumentsUiState
    }
        .catch { emit(InstrumentsUiState.Error("Não foi possível carregar os instrumentos.")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InstrumentsUiState.Loading
        )

    /** Seleciona uma família para filtrar; null mostra todos. */
    fun selectFamily(family: String?) {
        selectedFamily.value = family
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SonoraApplication
                InstrumentsViewModel(app.container.instrumentsRepository)
            }
        }
    }
}
