package com.example.sonora.feature.festivals.data

import com.example.sonora.feature.festivals.data.local.FestivalDao
import com.example.sonora.feature.festivals.data.local.FestivalEntity
import com.example.sonora.feature.festivals.data.model.Festival
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Lê os festivais do Room. Os Flows emitem de novo sempre que a tabela mudar. */
class FestivalsRepository(private val dao: FestivalDao) {

    /** Regiões disponíveis (Brasil, Europa, ...), usadas nos filtros da tela. */
    fun getRegions(): Flow<List<String>> = dao.getRegions()

    /** Todos os festivais ou, se [region] não for nulo, só os daquela região. */
    fun getFestivals(region: String?): Flow<List<Festival>> {
        val source = if (region == null) dao.getAllFestivals() else dao.getFestivalsByRegion(region)
        return source.map { entities -> entities.map { it.toFestival() } }
    }

    private fun FestivalEntity.toFestival() = Festival(
        id = id,
        name = name,
        city = city,
        country = country,
        region = region,
        firstEdition = firstEdition,
        genres = genres.splitList(),
        description = description,
        iconicArtists = iconicArtists.splitList(),
        curiosity = curiosity
    )

    private fun String.splitList() = split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
