package com.example.sonora.feature.decades.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** DAO somente de consulta: o app não insere, edita nem apaga décadas. */
@Dao
interface DecadeDao {

    @Query("SELECT * FROM decades ORDER BY start_year ASC")
    fun getAllDecades(): Flow<List<DecadeEntity>>

    @Query("SELECT * FROM decades WHERE id = :id")
    suspend fun getDecadeById(id: Int): DecadeEntity?
}
