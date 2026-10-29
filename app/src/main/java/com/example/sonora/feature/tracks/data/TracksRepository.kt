package com.example.sonora.feature.tracks.data

import com.example.sonora.feature.tracks.data.model.Track
import com.example.sonora.feature.tracks.data.remote.DeezerApi
import com.example.sonora.feature.tracks.data.remote.dto.TrackDto
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Busca as músicas mais tocadas na Deezer.
 * Todas as exceções de rede são tratadas aqui e viram Result.failure com mensagem amigável.
 */
class TracksRepository(
    private val api: DeezerApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getTopTracks(): Result<List<Track>> = withContext(ioDispatcher) {
        try {
            val response = api.getTopTracks()
            val error = response.error
            if (error != null) {
                Result.failure(TracksException("A Deezer retornou um erro: ${error.message ?: error.type}"))
            } else {
                val tracks = response.data.orEmpty().mapIndexed { index, dto -> dto.toTrack(index) }
                Result.success(tracks)
            }
        } catch (e: CancellationException) {
            // Cancelamento de coroutine não é erro: repassa para não quebrar a concorrência estruturada
            throw e
        } catch (e: IOException) {
            Result.failure(TracksException("Sem conexão com a internet. Verifique sua rede.", e))
        } catch (e: HttpException) {
            Result.failure(TracksException("O servidor respondeu com erro (HTTP ${e.code()}).", e))
        } catch (e: JsonParseException) {
            Result.failure(TracksException("Não foi possível ler a resposta da Deezer.", e))
        } catch (e: Exception) {
            Result.failure(TracksException("Erro inesperado ao carregar as músicas.", e))
        }
    }

    private fun TrackDto.toTrack(index: Int) = Track(
        id = id,
        position = position ?: (index + 1),
        title = title.orEmpty(),
        artistName = artist?.name.orEmpty(),
        albumTitle = album?.title.orEmpty(),
        coverUrl = album?.coverBig ?: album?.coverMedium,
        durationSeconds = duration ?: 0,
        previewUrl = preview
    )
}

/** Erro da feature com mensagem pronta para exibir ao usuário. */
class TracksException(message: String, cause: Throwable? = null) : Exception(message, cause)
