package com.example.sonora.feature.instruments.data

import com.example.sonora.feature.instruments.data.local.InstrumentDao
import com.example.sonora.feature.instruments.data.local.InstrumentEntity
import com.example.sonora.feature.instruments.data.model.Instrument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Lê os instrumentos do Room. Os Flows emitem de novo sempre que a tabela mudar. */
class InstrumentsRepository(private val dao: InstrumentDao) {

    /** Famílias disponíveis (Cordas, Sopro, ...), usadas nos filtros da tela. */
    fun getFamilies(): Flow<List<String>> = dao.getFamilies()

    /** Todos os instrumentos ou, se [family] não for nulo, só os daquela família. */
    fun getInstruments(family: String?): Flow<List<Instrument>> {
        val source = if (family == null) dao.getAllInstruments() else dao.getInstrumentsByFamily(family)
        return source.map { entities -> entities.map { it.toInstrument() } }
    }

    private fun InstrumentEntity.toInstrument() = Instrument(
        id = id,
        name = name,
        family = family,
        origin = origin,
        description = description,
        famousPlayers = famousPlayers.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        curiosity = curiosity
    )
}
