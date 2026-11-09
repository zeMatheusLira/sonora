package com.example.sonora.feature.genres.data

import com.example.sonora.feature.genres.data.local.GenreDao
import com.example.sonora.feature.genres.data.local.GenreEntity
import com.example.sonora.feature.genres.data.model.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Lê os gêneros musicais do Room. Os Flows emitem de novo sempre que a tabela mudar. */
class GenresRepository(private val dao: GenreDao) {

    /** Todos os gêneros ou, se [query] não estiver em branco, só os que combinam com a busca. */
    fun getGenres(query: String): Flow<List<Genre>> {
        val term = query.trim()
        val source = if (term.isEmpty()) dao.getAllGenres() else dao.searchGenres(term)
        return source.map { entities -> entities.map { it.toGenre() } }
    }

    private fun GenreEntity.toGenre() = Genre(
        id = id,
        name = name,
        originCountry = originCountry,
        originYear = originYear,
        roots = roots.splitList(),
        description = description,
        bpmRange = bpmMin..bpmMax,
        keyArtists = keyArtists.splitList(),
        curiosity = curiosity
    )

    private fun String.splitList() = split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
