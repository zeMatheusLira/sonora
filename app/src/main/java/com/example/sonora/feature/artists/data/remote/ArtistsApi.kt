package com.example.sonora.feature.artists.data.remote

import com.example.sonora.feature.artists.data.remote.dto.ChartArtistsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/** Rota da API da Deezer usada pela tela Top Artistas. */
interface ArtistsApi {

    @GET("chart/0/artists")
    suspend fun getTopArtists(
        @Query("limit") limit: Int = 30
    ): ChartArtistsResponse
}
