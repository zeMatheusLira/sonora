package com.example.sonora.feature.tracks.data.remote

import com.example.sonora.feature.tracks.data.remote.dto.ChartTracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

/** Rota da API da Deezer usada pelo integrante 1 (Top Músicas). */
interface DeezerApi {

    @GET("chart/0/tracks")
    suspend fun getTopTracks(
        @Query("limit") limit: Int = 50
    ): ChartTracksResponse
}
