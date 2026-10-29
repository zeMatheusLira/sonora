package com.example.sonora.core.network

import com.example.sonora.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cria o Retrofit compartilhado por todas as features.
 * Cada integrante cria a própria interface (ex.: DeezerApi) com retrofit.create(...).
 */
object NetworkModule {

    private const val DEEZER_BASE_URL = "https://api.deezer.com/"

    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            // Loga o corpo das respostas só em debug
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(DEEZER_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
