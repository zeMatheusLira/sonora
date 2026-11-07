package com.example.sonora.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sonora.feature.decades.data.local.DecadeDao
import com.example.sonora.feature.decades.data.local.DecadeEntity
import com.example.sonora.feature.decades.data.local.DecadesSeed
import com.example.sonora.feature.instruments.data.local.InstrumentDao
import com.example.sonora.feature.instruments.data.local.InstrumentEntity
import com.example.sonora.feature.instruments.data.local.InstrumentsSeed

/**
 * Banco único do app. Para adicionar a sua tabela:
 * 1. inclua a sua @Entity em `entities`;
 * 2. crie uma `abstract fun` para o seu DAO;
 * 3. aumente `version` (e adicione uma Migration, se necessário);
 * 4. se tiver dados iniciais, chame o seu seed em SeedCallback.onCreate.
 */
@Database(
    entities = [
        DecadeEntity::class, // Décadas da Música
        InstrumentEntity::class, // Instrumentos Musicais
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun decadeDao(): DecadeDao

    abstract fun instrumentDao(): InstrumentDao

    /**
     * Popula as tabelas com os dados iniciais.
     * - onCreate: primeira abertura do banco (app recém-instalado).
     * - onDestructiveMigration: quando a `version` sobe, o fallbackToDestructiveMigration
     *   apaga e recria as tabelas SEM chamar onCreate; sem isto as telas ficariam vazias.
     */
    private class SeedCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            populateAll(db)
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            populateAll(db)
        }

        private fun populateAll(db: SupportSQLiteDatabase) {
            DecadesSeed.populate(db)
            InstrumentsSeed.populate(db)
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
