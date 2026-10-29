package com.example.sonora.feature.decades.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabela "decades" do integrante 1.
 * Gêneros e artistas ficam como texto separado por vírgula para não precisar de TypeConverter.
 */
@Entity(tableName = DecadeEntity.TABLE_NAME)
data class DecadeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "start_year") val startYear: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "top_genres") val topGenres: String,
    @ColumnInfo(name = "iconic_artists") val iconicArtists: String,
    @ColumnInfo(name = "curiosity") val curiosity: String
) {
    companion object {
        const val TABLE_NAME = "decades"
    }
}
