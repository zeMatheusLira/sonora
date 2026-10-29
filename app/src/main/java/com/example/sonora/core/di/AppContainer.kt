package com.example.sonora.core.di

import android.content.Context
import com.example.sonora.core.database.AppDatabase
import com.example.sonora.core.network.NetworkModule
import com.example.sonora.feature.decades.data.DecadesRepository
import com.example.sonora.feature.tracks.data.TracksRepository
import com.example.sonora.feature.tracks.data.remote.DeezerApi
import retrofit2.Retrofit

/**
 * Injeção de dependências manual (sem Hilt/Dagger).
 * Guarda as instâncias únicas de Retrofit e do banco e cria os repositórios das features.
 * Cada integrante adiciona aqui o repositório da sua feature.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val retrofit: Retrofit by lazy { NetworkModule.provideRetrofit() }

    val database: AppDatabase by lazy { AppDatabase.build(appContext) }

    // Integrante 1 — Top Músicas (Retrofit)
    val tracksRepository: TracksRepository by lazy {
        TracksRepository(retrofit.create(DeezerApi::class.java))
    }

    // Integrante 1 — Décadas da Música (Room)
    val decadesRepository: DecadesRepository by lazy {
        DecadesRepository(database.decadeDao())
    }
}
