package com.example.sonora.feature.playlists.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.sonora.feature.playlists.data.model.Playlist

/** Tela "Top Playlists": grade com as playlists em alta na Deezer (somente leitura). */
@Composable
fun PlaylistsScreen(
    viewModel: PlaylistsViewModel = viewModel(factory = PlaylistsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaylistsContent(uiState = uiState, onRetry = viewModel::loadPlaylists)
}

@Composable
private fun PlaylistsContent(
    uiState: PlaylistsUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(R.string.playlists_title),
            subtitle = stringResource(R.string.playlists_subtitle)
        )
        when (uiState) {
            PlaylistsUiState.Loading -> LoadingState()
            is PlaylistsUiState.Error -> ErrorState(message = uiState.message, onRetry = onRetry)
            is PlaylistsUiState.Success -> {
                if (uiState.playlists.isEmpty()) {
                    EmptyState(message = stringResource(R.string.playlists_empty))
                } else {
                    PlaylistGrid(playlists = uiState.playlists)
                }
            }
        }
    }
}

@Composable
private fun PlaylistGrid(playlists: List<Playlist>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(playlists, key = { it.id }) { playlist ->
            PlaylistCard(playlist = playlist)
        }
    }
}

@Composable
private fun PlaylistCard(playlist: Playlist, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = playlist.coverUrl,
                contentDescription = stringResource(R.string.playlists_cover_description, playlist.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Degradê na base da capa para a quantidade de faixas ficar legível sobre qualquer imagem
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.BottomStart)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xCC000000))))
            )
            Text(
                text = pluralStringResource(
                    R.plurals.playlists_tracks,
                    playlist.trackCount,
                    playlist.trackCount
                ),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            )
            // Posição no chart, destacada no top 3
            Text(
                text = "#${playlist.position}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = if (playlist.position <= 3) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (playlist.position <= 3) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.85f)
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = playlist.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (playlist.curator.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.playlists_by, playlist.curator),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D12)
@Composable
private fun PlaylistsContentPreview() {
    SonoraTheme {
        PlaylistsContent(
            uiState = PlaylistsUiState.Success(
                listOf(
                    Playlist(1, 1, "Top 50 Sertanejo", 50, "Daniel - Editor Sênior Deezer Brasil", null),
                    Playlist(2, 2, "Hitou", 80, "Laura - Editora Deezer Brasil", null),
                    Playlist(3, 3, "Funk Hits", 60, "Deezer Brasil", null),
                    Playlist(4, 4, "Pagodeiros", 1, "Deezer Brasil", null)
                )
            ),
            onRetry = {}
        )
    }
}
