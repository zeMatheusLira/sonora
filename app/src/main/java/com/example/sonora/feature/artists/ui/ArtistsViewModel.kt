package com.example.sonora.feature.artists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sonora.SonoraApplication
import com.example.sonora.feature.artists.data.ArtistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtistsViewModel(
    private val repository: ArtistsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArtistsUiState>(ArtistsUiState.Loading)
    val uiState: StateFlow<ArtistsUiState> = _uiState.asStateFlow()

    init {
        loadArtists()
    }

    /** Busca (ou busca de novo, no "Tentar novamente") os artistas do chart. */
    fun loadArtists() {
        _uiState.value = ArtistsUiState.Loading
        viewModelScope.launch {
            _uiState.value = repository.getTopArtists().fold(
                onSuccess = { ArtistsUiState.Success(it) },
                onFailure = { ArtistsUiState.Error(it.message ?: "Erro desconhecido.") }
            )
        }
    }

    companion object {
        // Factory com injeção manual: pega o repositório do AppContainer
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SonoraApplication
                ArtistsViewModel(app.container.artistsRepository)
            }
        }
    }
}
