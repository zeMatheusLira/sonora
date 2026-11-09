package com.example.sonora.feature.genres.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabela "music_genres" da feature Gêneros Musicais (integrante 4).
 * Raízes e artistas ficam como texto separado por vírgula para não precisar de TypeConverter.
 */
@Entity(tableName = GenreEntity.TABLE_NAME)
data class GenreEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "origin_country") val originCountry: String,
    @ColumnInfo(name = "origin_year") val originYear: Int,
    @ColumnInfo(name = "roots") val roots: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "bpm_min") val bpmMin: Int,
    @ColumnInfo(name = "bpm_max") val bpmMax: Int,
    @ColumnInfo(name = "key_artists") val keyArtists: String,
    @ColumnInfo(name = "curiosity") val curiosity: String
) {
    companion object {
        const val TABLE_NAME = "music_genres"
    }
}
