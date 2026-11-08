package com.example.sonora.feature.instruments.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Seed inicial da tabela "instruments", executado quando a tabela está vazia ao abrir o banco
 * (AppDatabase.SeedCallback.onOpen). Não existe tela para o usuário inserir ou editar dados.
 */
object InstrumentsSeed {

    private const val CORDAS = "Cordas"
    private const val SOPRO = "Sopro"
    private const val PERCUSSAO = "Percussão"
    private const val TECLAS = "Teclas"

    fun populate(db: SupportSQLiteDatabase) {
        instruments.forEach { instrument ->
            val values = ContentValues().apply {
                put("id", instrument.id)
                put("name", instrument.name)
                put("family", instrument.family)
                put("origin", instrument.origin)
                put("description", instrument.description)
                put("famous_players", instrument.famousPlayers)
                put("curiosity", instrument.curiosity)
            }
            db.insert(InstrumentEntity.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values)
        }
    }

    val instruments = listOf(
        InstrumentEntity(
            id = 1,
            name = "Violão",
            family = CORDAS,
            origin = "Espanha",
            description = "Instrumento de seis cordas tocado com os dedos ou palheta. No Brasil " +
                "é a base do choro, da Bossa Nova, do sertanejo e da MPB.",
            famousPlayers = "Baden Powell, João Gilberto, Paco de Lucía, Yamandu Costa",
            curiosity = "A batida de violão de João Gilberto em \"Chega de Saudade\" (1958) " +
                "é considerada o marco inicial da Bossa Nova."
        ),
        InstrumentEntity(
            id = 2,
            name = "Guitarra elétrica",
            family = CORDAS,
            origin = "Estados Unidos",
            description = "Usa captadores magnéticos e um amplificador para produzir o som. " +
                "Virou o símbolo do rock, mas também está no blues, no jazz e no pop.",
            famousPlayers = "Jimi Hendrix, B.B. King, Eric Clapton, Pepeu Gomes",
            curiosity = "A Fender Stratocaster, lançada em 1954, é um dos modelos de guitarra " +
                "mais copiados da história."
        ),
        InstrumentEntity(
            id = 3,
            name = "Violino",
            family = CORDAS,
            origin = "Itália",
            description = "O menor e mais agudo da família das cordas friccionadas. Tocado com " +
                "arco, é peça central da orquestra e também aparece na música popular.",
            famousPlayers = "Niccolò Paganini, Itzhak Perlman, Anne-Sophie Mutter",
            curiosity = "Violinos feitos por Antonio Stradivari no século XVIII já foram " +
                "vendidos por milhões de dólares."
        ),
        InstrumentEntity(
            id = 4,
            name = "Cavaquinho",
            family = CORDAS,
            origin = "Portugal",
            description = "Pequeno instrumento de quatro cordas trazido pelos portugueses. " +
                "No Brasil virou peça fundamental do samba e do choro.",
            famousPlayers = "Waldir Azevedo, Paulinho da Viola, Mestre Marçal",
            curiosity = "\"Brasileirinho\", de Waldir Azevedo, é um dos choros mais gravados " +
                "de todos os tempos."
        ),
        InstrumentEntity(
            id = 5,
            name = "Saxofone",
            family = SOPRO,
            origin = "Bélgica",
            description = "Inventado por Adolphe Sax na década de 1840. Mistura o corpo de metal " +
                "com a palheta dos instrumentos de madeira e marcou a história do jazz.",
            famousPlayers = "Charlie Parker, John Coltrane, Paulo Moura",
            curiosity = "Apesar de ser feito de metal, o saxofone é classificado como " +
                "instrumento de madeira por usar palheta."
        ),
        InstrumentEntity(
            id = 6,
            name = "Flauta transversal",
            family = SOPRO,
            origin = "Europa",
            description = "Tocada na lateral, soprando sobre um orifício. Tem som leve e ágil e " +
                "é muito usada no choro e na música erudita.",
            famousPlayers = "Pixinguinha, Altamiro Carrilho, Jean-Pierre Rampal",
            curiosity = "Flautas feitas de osso, com mais de 40 mil anos, estão entre os " +
                "instrumentos musicais mais antigos já encontrados."
        ),
        InstrumentEntity(
            id = 7,
            name = "Sanfona",
            family = SOPRO,
            origin = "Europa",
            description = "Também chamada de acordeão. O fole empurra o ar por palhetas metálicas. " +
                "É a alma do forró e do baião no Nordeste brasileiro.",
            famousPlayers = "Luiz Gonzaga, Dominguinhos, Sivuca",
            curiosity = "Luiz Gonzaga, o Rei do Baião, levou a sanfona do sertão para as " +
                "rádios de todo o Brasil nos anos 40."
        ),
        InstrumentEntity(
            id = 8,
            name = "Bateria",
            family = PERCUSSAO,
            origin = "Estados Unidos",
            description = "Conjunto de tambores e pratos tocado por uma só pessoa com baquetas e " +
                "pedais. Segura o ritmo no rock, no pop e no jazz.",
            famousPlayers = "John Bonham, Ringo Starr, Neil Peart",
            curiosity = "A bateria surgiu no início do século XX, quando um único músico " +
                "passou a tocar bumbo, caixa e pratos ao mesmo tempo."
        ),
        InstrumentEntity(
            id = 9,
            name = "Pandeiro",
            family = PERCUSSAO,
            origin = "Península Ibérica",
            description = "Aro com pele e platinelas de metal. Chegou com os portugueses e se " +
                "tornou um dos símbolos do samba e do choro.",
            famousPlayers = "Jackson do Pandeiro, Marcos Suzano, Jorginho do Pandeiro",
            curiosity = "Jackson do Pandeiro gostava tanto do instrumento que o incorporou " +
                "ao próprio nome artístico."
        ),
        InstrumentEntity(
            id = 10,
            name = "Berimbau",
            family = PERCUSSAO,
            origin = "África",
            description = "Arco de madeira com uma corda de aço e uma cabaça que amplifica o som. " +
                "É tocado com uma baqueta e um caxixi.",
            famousPlayers = "Naná Vasconcelos, Mestre Bimba, Mestre Pastinha",
            curiosity = "Na capoeira é o berimbau que comanda a roda e define o ritmo do jogo."
        ),
        InstrumentEntity(
            id = 11,
            name = "Piano",
            family = TECLAS,
            origin = "Itália",
            description = "Criado por Bartolomeo Cristofori por volta de 1700. Cada tecla aciona um " +
                "martelo que percute as cordas, permitindo tocar forte ou piano (suave).",
            famousPlayers = "Frédéric Chopin, Nelson Freire, Chiquinha Gonzaga, Elton John",
            curiosity = "O piano moderno tem 88 teclas: 52 brancas e 36 pretas."
        ),
        InstrumentEntity(
            id = 12,
            name = "Sintetizador",
            family = TECLAS,
            origin = "Estados Unidos",
            description = "Gera sons eletronicamente a partir de osciladores e filtros. " +
                "Revolucionou o pop, a música eletrônica e as trilhas sonoras.",
            famousPlayers = "Wendy Carlos, Jean-Michel Jarre, Herbie Hancock",
            curiosity = "Robert Moog apresentou seus primeiros módulos de sintetizador em 1964."
        )
    )
}
