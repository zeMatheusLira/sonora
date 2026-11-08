package com.example.sonora.feature.festivals.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabela "festivals" da feature Festivais de Música (integrante 3).
 * Gêneros e artistas marcantes ficam como texto separado por vírgula para não precisar de TypeConverter.
 */
@Entity(tableName = FestivalEntity.TABLE_NAME)
data class FestivalEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "region") val region: String,
    @ColumnInfo(name = "first_edition") val firstEdition: Int,
    @ColumnInfo(name = "genres") val genres: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "iconic_artists") val iconicArtists: String,
    @ColumnInfo(name = "curiosity") val curiosity: String
) {
    companion object {
        const val TABLE_NAME = "festivals"
    }
}
