package com.example.sonora.feature.genres.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sonora.SonoraApplication
import com.example.sonora.feature.genres.data.GenresRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class GenresViewModel(
    repository: GenresRepository
) : ViewModel() {

    // Texto do campo de busca. É só estado de tela: muda a CONSULTA, não altera dados.
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val uiState: StateFlow<GenresUiState> = _query
        // Espera o usuário parar de digitar antes de consultar o banco
        .debounce(250)
        .map { it.trim() }
        .distinctUntilChanged()
        // A cada busca nova, flatMapLatest cancela a consulta anterior e assina a nova no Room
        .flatMapLatest { term -> repository.getGenres(term) }
        .map { GenresUiState.Success(it) as GenresUiState }
        .catch { emit(GenresUiState.Error("Não foi possível carregar os gêneros.")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GenresUiState.Loading
        )

    fun onQueryChange(text: String) {
        _query.value = text
    }

    companion object {
        // Factory com injeção manual: pega o repositório do AppContainer
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as SonoraApplication
                GenresViewModel(app.container.genresRepository)
            }
        }
    }
}
