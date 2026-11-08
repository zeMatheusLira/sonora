package com.example.sonora.feature.festivals.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** DAO somente de consulta: o app não insere, edita nem apaga festivais. */
@Dao
interface FestivalDao {

    // Ordenados pelo ano da primeira edição, formando uma linha do tempo
    @Query("SELECT * FROM festivals ORDER BY first_edition ASC")
    fun getAllFestivals(): Flow<List<FestivalEntity>>

    @Query("SELECT * FROM festivals WHERE region = :region ORDER BY first_edition ASC")
    fun getFestivalsByRegion(region: String): Flow<List<FestivalEntity>>

    @Query("SELECT DISTINCT region FROM festivals ORDER BY region ASC")
    fun getRegions(): Flow<List<String>>
}
