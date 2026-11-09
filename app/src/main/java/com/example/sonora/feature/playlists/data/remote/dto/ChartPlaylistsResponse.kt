package com.example.sonora.feature.playlists.data.remote.dto

import com.google.gson.annotations.SerializedName

/*
 * DTOs criados a partir da resposta real de GET https://api.deezer.com/chart/0/playlists.
 * Mapeamos só os campos usados pelo app; o Gson ignora o resto.
 */

data class ChartPlaylistsResponse(
    @SerializedName("data") val data: List<ChartPlaylistDto>?,
    @SerializedName("total") val total: Int?,
    // A Deezer devolve HTTP 200 com este objeto quando algo dá errado
    @SerializedName("error") val error: PlaylistsErrorDto?
)

data class ChartPlaylistDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("nb_tracks") val nbTracks: Int?,
    @SerializedName("picture_medium") val pictureMedium: String?,
    @SerializedName("picture_big") val pictureBig: String?,
    @SerializedName("user") val user: PlaylistUserDto?
)

data class PlaylistUserDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?
)

data class PlaylistsErrorDto(
    @SerializedName("type") val type: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("code") val code: Int?
)
