package com.example.sonora.feature.festivals.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Seed inicial da tabela "festivals", executado quando o banco é criado ou recriado
 * (AppDatabase.SeedCallback). Não existe tela para o usuário inserir ou editar dados.
 */
object FestivalsSeed {

    private const val BRASIL = "Brasil"
    private const val EUROPA = "Europa"
    private const val AMERICA_DO_NORTE = "América do Norte"
    private const val ASIA = "Ásia"

    fun populate(db: SupportSQLiteDatabase) {
        festivals.forEach { festival ->
            val values = ContentValues().apply {
                put("id", festival.id)
                put("name", festival.name)
                put("city", festival.city)
                put("country", festival.country)
                put("region", festival.region)
                put("first_edition", festival.firstEdition)
                put("genres", festival.genres)
                put("description", festival.description)
                put("iconic_artists", festival.iconicArtists)
                put("curiosity", festival.curiosity)
            }
            db.insert(FestivalEntity.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values)
        }
    }

    val festivals = listOf(
        FestivalEntity(
            id = 1,
            name = "Festival de Parintins",
            city = "Parintins",
            country = "Brasil",
            region = BRASIL,
            firstEdition = 1965,
            genres = "Toada, Boi-bumbá",
            description = "Festa folclórica na Ilha de Tupinambarana, no Amazonas, onde os bois " +
                "Garantido (vermelho) e Caprichoso (azul) disputam no Bumbódromo com alegorias gigantes.",
            iconicArtists = "Boi Garantido, Boi Caprichoso, David Assayag",
            curiosity = "A torcida de um boi fica em silêncio enquanto o rival se apresenta, " +
                "porque a postura da galera também conta pontos."
        ),
        FestivalEntity(
            id = 2,
            name = "Montreux Jazz Festival",
            city = "Montreux",
            country = "Suíça",
            region = EUROPA,
            firstEdition = 1967,
            genres = "Jazz, Blues, Soul, Rock",
            description = "Criado por Claude Nobs às margens do Lago Léman. Começou focado no jazz " +
                "e se abriu para todos os estilos, mantendo um enorme arquivo de shows gravados.",
            iconicArtists = "Miles Davis, Nina Simone, Elis Regina, Gilberto Gil",
            curiosity = "\"Smoke on the Water\", do Deep Purple, conta o incêndio do cassino de " +
                "Montreux durante um show de Frank Zappa em 1971."
        ),
        FestivalEntity(
            id = 3,
            name = "Woodstock",
            city = "Bethel, Nova York",
            country = "Estados Unidos",
            region = AMERICA_DO_NORTE,
            firstEdition = 1969,
            genres = "Rock, Folk, Soul",
            description = "Três dias de \"paz e música\" numa fazenda de Bethel, em agosto de 1969. " +
                "Virou o símbolo da contracultura e da geração hippie.",
            iconicArtists = "Jimi Hendrix, Janis Joplin, Santana, The Who",
            curiosity = "Cerca de 400 mil pessoas apareceram, muito mais que o esperado, e o " +
                "festival acabou virando evento gratuito."
        ),
        FestivalEntity(
            id = 4,
            name = "Glastonbury",
            city = "Pilton",
            country = "Inglaterra",
            region = EUROPA,
            firstEdition = 1970,
            genres = "Rock, Pop, Indie, Eletrônica",
            description = "Realizado na fazenda de Michael Eavis, é um dos maiores festivais a céu " +
                "aberto do mundo, famoso pelo palco Pyramid e pela lama.",
            iconicArtists = "David Bowie, Radiohead, Beyoncé, Adele",
            curiosity = "O festival costuma tirar um \"ano de descanso\" de tempos em tempos para " +
                "a terra da fazenda se recuperar."
        ),
        FestivalEntity(
            id = 5,
            name = "Rock in Rio",
            city = "Rio de Janeiro",
            country = "Brasil",
            region = BRASIL,
            firstEdition = 1985,
            genres = "Rock, Pop, Metal",
            description = "Criado por Roberto Medina, colocou o Brasil na rota dos grandes shows " +
                "internacionais e depois ganhou edições em Lisboa, Madri e Las Vegas.",
            iconicArtists = "Queen, Iron Maiden, Guns N' Roses, Cássia Eller",
            curiosity = "Em 1985 o público cantou \"Love of My Life\" inteira com o Queen, " +
                "um dos momentos mais lembrados da história do festival."
        ),
        FestivalEntity(
            id = 6,
            name = "Fuji Rock Festival",
            city = "Yuzawa, Niigata",
            country = "Japão",
            region = ASIA,
            firstEdition = 1997,
            genres = "Rock, Indie, Eletrônica",
            description = "Acontece numa estação de esqui nas montanhas, com palcos espalhados pela " +
                "floresta. É conhecido pela organização e pelo público que recolhe o próprio lixo.",
            iconicArtists = "Red Hot Chili Peppers, Radiohead, Björk",
            curiosity = "A primeira edição, perto do Monte Fuji, foi atingida por um tufão; " +
                "por isso o festival mudou de lugar."
        ),
        FestivalEntity(
            id = 7,
            name = "Coachella",
            city = "Indio, Califórnia",
            country = "Estados Unidos",
            region = AMERICA_DO_NORTE,
            firstEdition = 1999,
            genres = "Pop, Indie, Hip-hop, Eletrônica",
            description = "Realizado no deserto do Vale de Coachella em dois fins de semana " +
                "seguidos, com o mesmo line-up, e famoso pelas instalações de arte.",
            iconicArtists = "Beyoncé, Daft Punk, Prince, Anitta",
            curiosity = "O show de Beyoncé em 2018 ficou tão famoso que o festival ganhou o " +
                "apelido de \"Beychella\"."
        ),
        FestivalEntity(
            id = 8,
            name = "Tomorrowland",
            city = "Boom",
            country = "Bélgica",
            region = EUROPA,
            firstEdition = 2005,
            genres = "Eletrônica, House, Techno",
            description = "Um dos maiores festivais de música eletrônica do mundo, com palcos " +
                "cenográficos que mudam de tema a cada edição.",
            iconicArtists = "Armin van Buuren, David Guetta, Alok, Dimitri Vegas & Like Mike",
            curiosity = "Teve uma edição brasileira em Itu (SP), a partir de 2015."
        ),
        FestivalEntity(
            id = 9,
            name = "Lollapalooza Brasil",
            city = "São Paulo",
            country = "Brasil",
            region = BRASIL,
            firstEdition = 2012,
            genres = "Rock alternativo, Indie, Pop, Hip-hop",
            description = "Versão brasileira do festival criado por Perry Farrell nos EUA em 1991. " +
                "Acontece no Autódromo de Interlagos.",
            iconicArtists = "Foo Fighters, Arctic Monkeys, Pearl Jam, Pabllo Vittar",
            curiosity = "O Lollapalooza nasceu como turnê de despedida da banda Jane's Addiction."
        ),
        FestivalEntity(
            id = 10,
            name = "The Town",
            city = "São Paulo",
            country = "Brasil",
            region = BRASIL,
            firstEdition = 2023,
            genres = "Pop, Rock, Rap, MPB",
            description = "Festival paulistano criado pelos mesmos organizadores do Rock in Rio, " +
                "também no Autódromo de Interlagos.",
            iconicArtists = "Bruno Mars, Post Malone, Iza, Ludmilla",
            curiosity = "Bruno Mars foi atração principal de duas noites na primeira edição."
        )
    )
}
