package com.example.sonora.feature.decades.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
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
import com.example.sonora.feature.decades.data.model.Decade

// Cor de destaque de cada card, alternando ao longo da lista
private val DecadeAccents = listOf(
    Color(0xFFFF5C8A),
    Color(0xFFFFA45C),
    Color(0xFF9D7BFF),
    Color(0xFF4FD8C4),
    Color(0xFF5CA8FF),
    Color(0xFFFFD25C),
    Color(0xFFE05CFF),
)

/** Tela "Décadas da Música": dados locais do Room (somente leitura). */
@Composable
fun DecadesScreen(
    viewModel: DecadesViewModel = viewModel(factory = DecadesViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DecadesContent(uiState = uiState)
}

@Composable
private fun DecadesContent(
    uiState: DecadesUiState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(R.string.decades_title),
            subtitle = stringResource(R.string.decades_subtitle)
        )
        when (uiState) {
            DecadesUiState.Loading -> LoadingState()
            is DecadesUiState.Error -> ErrorState(message = uiState.message)
            is DecadesUiState.Success -> {
                if (uiState.decades.isEmpty()) {
                    EmptyState(message = stringResource(R.string.decades_empty))
                } else {
                    DecadeList(decades = uiState.decades)
                }
            }
        }
    }
}

@Composable
private fun DecadeList(decades: List<Decade>) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(decades, key = { _, decade -> decade.id }) { index, decade ->
            DecadeCard(
                decade = decade,
                accent = DecadeAccents[index % DecadeAccents.size]
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DecadeCard(
    decade: Decade,
    accent: Color,
    modifier: Modifier = Modifier
) {
    // Expandir/recolher é apenas estado visual da tela, não altera nenhum dado
    var expanded by rememberSaveable(decade.id) { mutableStateOf(false) }
    val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "seta")

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
        // Faixa colorida no topo do card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Brush.horizontalGradient(listOf(accent, accent.copy(alpha = 0.2f))))
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = decade.startYear.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = decade.label, style = MaterialTheme.typography.titleLarge)
                }
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(
                        if (expanded) R.string.decades_collapse else R.string.decades_expand
                    ),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }

            Spacer(Modifier.height(12.dp))
            SectionLabel(stringResource(R.string.decades_genres))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                decade.topGenres.forEach { genre -> GenreTag(text = genre, accent = accent) }
            }

            Spacer(Modifier.height(12.dp))
            SectionLabel(stringResource(R.string.decades_artists))
            Text(
                text = decade.iconicArtists.joinToString(" • "),
                style = MaterialTheme.typography.bodyMedium
            )

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    SectionLabel(stringResource(R.string.decades_about))
                    Text(
                        text = decade.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            CuriosityBox(text = decade.curiosity, accent = accent)
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
private fun GenreTag(text: String, accent: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = accent,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(accent.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
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
private fun DecadesContentPreview() {
    SonoraTheme {
        DecadesContent(
            uiState = DecadesUiState.Success(
                listOf(
                    Decade(
                        id = 1,
                        label = "Anos 60",
                        startYear = 1960,
                        description = "A década da contracultura.",
                        topGenres = listOf("Rock", "Soul", "Bossa Nova"),
                        iconicArtists = listOf("The Beatles", "Aretha Franklin", "Tom Jobim"),
                        curiosity = "Woodstock reuniu cerca de 400 mil pessoas."
                    )
                )
            )
        )
    }
}
