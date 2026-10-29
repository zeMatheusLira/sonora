package com.example.sonora.feature.tracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sonora.SonoraApplication
import com.example.sonora.feature.tracks.data.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TracksViewModel(
    private val repository: TracksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TracksUiState>(TracksUiState.Loading)
    val uiState: StateFlow<TracksUiState> = _uiState.asStateFlow()

    init {
        loadTracks()
    }

    /** Busca (ou busca de novo, no "Tentar novamente") as músicas do chart. */
    fun loadTracks() {
        _uiState.value = TracksUiState.Loading
        viewModelScope.launch {
            _uiState.value = repository.getTopTracks().fold(
                onSuccess = { TracksUiState.Success(it) },
                onFailure = { TracksUiState.Error(it.message ?: "Erro desconhecido.") }
            )
        }
    }

    companion object {
        // Factory com injeção manual: pega o repositório do AppContainer
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SonoraApplication
                TracksViewModel(app.container.tracksRepository)
            }
        }
    }
}
