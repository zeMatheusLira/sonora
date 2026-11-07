package com.example.sonora.feature.instruments.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabela "instruments" da feature Instrumentos Musicais.
 * Os músicos famosos ficam como texto separado por vírgula para não precisar de TypeConverter.
 */
@Entity(tableName = InstrumentEntity.TABLE_NAME)
data class InstrumentEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "family") val family: String,
    @ColumnInfo(name = "origin") val origin: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "famous_players") val famousPlayers: String,
    @ColumnInfo(name = "curiosity") val curiosity: String
) {
    companion object {
        const val TABLE_NAME = "instruments"
    }
}
