package com.example.sonora.feature.decades.data

import com.example.sonora.feature.decades.data.local.DecadeDao
import com.example.sonora.feature.decades.data.local.DecadeEntity
import com.example.sonora.feature.decades.data.model.Decade
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Lê as décadas do Room. O Flow emite de novo sempre que a tabela mudar. */
class DecadesRepository(private val dao: DecadeDao) {

    fun getAllDecades(): Flow<List<Decade>> =
        dao.getAllDecades().map { entities -> entities.map { it.toDecade() } }

    private fun DecadeEntity.toDecade() = Decade(
        id = id,
        label = label,
        startYear = startYear,
        description = description,
        topGenres = topGenres.splitList(),
        iconicArtists = iconicArtists.splitList(),
        curiosity = curiosity
    )

    private fun String.splitList(): List<String> =
        split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
