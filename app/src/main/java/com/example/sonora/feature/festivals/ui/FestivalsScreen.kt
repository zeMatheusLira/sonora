package com.example.sonora.feature.festivals.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.sonora.feature.festivals.data.model.Festival

/** Cor de destaque de cada região. */
private fun regionColor(region: String): Color = when (region) {
    "Brasil" -> Color(0xFF4FD8C4)
    "Europa" -> Color(0xFF5CA8FF)
    "América do Norte" -> Color(0xFFFFA45C)
    "Ásia" -> Color(0xFFFF5C8A)
    else -> Color(0xFF9D7BFF)
}

/** Tela "Festivais de Música": linha do tempo vinda do Room com filtro por região (somente leitura). */
@Composable
fun FestivalsScreen(
    viewModel: FestivalsViewModel = viewModel(factory = FestivalsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FestivalsContent(uiState = uiState, onRegionSelected = viewModel::selectRegion)
}

@Composable
private fun FestivalsContent(
    uiState: FestivalsUiState,
    onRegionSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(R.string.festivals_title),
            subtitle = stringResource(R.string.festivals_subtitle)
        )
        when (uiState) {
            FestivalsUiState.Loading -> LoadingState()
            is FestivalsUiState.Error -> ErrorState(message = uiState.message)
            is FestivalsUiState.Success -> {
                RegionFilter(
                    regions = uiState.regions,
                    selected = uiState.selectedRegion,
                    onSelected = onRegionSelected
                )
                if (uiState.festivals.isEmpty()) {
                    EmptyState(message = stringResource(R.string.festivals_empty))
                } else {
                    FestivalTimeline(festivals = uiState.festivals)
                }
            }
        }
    }
}

/** Chips de filtro: "Todos" + uma opção por região vinda do banco. */
@Composable
private fun RegionFilter(
    regions: List<String>,
    selected: String?,
    onSelected: (String?) -> Unit
) {
    // null representa a opção "Todos"
    val options = listOf<String?>(null) + regions
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        items(options) { region ->
            val accent = region?.let(::regionColor) ?: MaterialTheme.colorScheme.primary
            FilterChip(
                selected = region == selected,
                onClick = { onSelected(region) },
                label = { Text(region ?: stringResource(R.string.festivals_filter_all)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = accent.copy(alpha = 0.25f),
                    selectedLabelColor = accent
                )
            )
        }
    }
}

@Composable
private fun FestivalTimeline(festivals: List<Festival>) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(festivals, key = { it.id }) { festival ->
            FestivalCard(festival = festival)
        }
    }
}

@Composable
private fun FestivalCard(festival: Festival, modifier: Modifier = Modifier) {
    // Expandir/recolher é apenas estado visual da tela, não altera nenhum dado
    var expanded by rememberSaveable(festival.id) { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "seta")
    val accent = regionColor(festival.region)

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
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // Faixa lateral na cor da região, como um marcador da linha do tempo
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accent)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.festivals_since, festival.firstEdition),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            color = accent
                        )
                        Text(text = festival.name, style = MaterialTheme.typography.titleLarge)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = stringResource(R.string.festivals_location),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${festival.city}, ${festival.country}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(
                            if (expanded) R.string.festivals_collapse else R.string.festivals_expand
                        ),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.rotate(arrowRotation)
                    )
                }

                Spacer(Modifier.height(10.dp))
                GenreTags(genres = festival.genres, accent = accent)

                AnimatedVisibility(visible = expanded) {
                    Column {
                        Spacer(Modifier.height(12.dp))
                        SectionLabel(stringResource(R.string.festivals_about))
                        Text(
                            text = festival.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(12.dp))
                        SectionLabel(stringResource(R.string.festivals_artists))
                        Text(
                            text = festival.iconicArtists.joinToString(" • "),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(12.dp))
                        CuriosityBox(text = festival.curiosity, accent = accent)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenreTags(genres: List<String>, accent: Color) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        genres.forEach { genre ->
            Text(
                text = genre,
                style = MaterialTheme.typography.labelMedium,
                color = accent,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(accent.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
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
private fun FestivalsContentPreview() {
    SonoraTheme {
        FestivalsContent(
            uiState = FestivalsUiState.Success(
                regions = listOf("América do Norte", "Brasil", "Europa", "Ásia"),
                selectedRegion = null,
                festivals = listOf(
                    Festival(
                        id = 5,
                        name = "Rock in Rio",
                        city = "Rio de Janeiro",
                        country = "Brasil",
                        region = "Brasil",
                        firstEdition = 1985,
                        genres = listOf("Rock", "Pop", "Metal"),
                        description = "Colocou o Brasil na rota dos grandes shows.",
                        iconicArtists = listOf("Queen", "Iron Maiden"),
                        curiosity = "O público cantou \"Love of My Life\" com o Queen."
                    )
                )
            ),
            onRegionSelected = {}
        )
    }
}
