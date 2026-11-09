package com.example.sonora.feature.playlists.data.remote

import com.example.sonora.feature.playlists.data.remote.dto.ChartPlaylistsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/** Rota da API da Deezer usada pela tela Top Playlists (integrante 4). */
interface PlaylistsApi {

    @GET("chart/0/playlists")
    suspend fun getTopPlaylists(
        @Query("limit") limit: Int = 30
    ): ChartPlaylistsResponse
}
