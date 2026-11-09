package com.example.sonora.feature.playlists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sonora.SonoraApplication
import com.example.sonora.feature.playlists.data.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val repository: PlaylistsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlaylistsUiState>(PlaylistsUiState.Loading)
    val uiState: StateFlow<PlaylistsUiState> = _uiState.asStateFlow()

    init {
        loadPlaylists()
    }

    /** Busca (ou busca de novo, no "Tentar novamente") as playlists do chart. */
    fun loadPlaylists() {
        _uiState.value = PlaylistsUiState.Loading
        viewModelScope.launch {
            _uiState.value = repository.getTopPlaylists().fold(
                onSuccess = { PlaylistsUiState.Success(it) },
                onFailure = { PlaylistsUiState.Error(it.message ?: "Erro desconhecido.") }
            )
        }
    }

    companion object {
        // Factory com injeção manual: pega o repositório do AppContainer
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SonoraApplication
                PlaylistsViewModel(app.container.playlistsRepository)
            }
        }
    }
}
