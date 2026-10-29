package com.example.sonora.feature.tracks.data.remote.dto

import com.google.gson.annotations.SerializedName

/*
 * DTOs criados a partir da resposta real de GET https://api.deezer.com/chart/0/tracks.
 * Mapeamos só os campos usados pelo app; o Gson ignora o resto.
 */

data class ChartTracksResponse(
    @SerializedName("data") val data: List<TrackDto>?,
    @SerializedName("total") val total: Int?,
    // A Deezer devolve HTTP 200 com este objeto quando algo dá errado
    @SerializedName("error") val error: DeezerErrorDto?
)

data class TrackDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("duration") val duration: Int?,
    @SerializedName("position") val position: Int?,
    @SerializedName("preview") val preview: String?,
    @SerializedName("artist") val artist: ArtistDto?,
    @SerializedName("album") val album: AlbumDto?
)

data class ArtistDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?
)

data class AlbumDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("cover_medium") val coverMedium: String?,
    @SerializedName("cover_big") val coverBig: String?
)

data class DeezerErrorDto(
    @SerializedName("type") val type: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("code") val code: Int?
)
