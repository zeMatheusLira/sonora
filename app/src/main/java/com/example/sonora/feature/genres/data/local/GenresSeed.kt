package com.example.sonora.feature.genres.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Seed inicial da tabela "music_genres", executado quando a tabela está vazia
 * (AppDatabase.SeedCallback). Não existe tela para o usuário inserir ou editar dados.
 */
object GenresSeed {

    fun populate(db: SupportSQLiteDatabase) {
        genres.forEach { genre ->
            val values = ContentValues().apply {
                put("id", genre.id)
                put("name", genre.name)
                put("origin_country", genre.originCountry)
                put("origin_year", genre.originYear)
                put("roots", genre.roots)
                put("description", genre.description)
                put("bpm_min", genre.bpmMin)
                put("bpm_max", genre.bpmMax)
                put("key_artists", genre.keyArtists)
                put("curiosity", genre.curiosity)
            }
            db.insert(GenreEntity.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values)
        }
    }

    val genres = listOf(
        GenreEntity(
            id = 1,
            name = "Blues",
            originCountry = "Estados Unidos",
            originYear = 1900,
            roots = "Spirituals, Work songs, Música africana",
            description = "Nasceu entre as comunidades afro-americanas do sul dos EUA. A estrutura " +
                "de 12 compassos e as \"blue notes\" viraram a base de boa parte da música popular moderna.",
            bpmMin = 60,
            bpmMax = 120,
            keyArtists = "Robert Johnson, B.B. King, Muddy Waters, Etta James",
            curiosity = "Robert Johnson gravou apenas 29 músicas e mesmo assim influenciou " +
                "Eric Clapton, os Rolling Stones e o Led Zeppelin."
        ),
        GenreEntity(
            id = 2,
            name = "Samba",
            originCountry = "Brasil",
            originYear = 1916,
            roots = "Lundu, Maxixe, Batuque",
            description = "Ganhou forma nas casas das tias baianas no Rio de Janeiro. É o ritmo das " +
                "escolas de samba e deu origem ao pagode, ao samba-enredo e à bossa nova.",
            bpmMin = 90,
            bpmMax = 110,
            keyArtists = "Cartola, Clara Nunes, Zeca Pagodinho, Beth Carvalho",
            curiosity = "\"Pelo Telefone\", registrado em 1916 por Donga, é considerado o " +
                "primeiro samba gravado."
        ),
        GenreEntity(
            id = 3,
            name = "Jazz",
            originCountry = "Estados Unidos",
            originYear = 1917,
            roots = "Blues, Ragtime, Bandas de metais",
            description = "Surgiu em Nova Orleans e tem a improvisação como marca. Passou pelo swing, " +
                "bebop, cool e fusion, sempre reinventando a harmonia.",
            bpmMin = 80,
            bpmMax = 280,
            keyArtists = "Louis Armstrong, Miles Davis, Ella Fitzgerald, John Coltrane",
            curiosity = "\"Kind of Blue\", de Miles Davis, foi gravado em só duas sessões e quase " +
                "sem ensaio: os músicos receberam apenas esboços das músicas."
        ),
        GenreEntity(
            id = 4,
            name = "Forró",
            originCountry = "Brasil",
            originYear = 1946,
            roots = "Baião, Xote, Xaxado",
            description = "Trio de sanfona, zabumba e triângulo que levou a música do sertão " +
                "nordestino para todo o Brasil, hoje dividido em pé de serra, universitário e eletrônico.",
            bpmMin = 100,
            bpmMax = 160,
            keyArtists = "Luiz Gonzaga, Dominguinhos, Jackson do Pandeiro, Elba Ramalho",
            curiosity = "Luiz Gonzaga, o Rei do Baião, gravou \"Asa Branca\" em 1947, e ela " +
                "virou um hino do Nordeste."
        ),
        GenreEntity(
            id = 5,
            name = "Rock",
            originCountry = "Estados Unidos",
            originYear = 1954,
            roots = "Rhythm and blues, Country, Blues",
            description = "Guitarra elétrica, baixo e bateria em primeiro plano. Do rock and roll dos " +
                "anos 50 saíram o hard rock, o punk, o metal, o grunge e o indie.",
            bpmMin = 100,
            bpmMax = 160,
            keyArtists = "Elvis Presley, Chuck Berry, The Beatles, Rita Lee",
            curiosity = "Chuck Berry tem uma música no Voyager Golden Record, o disco que viaja " +
                "pelo espaço a bordo das sondas Voyager."
        ),
        GenreEntity(
            id = 6,
            name = "Bossa Nova",
            originCountry = "Brasil",
            originYear = 1958,
            roots = "Samba, Jazz",
            description = "Samba mais intimista, com a batida de violão de João Gilberto e harmonias " +
                "sofisticadas. Fez a música brasileira ser ouvida no mundo todo.",
            bpmMin = 110,
            bpmMax = 140,
            keyArtists = "João Gilberto, Tom Jobim, Vinicius de Moraes, Nara Leão",
            curiosity = "\"Garota de Ipanema\" está entre as músicas mais gravadas da história, " +
                "com versões de Frank Sinatra a Amy Winehouse."
        ),
        GenreEntity(
            id = 7,
            name = "Reggae",
            originCountry = "Jamaica",
            originYear = 1968,
            roots = "Ska, Rocksteady, Mento",
            description = "Ritmo marcado pelo contratempo da guitarra (\"skank\") e pelo baixo em " +
                "destaque, muitas vezes com letras de protesto e espiritualidade.",
            bpmMin = 60,
            bpmMax = 90,
            keyArtists = "Bob Marley, Peter Tosh, Jimmy Cliff, Edson Gomes",
            curiosity = "Em 2018 a UNESCO declarou o reggae jamaicano Patrimônio Cultural " +
                "Imaterial da Humanidade."
        ),
        GenreEntity(
            id = 8,
            name = "Hip-Hop",
            originCountry = "Estados Unidos",
            originYear = 1973,
            roots = "Funk, Soul, Disco",
            description = "Nasceu nas festas de quarteirão do Bronx, com DJs repetindo os breaks dos " +
                "discos para MCs rimarem por cima. Hoje é um dos gêneros mais ouvidos do planeta.",
            bpmMin = 80,
            bpmMax = 110,
            keyArtists = "Grandmaster Flash, Tupac, Racionais MC's, Kendrick Lamar",
            curiosity = "Uma festa de DJ Kool Herc em agosto de 1973, no Bronx, costuma ser " +
                "citada como o nascimento do hip-hop."
        ),
        GenreEntity(
            id = 9,
            name = "Sertanejo",
            originCountry = "Brasil",
            originYear = 1929,
            roots = "Música caipira, Moda de viola, Guarânia",
            description = "Começou com as duplas caipiras e a viola, passou pelo romântico dos anos 80 " +
                "e chegou ao universitário, hoje o gênero mais tocado no Brasil.",
            bpmMin = 70,
            bpmMax = 140,
            keyArtists = "Tonico e Tinoco, Chitãozinho e Xororó, Marília Mendonça, Jorge e Mateus",
            curiosity = "As primeiras gravações de música caipira foram bancadas pelo próprio " +
                "Cornélio Pires em 1929, porque as gravadoras não acreditavam no sucesso."
        ),
        GenreEntity(
            id = 10,
            name = "House",
            originCountry = "Estados Unidos",
            originYear = 1984,
            roots = "Disco, Soul, Synth-pop",
            description = "Batida \"four on the floor\" feita em baterias eletrônicas, criada nos clubes " +
                "de Chicago. É a base de muitos estilos de música eletrônica.",
            bpmMin = 118,
            bpmMax = 130,
            keyArtists = "Frankie Knuckles, Daft Punk, Marshall Jefferson, Alok",
            curiosity = "O nome vem do Warehouse, o clube de Chicago onde Frankie Knuckles era DJ " +
                "residente."
        ),
        GenreEntity(
            id = 11,
            name = "Funk Carioca",
            originCountry = "Brasil",
            originYear = 1989,
            roots = "Miami bass, Freestyle, Hip-Hop",
            description = "Surgiu nos bailes das comunidades do Rio a partir do Miami bass. Ganhou " +
                "batida própria (o tamborzão) e hoje é exportado para o mundo.",
            bpmMin = 130,
            bpmMax = 150,
            keyArtists = "DJ Marlboro, Claudinho e Buchecha, Anitta, Ludmilla",
            curiosity = "O LP \"Funk Brasil\", de DJ Marlboro (1989), trouxe as primeiras " +
                "letras de funk cantadas em português."
        ),
        GenreEntity(
            id = 12,
            name = "K-Pop",
            originCountry = "Coreia do Sul",
            originYear = 1992,
            roots = "Pop, Hip-Hop, R&B, Eletrônica",
            description = "Pop sul-coreano com produção caprichada, coreografias sincronizadas e " +
                "grupos formados por anos de treinamento nas agências.",
            bpmMin = 100,
            bpmMax = 130,
            keyArtists = "Seo Taiji and Boys, BTS, BLACKPINK, TWICE",
            curiosity = "A estreia de Seo Taiji and Boys em 1992 tirou nota baixa dos jurados de " +
                "um programa de TV, mas a música virou um fenômeno e mudou o pop coreano."
        )
    )
}
