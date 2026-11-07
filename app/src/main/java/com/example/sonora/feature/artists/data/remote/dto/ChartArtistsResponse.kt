package com.example.sonora.feature.artists.data.remote.dto

import com.google.gson.annotations.SerializedName

/*
 * DTOs criados a partir da resposta real de GET https://api.deezer.com/chart/0/artists.
 * Mapeamos só os campos usados pelo app; o Gson ignora o resto.
 */

data class ChartArtistsResponse(
    @SerializedName("data") val data: List<ChartArtistDto>?,
    @SerializedName("total") val total: Int?,
    // A Deezer devolve HTTP 200 com este objeto quando algo dá errado
    @SerializedName("error") val error: ArtistsErrorDto?
)

data class ChartArtistDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("position") val position: Int?,
    @SerializedName("link") val link: String?,
    @SerializedName("picture_medium") val pictureMedium: String?,
    @SerializedName("picture_big") val pictureBig: String?
)

data class ArtistsErrorDto(
    @SerializedName("type") val type: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("code") val code: Int?
)
