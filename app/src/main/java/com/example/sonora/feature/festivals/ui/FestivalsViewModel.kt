package com.example.sonora.feature.festivals.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sonora.SonoraApplication
import com.example.sonora.feature.festivals.data.FestivalsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class FestivalsViewModel(
    repository: FestivalsRepository
) : ViewModel() {

    // Região escolhida nos chips. É só estado de tela: muda a CONSULTA, não altera dados.
    private val selectedRegion = MutableStateFlow<String?>(null)

    // Sempre que o filtro muda, flatMapLatest cancela a consulta anterior e assina a nova no Room
    private val festivals = selectedRegion.flatMapLatest { region ->
        repository.getFestivals(region)
    }

    val uiState: StateFlow<FestivalsUiState> = combine(
        repository.getRegions(),
        selectedRegion,
        festivals
    ) { regions, region, list ->
        FestivalsUiState.Success(regions, region, list) as FestivalsUiState
    }
        .catch { emit(FestivalsUiState.Error("Não foi possível carregar os festivais.")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FestivalsUiState.Loading
        )

    /** Seleciona uma região para filtrar; null mostra todos. */
    fun selectRegion(region: String?) {
        selectedRegion.value = region
    }

    companion object {
        // Factory com injeção manual: pega o repositório do AppContainer
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SonoraApplication
                FestivalsViewModel(app.container.festivalsRepository)
            }
        }
    }
}
