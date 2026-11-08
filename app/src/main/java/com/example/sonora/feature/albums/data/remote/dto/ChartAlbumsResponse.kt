package com.example.sonora.feature.albums.data.remote.dto

import com.google.gson.annotations.SerializedName

/*
 * DTOs criados a partir da resposta real de GET https://api.deezer.com/chart/0/albums.
 * Mapeamos só os campos usados pelo app; o Gson ignora o resto.
 */

data class ChartAlbumsResponse(
    @SerializedName("data") val data: List<ChartAlbumDto>?,
    @SerializedName("total") val total: Int?,
    // A Deezer devolve HTTP 200 com este objeto quando algo dá errado
    @SerializedName("error") val error: AlbumsErrorDto?
)

data class ChartAlbumDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("position") val position: Int?,
    @SerializedName("record_type") val recordType: String?,
    @SerializedName("explicit_lyrics") val explicitLyrics: Boolean?,
    @SerializedName("cover_medium") val coverMedium: String?,
    @SerializedName("cover_big") val coverBig: String?,
    @SerializedName("artist") val artist: AlbumArtistDto?
)

data class AlbumArtistDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?
)

data class AlbumsErrorDto(
    @SerializedName("type") val type: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("code") val code: Int?
)
