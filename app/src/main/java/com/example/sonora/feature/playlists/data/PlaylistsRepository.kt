package com.example.sonora.feature.playlists.data

import com.example.sonora.feature.playlists.data.model.Playlist
import com.example.sonora.feature.playlists.data.remote.PlaylistsApi
import com.example.sonora.feature.playlists.data.remote.dto.ChartPlaylistDto
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Busca as playlists em alta na Deezer.
 * Todas as exceções de rede são tratadas aqui e viram Result.failure com mensagem amigável.
 */
class PlaylistsRepository(
    private val api: PlaylistsApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    // withContext(ioDispatcher): a requisição roda numa thread de I/O, fora da thread principal
    suspend fun getTopPlaylists(): Result<List<Playlist>> = withContext(ioDispatcher) {
        try {
            val response = api.getTopPlaylists()
            val error = response.error
            if (error != null) {
                Result.failure(PlaylistsException("A Deezer retornou um erro: ${error.message ?: error.type}"))
            } else {
                val playlists = response.data.orEmpty().mapIndexed { index, dto -> dto.toPlaylist(index) }
                Result.success(playlists)
            }
        } catch (e: CancellationException) {
            // Cancelamento de coroutine não é erro: repassa para não quebrar a concorrência estruturada
            throw e
        } catch (e: IOException) {
            Result.failure(PlaylistsException("Sem conexão com a internet. Verifique sua rede.", e))
        } catch (e: HttpException) {
            Result.failure(PlaylistsException("O servidor respondeu com erro (HTTP ${e.code()}).", e))
        } catch (e: JsonParseException) {
            Result.failure(PlaylistsException("Não foi possível ler a resposta da Deezer.", e))
        } catch (e: Exception) {
            Result.failure(PlaylistsException("Erro inesperado ao carregar as playlists.", e))
        }
    }

    // Converte o DTO da API no modelo usado pela UI. A rota não traz "position", então usamos a ordem.
    private fun ChartPlaylistDto.toPlaylist(index: Int) = Playlist(
        id = id,
        position = index + 1,
        title = title.orEmpty(),
        trackCount = nbTracks ?: 0,
        curator = user?.name.orEmpty(),
        coverUrl = pictureBig ?: pictureMedium
    )
}

/** Erro da feature com mensagem pronta para exibir ao usuário. */
class PlaylistsException(message: String, cause: Throwable? = null) : Exception(message, cause)
