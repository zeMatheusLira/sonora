# Arquitetura do Sonora

App Android de música (Kotlin + Jetpack Compose + Material 3), organizado em
**MVVM + Repository**, com pacotes **por feature**. Cada integrante do grupo
adiciona a sua própria feature (`feature/<nome>/`) com 2 telas, sua tabela no
Room e sua rota da API, sem precisar alterar o código dos outros.

Todas as telas são **somente de consulta e exibição** (sem cadastro, login,
edição ou exclusão).

## Diagrama

```mermaid
flowchart TD
    subgraph UI["UI (Jetpack Compose)"]
        Nav["SonoraApp<br/>Scaffold + NavigationBar"]
        TS["TracksScreen<br/>Top Músicas"]
        DS["DecadesScreen<br/>Décadas da Música"]
        AS["ArtistsScreen<br/>Top Artistas"]
        IS["InstrumentsScreen<br/>Instrumentos Musicais"]
        ALS["AlbumsScreen<br/>Top Álbuns"]
        FS["FestivalsScreen<br/>Festivais de Música"]
    end

    subgraph VM["ViewModel (StateFlow + viewModelScope)"]
        TVM["TracksViewModel<br/>TracksUiState"]
        DVM["DecadesViewModel<br/>DecadesUiState"]
        AVM["ArtistsViewModel<br/>ArtistsUiState"]
        IVM["InstrumentsViewModel<br/>InstrumentsUiState"]
        ALVM["AlbumsViewModel<br/>AlbumsUiState"]
        FVM["FestivalsViewModel<br/>FestivalsUiState"]
    end

    subgraph REPO["Repository"]
        TR["TracksRepository<br/>Result&lt;List&lt;Track&gt;&gt;"]
        DR["DecadesRepository<br/>Flow&lt;List&lt;Decade&gt;&gt;"]
        AR["ArtistsRepository<br/>Result&lt;List&lt;Artist&gt;&gt;"]
        IR["InstrumentsRepository<br/>Flow&lt;List&lt;Instrument&gt;&gt;"]
        ALR["AlbumsRepository<br/>Result&lt;List&lt;Album&gt;&gt;"]
        FR["FestivalsRepository<br/>Flow&lt;List&lt;Festival&gt;&gt;"]
    end

    subgraph DATA["Fontes de dados"]
        API["DeezerApi (Retrofit)<br/>GET chart/0/tracks"]
        DAO["DecadeDao (Room)<br/>tabela decades"]
        AAPI["ArtistsApi (Retrofit)<br/>GET chart/0/artists"]
        IDAO["InstrumentDao (Room)<br/>tabela instruments"]
        ALAPI["AlbumsApi (Retrofit)<br/>GET chart/0/albums"]
        FDAO["FestivalDao (Room)<br/>tabela festivals"]
        DB[("AppDatabase<br/>sonora.db")]
        NET(("api.deezer.com"))
    end

    APP["SonoraApplication<br/>AppContainer (DI manual)"]

    Nav --> TS & DS & AS & IS & ALS & FS
    TS --> TVM --> TR --> API --> NET
    DS --> DVM --> DR --> DAO --> DB
    AS --> AVM --> AR --> AAPI --> NET
    IS --> IVM --> IR --> IDAO --> DB
    ALS --> ALVM --> ALR --> ALAPI --> NET
    FS --> FVM --> FR --> FDAO --> DB
    APP -. fornece Retrofit e banco .-> TR & DR & AR & IR & ALR & FR
```

Fluxo de dados: a UI coleta o `StateFlow` do ViewModel com
`collectAsStateWithLifecycle()`. O ViewModel chama o Repository dentro de
`viewModelScope`. O Repository usa `suspend fun` (Retrofit) ou `Flow` (Room) e
converte DTOs/entities para modelos de domínio. Erros de rede são tratados no
Repository e chegam à UI como `UiState.Error`.

## Estrutura de pacotes

