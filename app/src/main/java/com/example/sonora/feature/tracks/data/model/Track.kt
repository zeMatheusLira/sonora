package com.example.sonora.feature.tracks.data.model

/** Modelo usado pela UI, já sem os detalhes do JSON da Deezer. */
data class Track(
    val id: Long,
    val position: Int,
    val title: String,
    val artistName: String,
    val albumTitle: String,
    val coverUrl: String?,
    val durationSeconds: Int,
    val previewUrl: String?
) {
    /** Duração no formato mm:ss (ex.: 175 -> "02:55"). */
    val formattedDuration: String
        get() = "%02d:%02d".format(durationSeconds / 60, durationSeconds % 60)
}
