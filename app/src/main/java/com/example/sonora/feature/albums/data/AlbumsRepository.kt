package com.example.sonora.feature.albums.data

import com.example.sonora.feature.albums.data.model.Album
import com.example.sonora.feature.albums.data.remote.AlbumsApi
import com.example.sonora.feature.albums.data.remote.dto.ChartAlbumDto
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Busca os álbuns mais ouvidos na Deezer.
 * Todas as exceções de rede são tratadas aqui e viram Result.failure com mensagem amigável.
 */
class AlbumsRepository(
    private val api: AlbumsApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    // withContext(ioDispatcher): a requisição roda numa thread de I/O, fora da thread principal
    suspend fun getTopAlbums(): Result<List<Album>> = withContext(ioDispatcher) {
        try {
            val response = api.getTopAlbums()
            val error = response.error
            if (error != null) {
                Result.failure(AlbumsException("A Deezer retornou um erro: ${error.message ?: error.type}"))
            } else {
                val albums = response.data.orEmpty().mapIndexed { index, dto -> dto.toAlbum(index) }
                Result.success(albums)
            }
        } catch (e: CancellationException) {
            // Cancelamento de coroutine não é erro: repassa para não quebrar a concorrência estruturada
            throw e
        } catch (e: IOException) {
            Result.failure(AlbumsException("Sem conexão com a internet. Verifique sua rede.", e))
        } catch (e: HttpException) {
            Result.failure(AlbumsException("O servidor respondeu com erro (HTTP ${e.code()}).", e))
        } catch (e: JsonParseException) {
            Result.failure(AlbumsException("Não foi possível ler a resposta da Deezer.", e))
        } catch (e: Exception) {
            Result.failure(AlbumsException("Erro inesperado ao carregar os álbuns.", e))
        }
    }

    // Converte o DTO da API no modelo usado pela UI
    private fun ChartAlbumDto.toAlbum(index: Int) = Album(
        id = id,
        position = position ?: (index + 1),
        title = title.orEmpty(),
        artistName = artist?.name.orEmpty(),
        recordType = recordType ?: "album",
        explicit = explicitLyrics == true,
        coverUrl = coverBig ?: coverMedium
    )
}

/** Erro da feature com mensagem pronta para exibir ao usuário. */
class AlbumsException(message: String, cause: Throwable? = null) : Exception(message, cause)
