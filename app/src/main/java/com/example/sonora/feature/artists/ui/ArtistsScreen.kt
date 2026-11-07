package com.example.sonora.feature.artists.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sonora.R
import com.example.sonora.core.ui.components.EmptyState
import com.example.sonora.core.ui.components.ErrorState
import com.example.sonora.core.ui.components.LoadingState
import com.example.sonora.core.ui.components.ScreenHeader
import com.example.sonora.core.ui.theme.SonoraTheme
import com.example.sonora.feature.artists.data.model.Artist

/** Tela "Top Artistas": grade com os artistas mais ouvidos na Deezer (somente leitura). */
@Composable
fun ArtistsScreen(
    viewModel: ArtistsViewModel = viewModel(factory = ArtistsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArtistsContent(uiState = uiState, onRetry = viewModel::loadArtists)
}

@Composable
private fun ArtistsContent(
    uiState: ArtistsUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(R.string.artists_title),
            subtitle = stringResource(R.string.artists_subtitle)
        )
        when (uiState) {
            ArtistsUiState.Loading -> LoadingState()
            is ArtistsUiState.Error -> ErrorState(message = uiState.message, onRetry = onRetry)
            is ArtistsUiState.Success -> {
                if (uiState.artists.isEmpty()) {
                    EmptyState(message = stringResource(R.string.artists_empty))
                } else {
                    ArtistGrid(artists = uiState.artists)
                }
            }
        }
    }
}

@Composable
private fun ArtistGrid(artists: List<Artist>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(artists, key = { it.id }) { artist ->
            ArtistCard(artist = artist)
        }
    }
}

@Composable
private fun ArtistCard(artist: Artist, modifier: Modifier = Modifier) {
    // O pódio (top 3) ganha borda na cor de destaque do tema
    val isPodium = artist.position <= 3
    val ringColor = if (isPodium) MaterialTheme.colorScheme.primary else Color.Transparent

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = artist.pictureUrl,
                    contentDescription = stringResource(R.string.artists_picture_description, artist.name),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .border(3.dp, ringColor, CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                PositionBadge(position = artist.position, highlighted = isPodium)
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = artist.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PositionBadge(position: Int, highlighted: Boolean) {
    val container = if (highlighted) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondaryContainer
    }
    val content = if (highlighted) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(container),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "#$position",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Black,
            color = content
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D12)
@Composable
private fun ArtistsContentPreview() {
    SonoraTheme {
        ArtistsContent(
            uiState = ArtistsUiState.Success(
                listOf(
                    Artist(1, 1, "Grupo Menos É Mais", null),
                    Artist(2, 2, "Henrique & Juliano", null),
                    Artist(3, 3, "Ana Castela", null),
                    Artist(4, 4, "Jorge & Mateus", null)
                )
            ),
            onRetry = {}
        )
    }
}
