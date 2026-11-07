package com.example.sonora.feature.instruments.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** DAO somente de consulta: o app não insere, edita nem apaga instrumentos. */
@Dao
interface InstrumentDao {

    @Query("SELECT * FROM instruments ORDER BY name ASC")
    fun getAllInstruments(): Flow<List<InstrumentEntity>>

    @Query("SELECT * FROM instruments WHERE family = :family ORDER BY name ASC")
    fun getInstrumentsByFamily(family: String): Flow<List<InstrumentEntity>>

    @Query("SELECT DISTINCT family FROM instruments ORDER BY family ASC")
    fun getFamilies(): Flow<List<String>>
}