```
com.example.sonora
├── SonoraApplication.kt          # Application: cria o AppContainer
├── MainActivity.kt               # setContent { SonoraApp() }
├── core/
│   ├── di/AppContainer.kt        # injeção manual: Retrofit, AppDatabase, repositórios
│   ├── network/NetworkModule.kt  # OkHttp + logging + Retrofit (base URL da Deezer)
│   ├── database/AppDatabase.kt   # banco único; cada integrante registra sua entity/DAO
│   ├── navigation/
│   │   ├── TopLevelDestination.kt  # abas da NavigationBar
│   │   └── SonoraApp.kt            # Scaffold + NavigationBar + NavHost
│   └── ui/
│       ├── components/StateViews.kt  # Loading / Erro / Vazio reutilizáveis
│       └── theme/                    # Color, Theme (escuro), Type
└── feature/
    ├── tracks/                   # Integrante 1 — Retrofit
    │   ├── data/
    │   │   ├── remote/DeezerApi.kt
    │   │   ├── remote/dto/ChartTracksResponse.kt
    │   │   ├── model/Track.kt
    │   │   └── TracksRepository.kt
    │   └── ui/
    │       ├── TracksUiState.kt
    │       ├── TracksViewModel.kt
    │       └── TracksScreen.kt
    └── decades/                  # Integrante 1 — Room
        ├── data/
        │   ├── local/DecadeEntity.kt
        │   ├── local/DecadeDao.kt
        │   ├── local/DecadesSeed.kt   # seed inicial (SeedCallback.onOpen, se a tabela estiver vazia)
        │   ├── model/Decade.kt
        │   └── DecadesRepository.kt
        └── ui/
            ├── DecadesUiState.kt
            ├── DecadesViewModel.kt
            └── DecadesScreen.kt
    ├── artists/                  # Top Artistas — Retrofit
    │   ├── data/
    │   │   ├── remote/ArtistsApi.kt
    │   │   ├── remote/dto/ChartArtistsResponse.kt
    │   │   ├── model/Artist.kt
    │   │   └── ArtistsRepository.kt
    │   └── ui/ (ArtistsUiState, ArtistsViewModel, ArtistsScreen)
    └── instruments/              # Instrumentos Musicais — Room
        ├── data/
        │   ├── local/InstrumentEntity.kt
        │   ├── local/InstrumentDao.kt
        │   ├── local/InstrumentsSeed.kt
        │   ├── model/Instrument.kt
        │   └── InstrumentsRepository.kt
        └── ui/ (InstrumentsUiState, InstrumentsViewModel, InstrumentsScreen)
    ├── albums/                   # Integrante 3 — Top Álbuns — Retrofit
    │   ├── data/
    │   │   ├── remote/AlbumsApi.kt
    │   │   ├── remote/dto/ChartAlbumsResponse.kt
    │   │   ├── model/Album.kt
    │   │   └── AlbumsRepository.kt
    │   └── ui/ (AlbumsUiState, AlbumsViewModel, AlbumsScreen)
    └── festivals/                # Integrante 3 — Festivais de Música — Room
        ├── data/
        │   ├── local/FestivalEntity.kt
        │   ├── local/FestivalDao.kt
        │   ├── local/FestivalsSeed.kt
        │   ├── model/Festival.kt
        │   └── FestivalsRepository.kt
        └── ui/ (FestivalsUiState, FestivalsViewModel, FestivalsScreen)
```

### Como outro integrante adiciona a sua feature

1. Criar `feature/<nome>/data` e `feature/<nome>/ui`.
2. **Room:** criar a sua `@Entity` e o seu `@Dao`; em `AppDatabase` acrescentar a
   entity em `entities = [...]`, uma `abstract fun <nome>Dao()` e **subir a
   `version`** (com `Migration` ou `fallbackToDestructiveMigration`). Se tiver
   seed, adicionar `seedIfEmpty(db, <TABELA>) { <Seed>.populate(it) }` em
   `AppDatabase.SeedCallback.onOpen`. Não use `onDestructiveMigration` para o seed:
   o Room o chama depois de apagar as tabelas e antes de recriá-las, e o insert
   falha com "no such table".
3. **Retrofit:** criar a própria interface (ex.: `AlbumsApi`) e obtê-la com
   `container.retrofit.create(AlbumsApi::class.java)`.
4. Expor o repositório em `AppContainer`.
5. Adicionar uma entrada em `TopLevelDestination` e um `composable(...)` no
   `NavHost` de `SonoraApp`.

## Dependências e versões

Versões confirmadas no Maven Central / Google Maven e compatíveis com o
AGP 8.7.3 e Kotlin 2.0.21 já usados pelo projeto.

