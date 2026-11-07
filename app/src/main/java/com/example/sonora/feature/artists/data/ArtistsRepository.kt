package com.example.sonora.feature.artists.data

import com.example.sonora.feature.artists.data.model.Artist
import com.example.sonora.feature.artists.data.remote.ArtistsApi
import com.example.sonora.feature.artists.data.remote.dto.ChartArtistDto
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Busca os artistas mais ouvidos na Deezer.
 * Todas as exceções de rede são tratadas aqui e viram Result.failure com mensagem amigável.
 */
class ArtistsRepository( // ´passado pelo retrodfit para o app container
    private val api: ArtistsApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) { //criacao da api e 2 depdencias api(req. deezer) e IODIspacther(defiine o contexto de onde sera executado)

    suspend fun getTopArtists(): Result<List<Artist>> = withContext(ioDispatcher) { //suspense execucao enquanto aguarda operaco assincrona + busca os artistas  array de artistas
        try {  // withContext(ioDispatcher): executa a operação no contexto de entrada e saída, adequado para requisições de rede.

            val response = api.getTopArtists() //Chama o metodo getTopArtists() da interface ArtistsApi.
            val error = response.error
            if (error != null) {
                Result.failure(ArtistsException("A Deezer retornou um erro: ${error.message ?: error.type}")) //O operador ?: significa que, se error.message for nulo, será utilizado error.type.
            } else {
                val artists = response.data.orEmpty().mapIndexed { index, dto -> dto.toArtist(index) }
                Result.success(artists) //retorna lista dos artistas
            }
        } catch (e: CancellationException) {
            // Cancelamento de coroutine não é erro: repassa para não quebrar a concorrência estruturada
            throw e
        } catch (e: IOException) {
            Result.failure(ArtistsException("Sem conexão com a internet. Verifique sua rede.", e))
        } catch (e: HttpException) {
            Result.failure(ArtistsException("O servidor respondeu com erro (HTTP ${e.code()}).", e))
        } catch (e: JsonParseException) {
            Result.failure(ArtistsException("Não foi possível ler a resposta da Deezer.", e))
        } catch (e: Exception) {
            Result.failure(ArtistsException("Erro inesperado ao carregar os artistas.", e))
        }
    }

    private fun ChartArtistDto.toArtist(index: Int) = Artist( // Essa função transforma os dados recebidos da API no modelo utilizado pelo aplicativo.
        id = id,
        position = position ?: (index + 1),
        name = name.orEmpty(),
        pictureUrl = pictureBig ?: pictureMedium
    )
}

/** Erro da feature com mensagem pronta para exibir ao usuário. */
class ArtistsException(message: String, cause: Throwable? = null) : Exception(message, cause)
