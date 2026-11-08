package com.example.sonora.feature.albums.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sonora.SonoraApplication
import com.example.sonora.feature.albums.data.AlbumsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumsViewModel(
    private val repository: AlbumsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumsUiState>(AlbumsUiState.Loading)
    val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

    init {
        loadAlbums()
    }

    /** Busca (ou busca de novo, no "Tentar novamente") os álbuns do chart. */
    fun loadAlbums() {
        _uiState.value = AlbumsUiState.Loading
        viewModelScope.launch {
            _uiState.value = repository.getTopAlbums().fold(
                onSuccess = { AlbumsUiState.Success(it) },
                onFailure = { AlbumsUiState.Error(it.message ?: "Erro desconhecido.") }
            )
        }
    }

    companion object {
        // Factory com injeção manual: pega o repositório do AppContainer
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SonoraApplication
                AlbumsViewModel(app.container.albumsRepository)
            }
        }
    }
}