| Biblioteca | Artefato | Versão |
|---|---|---|
| Android Gradle Plugin | `com.android.application` | 8.7.3 |
| Kotlin | `org.jetbrains.kotlin.android` / `plugin.compose` | 2.0.21 |
| KSP | `com.google.devtools.ksp` | 2.0.21-1.0.28 |
| Compose BOM | `androidx.compose:compose-bom` | 2024.12.01 |
| Material 3 | `androidx.compose.material3:material3` | (BOM) |
| Activity Compose | `androidx.activity:activity-compose` | 1.9.3 |
| Lifecycle (runtime, ViewModel Compose, runtime Compose) | `androidx.lifecycle:*` | 2.8.7 |
| Navigation Compose | `androidx.navigation:navigation-compose` | 2.8.5 |
| Coroutines | `org.jetbrains.kotlinx:kotlinx-coroutines-android` | 1.9.0 |
| Retrofit | `com.squareup.retrofit2:retrofit` | 2.11.0 |
| Conversor Gson | `com.squareup.retrofit2:converter-gson` | 2.11.0 |
| OkHttp Logging | `com.squareup.okhttp3:logging-interceptor` | 4.12.0 |
| Room (runtime, ktx, compiler via KSP) | `androidx.room:*` | 2.6.1 |
| Coil (Compose) | `io.coil-kt:coil-compose` | 2.7.0 |

Sem Hilt/Dagger: a injeção é manual via `SonoraApplication` → `AppContainer`.
O schema do Room é exportado em `app/schemas/` (`exportSchema = true`).

## API utilizada (Integrante 1)

`GET https://api.deezer.com/chart/0/tracks` (sem chave). Resposta validada
com curl:

```json
{
  "data": [
    {
      "id": 4337389322,
      "title": "Pós (Ao Vivo)",
      "duration": 175,
      "position": 1,
      "preview": "https://cdnt-preview.dzcdn.net/...mp3",
      "artist": { "id": 172071177, "name": "Felipe e Rodrigo", "...": "..." },
      "album": {
        "id": 1117358492,
        "title": "Pós (Ao Vivo)",
        "cover_medium": "https://cdn-images.dzcdn.net/.../250x250-000000-80-0-0.jpg",
        "cover_big": "https://cdn-images.dzcdn.net/.../500x500-000000-80-0-0.jpg"
      }
    }
  ],
  "total": 10
}
```

Em caso de falha a Deezer responde **HTTP 200** com
`{"error": {"type": "...", "message": "...", "code": 0}}`, por isso o
Repository também verifica esse campo.

## API utilizada (Top Artistas)

`GET https://api.deezer.com/chart/0/artists` (sem chave). Resposta validada
com curl; campos usados: `id`, `name`, `position`, `picture_medium`,
`picture_big`. Mesmo tratamento de erro (`error` com HTTP 200) da tela Top Músicas.

## Banco local (Instrumentos Musicais)

Tabela `instruments` (id, name, family, origin, description, famous_players,
curiosity), com 12 instrumentos em 4 famílias (Cordas, Sopro, Percussão,
Teclas). O DAO só tem consultas: `getAllInstruments()`,
`getInstrumentsByFamily(family)` e `getFamilies()`, todas em `Flow`. O filtro
por família da tela troca a consulta com `flatMapLatest`, sem alterar dados.

## API utilizada (Top Álbuns, integrante 3)

`GET https://api.deezer.com/chart/0/albums` (sem chave). Resposta validada
com curl; campos usados: `id`, `title`, `position`, `record_type`,
`explicit_lyrics`, `cover_medium`, `cover_big` e `artist.name`. Mesmo
tratamento de erro (`error` com HTTP 200) das outras telas da Deezer.

## Banco local (Festivais de Música, integrante 3)

Tabela `festivals` (id, name, city, country, region, first_edition, genres,
description, iconic_artists, curiosity), com 10 festivais em 4 regiões
(Brasil, Europa, América do Norte, Ásia). O DAO só tem consultas:
`getAllFestivals()`, `getFestivalsByRegion(region)` e `getRegions()`, todas em
`Flow` e ordenadas pelo ano da primeira edição (linha do tempo). O filtro por
região troca a consulta com `flatMapLatest`. Banco na `version = 3`.

## Build

O Gradle 8.11.1 não roda no JDK 25. Use o JDK 21 (o JBR do Android Studio ou o
Corretto 21), por exemplo:
`JAVA_HOME=<caminho do JDK 21> ./gradlew assembleDebug`.
