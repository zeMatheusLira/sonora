package com.example.sonora.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sonora.feature.decades.data.local.DecadeDao
import com.example.sonora.feature.decades.data.local.DecadeEntity
import com.example.sonora.feature.decades.data.local.DecadesSeed
import com.example.sonora.feature.festivals.data.local.FestivalDao
import com.example.sonora.feature.festivals.data.local.FestivalEntity
import com.example.sonora.feature.festivals.data.local.FestivalsSeed
import com.example.sonora.feature.genres.data.local.GenreDao
import com.example.sonora.feature.genres.data.local.GenreEntity
import com.example.sonora.feature.genres.data.local.GenresSeed
import com.example.sonora.feature.instruments.data.local.InstrumentDao
import com.example.sonora.feature.instruments.data.local.InstrumentEntity
import com.example.sonora.feature.instruments.data.local.InstrumentsSeed

/**
 * Banco único do app. Para adicionar a sua tabela:
 * 1. inclua a sua @Entity em `entities`;
 * 2. crie uma `abstract fun` para o seu DAO;
 * 3. aumente `version` (e adicione uma Migration, se necessário);
 * 4. se tiver dados iniciais, chame o seu seed com seedIfEmpty em SeedCallback.onOpen.
 */
@Database(
    entities = [
        DecadeEntity::class, // Décadas da Música
        InstrumentEntity::class, // Instrumentos Musicais
        FestivalEntity::class, // Festivais de Música (integrante 3)
        GenreEntity::class, // Gêneros Musicais (integrante 4)
    ],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun decadeDao(): DecadeDao

    abstract fun instrumentDao(): InstrumentDao

    abstract fun festivalDao(): FestivalDao

    abstract fun genreDao(): GenreDao

    /**
     * Popula as tabelas com os dados iniciais.
     *
     * Usa onOpen (e não onCreate/onDestructiveMigration) porque, na migração destrutiva,
     * o Room chama onDestructiveMigration DEPOIS de apagar as tabelas e ANTES de recriá-las:
     * o seed falhava com "no such table" e o banco ficava preso na versão antiga.
     * Em onOpen as tabelas já existem; cada seed só roda se a sua tabela estiver vazia.
     */
    private class SeedCallback : Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            seedIfEmpty(db, DecadeEntity.TABLE_NAME) { DecadesSeed.populate(it) }
            seedIfEmpty(db, InstrumentEntity.TABLE_NAME) { InstrumentsSeed.populate(it) }
            seedIfEmpty(db, FestivalEntity.TABLE_NAME) { FestivalsSeed.populate(it) }
            seedIfEmpty(db, GenreEntity.TABLE_NAME) { GenresSeed.populate(it) }
        }

        private fun seedIfEmpty(
            db: SupportSQLiteDatabase,
            table: String,
            populate: (SupportSQLiteDatabase) -> Unit
        ) {
            val isEmpty = db.query("SELECT COUNT(*) FROM $table").use { cursor ->
                cursor.moveToFirst() && cursor.getInt(0) == 0
            }
            if (isEmpty) populate(db)
        }
    }

    companion object {
        private const val DATABASE_NAME = "sonora.db"

        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(SeedCallback())
                // Enquanto o app está em desenvolvimento, recria o banco quando a versão muda
                .fallbackToDestructiveMigration()
                .build()
    }
}
