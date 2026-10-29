package com.example.sonora.feature.decades.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Seed inicial da tabela "decades", executado uma única vez quando o banco é criado
 * (AppDatabase.SeedCallback.onCreate). Não existe tela para o usuário inserir ou editar dados.
 */
object DecadesSeed {

    fun populate(db: SupportSQLiteDatabase) {
        decades.forEach { decade ->
            val values = ContentValues().apply {
                put("id", decade.id)
                put("label", decade.label)
                put("start_year", decade.startYear)
                put("description", decade.description)
                put("top_genres", decade.topGenres)
                put("iconic_artists", decade.iconicArtists)
                put("curiosity", decade.curiosity)
            }
            db.insert(DecadeEntity.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values)
        }
    }

    val decades = listOf(
        DecadeEntity(
            id = 1,
            label = "Anos 60",
            startYear = 1960,
            description = "A década da contracultura. O rock britânico conquistou o mundo, " +
                "a soul music deu voz ao movimento pelos direitos civis e, no Brasil, " +
                "a Bossa Nova ganhou o exterior enquanto a Jovem Guarda e a Tropicália " +
                "agitavam a TV e os festivais.",
            topGenres = "Rock, Soul, Bossa Nova, Jovem Guarda, Tropicália",
            iconicArtists = "The Beatles, Aretha Franklin, Jimi Hendrix, Tom Jobim, Roberto Carlos, Caetano Veloso",
            curiosity = "O festival de Woodstock, em 1969, reuniu cerca de 400 mil pessoas numa fazenda em Nova York."
        ),
        DecadeEntity(
            id = 2,
            label = "Anos 70",
            startYear = 1970,
            description = "Das pistas de dança da disco à rebeldia do punk, os anos 70 " +
                "foram de extremos. O rock ficou grandioso nos estádios e a MPB viveu " +
                "uma fase de ouro, muitas vezes driblando a censura da ditadura.",
            topGenres = "Disco, Rock progressivo, Punk, Funk, MPB",
            iconicArtists = "Queen, Led Zeppelin, ABBA, Bee Gees, Elis Regina, Raul Seixas",
            curiosity = "\"Bohemian Rhapsody\" (1975) tem quase 6 minutos e a gravadora achava " +
                "longa demais para tocar no rádio. Virou um dos maiores sucessos do Queen."
        ),
        DecadeEntity(
            id = 3,
            label = "Anos 80",
            startYear = 1980,
            description = "A era do videoclipe. Sintetizadores dominaram o pop, a MTV " +
                "transformou artistas em ícones visuais e o hip-hop começou a crescer. " +
                "No Brasil, foi a explosão do rock nacional.",
            topGenres = "Pop, New Wave, Rock nacional, Hip-hop, Heavy metal",
            iconicArtists = "Michael Jackson, Madonna, U2, Legião Urbana, Cazuza, Titãs",
            curiosity = "\"Thriller\" (1982), de Michael Jackson, é considerado o álbum mais vendido da história."
        ),
        DecadeEntity(
            id = 4,
            label = "Anos 90",
            startYear = 1990,
            description = "O grunge de Seattle derrubou o rock farofa, o rap ganhou as " +
                "paradas e as boy e girl bands lotaram estádios. No Brasil, axé, pagode " +
                "e o rap dos Racionais marcaram a década.",
            topGenres = "Grunge, Hip-hop, Axé, Pagode, Eurodance",
            iconicArtists = "Nirvana, Mariah Carey, Spice Girls, Tupac, Racionais MC's, Mamonas Assassinas",
            curiosity = "Em 1999 o Napster popularizou o compartilhamento de MP3 e mudou para sempre a indústria musical."
        ),
        DecadeEntity(
            id = 5,
            label = "Anos 2000",
            startYear = 2000,
            description = "A música ficou digital. O pop e o R&B dominaram as rádios, o " +
                "emo e o pop punk embalaram adolescentes e a internet passou a lançar " +
                "artistas. No Brasil, o funk carioca e o sertanejo universitário cresceram.",
            topGenres = "Pop, R&B, Hip-hop, Emo, Funk carioca",
            iconicArtists = "Beyoncé, Eminem, Britney Spears, Coldplay, Ivete Sangalo, Charlie Brown Jr.",
            curiosity = "O iPod (2001) e a iTunes Store (2003) popularizaram a compra de músicas avulsas pela internet."
        ),
        DecadeEntity(
            id = 6,
            label = "Anos 2010",
            startYear = 2010,
            description = "O streaming virou o principal jeito de ouvir música. A EDM " +
                "tomou os festivais, o trap e o K-pop ganharam o mundo e o sertanejo e o " +
                "funk lideraram as paradas brasileiras.",
            topGenres = "EDM, Trap, K-pop, Sertanejo, Funk",
            iconicArtists = "Taylor Swift, Adele, Drake, BTS, Anitta, Marília Mendonça",
            curiosity = "\"Gangnam Style\" (2012), do PSY, foi o primeiro vídeo do YouTube a passar de 1 bilhão de visualizações."
        ),
        DecadeEntity(
            id = 7,
            label = "Anos 2020",
            startYear = 2020,
            description = "Vídeos curtos nas redes sociais passaram a decidir os hits. " +
                "Ritmos latinos e africanos ganharam espaço global e, no Brasil, o " +
                "piseiro e o agronejo dividiram as paradas com o funk e o trap.",
            topGenres = "Pop, Reggaeton, Afrobeats, Piseiro, Trap",
            iconicArtists = "Bad Bunny, The Weeknd, Dua Lipa, Olivia Rodrigo, Anitta, Ana Castela",
            curiosity = "Em 2022, \"Envolver\" fez de Anitta a primeira artista brasileira a liderar o ranking global do Spotify."
        )
    )
}
