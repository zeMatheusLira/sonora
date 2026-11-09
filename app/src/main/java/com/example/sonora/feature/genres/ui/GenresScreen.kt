package com.example.sonora.feature.genres.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sonora.R
import com.example.sonora.core.ui.components.EmptyState
import com.example.sonora.core.ui.components.ErrorState
import com.example.sonora.core.ui.components.LoadingState
import com.example.sonora.core.ui.components.ScreenHeader
import com.example.sonora.core.ui.theme.SonoraTheme
import com.example.sonora.feature.genres.data.model.Genre

/** Escala do medidor de andamento (BPM) exibido em cada card. */
private const val BPM_SCALE_MIN = 40
private const val BPM_SCALE_MAX = 200

/** Cor de destaque de cada gênero, alternando numa paleta fixa pelo id. */
private val genrePalette = listOf(
    Color(0xFF5CA8FF),
    Color(0xFFFFA45C),
    Color(0xFF4FD8C4),
    Color(0xFFFF5C8A),
    Color(0xFF9D7BFF),
    Color(0xFFE8D45C)
)

private fun genreColor(genre: Genre): Color = genrePalette[genre.id % genrePalette.size]

/** Tela "Gêneros Musicais": lista vinda do Room com busca por nome, raízes ou artistas (somente leitura). */
@Composable
fun GenresScreen(
    viewModel: GenresViewModel = viewModel(factory = GenresViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    GenresContent(uiState = uiState, query = query, onQueryChange = viewModel::onQueryChange)
}

@Composable
private fun GenresContent(
    uiState: GenresUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(R.string.genres_title),
            subtitle = stringResource(R.string.genres_subtitle)
        )
        SearchField(query = query, onQueryChange = onQueryChange)
        when (uiState) {
            GenresUiState.Loading -> LoadingState()
            is GenresUiState.Error -> ErrorState(message = uiState.message)
            is GenresUiState.Success -> {
                if (uiState.genres.isEmpty()) {
                    EmptyState(
                        message = if (query.isBlank()) {
                            stringResource(R.string.genres_empty)
                        } else {
                            stringResource(R.string.genres_no_results, query.trim())
                        }
                    )
                } else {
                    GenreList(genres = uiState.genres)
                }
            }
        }
    }
}

/** Campo de busca: só filtra a consulta ao banco, não grava nada. */
@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
        placeholder = { Text(stringResource(R.string.genres_search_hint)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.genres_search_clear))
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
private fun GenreList(genres: List<Genre>) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(genres, key = { it.id }) { genre ->
            GenreCard(genre = genre)
        }
    }
}

@Composable
private fun GenreCard(genre: Genre, modifier: Modifier = Modifier) {
    // Expandir/recolher é apenas estado visual da tela, não altera nenhum dado
    var expanded by rememberSaveable(genre.id) { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "seta")
    val accent = genreColor(genre)

    Card(
        onClick = { expanded = !expanded },
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Inicial do gênero num círculo colorido
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(50))
                        .background(accent.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = genre.name.take(1),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = accent
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = genre.name, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = stringResource(R.string.genres_origin, genre.originCountry, genre.originYear),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(
                        if (expanded) R.string.genres_collapse else R.string.genres_expand
                    ),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            Spacer(Modifier.height(14.dp))
            BpmMeter(range = genre.bpmRange, accent = accent)

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(Modifier.height(14.dp))
                    SectionLabel(stringResource(R.string.genres_about))
                    Text(
                        text = genre.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    SectionLabel(stringResource(R.string.genres_roots))
                    Text(
                        text = genre.roots.joinToString(" • "),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    SectionLabel(stringResource(R.string.genres_artists))
                    Text(
                        text = genre.keyArtists.joinToString(" • "),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    CuriosityBox(text = genre.curiosity, accent = accent)
                }
            }
        }
    }
}

/** Barra de 40 a 200 BPM com a faixa típica do gênero destacada. */
@Composable
private fun BpmMeter(range: IntRange, accent: Color) {
    val span = (BPM_SCALE_MAX - BPM_SCALE_MIN).toFloat()
    val start = ((range.first.coerceIn(BPM_SCALE_MIN, BPM_SCALE_MAX) - BPM_SCALE_MIN) / span)
    val end = ((range.last.coerceIn(BPM_SCALE_MIN, BPM_SCALE_MAX) - BPM_SCALE_MIN) / span)

    Column {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = stringResource(R.string.genres_tempo).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.genres_bpm, range.first, range.last),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = accent
            )
        }
        Spacer(Modifier.height(6.dp))
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = maxWidth * start)
                    .width(maxWidth * (end - start).coerceAtLeast(0.02f))
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(accent)
            )
        }
        Row {
            Text(
                text = BPM_SCALE_MIN.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$BPM_SCALE_MAX+",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun CuriosityBox(text: String, accent: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D12)
@Composable
private fun GenresContentPreview() {
    SonoraTheme {
        GenresContent(
            uiState = GenresUiState.Success(
                listOf(
                    Genre(
                        id = 2,
                        name = "Samba",
                        originCountry = "Brasil",
                        originYear = 1916,
                        roots = listOf("Lundu", "Maxixe", "Batuque"),
                        description = "Ganhou forma nas casas das tias baianas no Rio de Janeiro.",
                        bpmRange = 90..110,
                        keyArtists = listOf("Cartola", "Clara Nunes"),
                        curiosity = "\"Pelo Telefone\" é considerado o primeiro samba gravado."
                    ),
                    Genre(
                        id = 7,
                        name = "Reggae",
                        originCountry = "Jamaica",
                        originYear = 1968,
                        roots = listOf("Ska", "Rocksteady"),
                        description = "Ritmo marcado pelo contratempo da guitarra.",
                        bpmRange = 60..90,
                        keyArtists = listOf("Bob Marley", "Peter Tosh"),
                        curiosity = "Patrimônio Cultural Imaterial da Humanidade desde 2018."
                    )
                )
            ),
            query = "",
            onQueryChange = {}
        )
    }
}
