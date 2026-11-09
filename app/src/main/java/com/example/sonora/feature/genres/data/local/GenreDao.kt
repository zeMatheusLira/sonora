package com.example.sonora.feature.genres.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** DAO somente de consulta: o app não insere, edita nem apaga gêneros. */
@Dao
interface GenreDao {

    // Ordenados pelo ano de surgimento, do mais antigo ao mais recente
    @Query("SELECT * FROM music_genres ORDER BY origin_year ASC")
    fun getAllGenres(): Flow<List<GenreEntity>>

    /** Busca pelo nome do gênero, pelas raízes ou pelos artistas (sem diferenciar maiúsculas). */
    @Query(
        """
        SELECT * FROM music_genres
        WHERE name LIKE '%' || :query || '%'
           OR roots LIKE '%' || :query || '%'
           OR key_artists LIKE '%' || :query || '%'
        ORDER BY origin_year ASC
        """
    )
    fun searchGenres(query: String): Flow<List<GenreEntity>>
}
