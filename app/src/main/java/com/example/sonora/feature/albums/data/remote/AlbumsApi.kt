package com.example.sonora.feature.albums.data.remote

import com.example.sonora.feature.albums.data.remote.dto.ChartAlbumsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/** Rota da API da Deezer usada pela tela Top Álbuns (integrante 3). */
interface AlbumsApi {

    @GET("chart/0/albums")
    suspend fun getTopAlbums(
        @Query("limit") limit: Int = 30
    ): ChartAlbumsResponse
}
