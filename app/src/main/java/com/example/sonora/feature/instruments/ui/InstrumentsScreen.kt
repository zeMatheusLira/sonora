package com.example.sonora.feature.instruments.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Place
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
import com.example.sonora.feature.instruments.data.model.Instrument

/** Cor de destaque de cada família de instrumentos. */
private fun familyColor(family: String): Color = when (family) {
    "Cordas" -> Color(0xFFFFA45C)
    "Sopro" -> Color(0xFF5CA8FF)
    "Percussão" -> Color(0xFFFF5C8A)
    "Teclas" -> Color(0xFF9D7BFF)
    else -> Color(0xFF4FD8C4)
}

/** Tela "Instrumentos Musicais": dados locais do Room com filtro por família (somente leitura). */
@Composable
fun InstrumentsScreen(
    viewModel: InstrumentsViewModel = viewModel(factory = InstrumentsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    InstrumentsContent(uiState = uiState, onFamilySelected = viewModel::selectFamily)
}

@Composable
private fun InstrumentsContent(
    uiState: InstrumentsUiState,
    onFamilySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(R.string.instruments_title),
            subtitle = stringResource(R.string.instruments_subtitle)
        )
        when (uiState) {
            InstrumentsUiState.Loading -> LoadingState()
            is InstrumentsUiState.Error -> ErrorState(message = uiState.message)
            is InstrumentsUiState.Success -> {
                FamilyFilter(
                    families = uiState.families,
                    selected = uiState.selectedFamily,
                    onSelected = onFamilySelected
                )
                if (uiState.instruments.isEmpty()) {
                    EmptyState(message = stringResource(R.string.instruments_empty))
                } else {
                    InstrumentList(instruments = uiState.instruments)
                }
            }
        }
    }
}

/** Chips de filtro: "Todos" + uma opção por família vinda do banco. */
@Composable
private fun FamilyFilter(
    families: List<String>,
    selected: String?,
    onSelected: (String?) -> Unit
) {
    // null representa a opção "Todos"
    val options = listOf<String?>(null) + families
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        items(options) { family ->
            val accent = family?.let(::familyColor) ?: MaterialTheme.colorScheme.primary
            FilterChip(
                selected = family == selected,
                onClick = { onSelected(family) },
                label = { Text(family ?: stringResource(R.string.instruments_filter_all)) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = accent.copy(alpha = 0.25f),
                    selectedLabelColor = accent
                )
            )
        }
    }
}

@Composable
private fun InstrumentList(instruments: List<Instrument>) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(instruments, key = { it.id }) { instrument ->
            InstrumentCard(instrument = instrument)
        }
    }
}

@Composable
private fun InstrumentCard(instrument: Instrument, modifier: Modifier = Modifier) {
    // Expandir/recolher é apenas estado visual da tela, não altera nenhum dado
    var expanded by rememberSaveable(instrument.id) { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "seta")
    val accent = familyColor(instrument.family)

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
                // "Avatar" com a inicial do instrumento na cor da família
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = instrument.name.take(1),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = accent
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = instrument.name, style = MaterialTheme.typography.titleLarge)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = instrument.family,
                            style = MaterialTheme.typography.labelLarge,
                            color = accent,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "  •  ",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = stringResource(R.string.instruments_origin),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = instrument.origin,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(
                        if (expanded) R.string.instruments_collapse else R.string.instruments_expand
                    ),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            Spacer(Modifier.height(12.dp))
            SectionLabel(stringResource(R.string.instruments_players))
            Text(
                text = instrument.famousPlayers.joinToString(" • "),
                style = MaterialTheme.typography.bodyMedium
            )

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    SectionLabel(stringResource(R.string.instruments_about))
                    Text(
                        text = instrument.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    CuriosityBox(text = instrument.curiosity, accent = accent)
                }
            }
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
private fun InstrumentsContentPreview() {
    SonoraTheme {
        InstrumentsContent(
            uiState = InstrumentsUiState.Success(
                families = listOf("Cordas", "Percussão", "Sopro", "Teclas"),
                selectedFamily = null,
                instruments = listOf(
                    Instrument(
                        id = 1,
                        name = "Violão",
                        family = "Cordas",
                        origin = "Espanha",
                        description = "Instrumento de seis cordas.",
                        famousPlayers = listOf("Baden Powell", "João Gilberto"),
                        curiosity = "Base da Bossa Nova."
                    )
                )
            ),
            onFamilySelected = {}
        )
    }
}
