package com.example.sonora.feature.albums.ui

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
import androidx.compose.foundation.lazy.items
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
import com.example.sonora.feature.albums.data.model.Album

/** Tela "Top Álbuns": lista com os álbuns mais ouvidos na Deezer (somente leitura). */
@Composable
fun AlbumsScreen(
    viewModel: AlbumsViewModel = viewModel(factory = AlbumsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AlbumsContent(uiState = uiState, onRetry = viewModel::loadAlbums)
}

@Composable
private fun AlbumsContent(
    uiState: AlbumsUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ScreenHeader(
            title = stringResource(R.string.albums_title),
            subtitle = stringResource(R.string.albums_subtitle)
        )
        when (uiState) {
            AlbumsUiState.Loading -> LoadingState()
            is AlbumsUiState.Error -> ErrorState(message = uiState.message, onRetry = onRetry)
            is AlbumsUiState.Success -> {
                if (uiState.albums.isEmpty()) {
                    EmptyState(message = stringResource(R.string.albums_empty))
                } else {
                    AlbumList(albums = uiState.albums)
                }
            }
        }
    }
}

@Composable
private fun AlbumList(albums: List<Album>) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(albums, key = { it.id }) { album ->
            AlbumCard(album = album)
        }
    }
}

@Composable
private fun AlbumCard(album: Album, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Posição grande à esquerda, destacada no top 3
            Text(
                text = album.position.toString(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = if (album.position <= 3) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.width(40.dp)
            )
            AsyncImage(
                model = album.coverUrl,
                contentDescription = stringResource(R.string.albums_cover_description, album.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = album.artistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Tag(
                        text = recordTypeLabel(album.recordType),
                        container = MaterialTheme.colorScheme.secondaryContainer,
                        content = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    if (album.explicit) {
                        Tag(
                            text = stringResource(R.string.albums_explicit),
                            container = MaterialTheme.colorScheme.errorContainer,
                            content = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

/** Traduz o record_type da Deezer para o rótulo exibido na tela. */
@Composable
private fun recordTypeLabel(recordType: String): String = stringResource(
    when (recordType) {
        "single" -> R.string.albums_type_single
        "ep" -> R.string.albums_type_ep
        "compile" -> R.string.albums_type_compile
        else -> R.string.albums_type_album
    }
)

@Composable
private fun Tag(text: String, container: Color, content: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(container)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = content
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D12)
@Composable
private fun AlbumsContentPreview() {
    SonoraTheme {
        AlbumsContent(
            uiState = AlbumsUiState.Success(
                listOf(
                    Album(1, 1, "No Sentimento em Brasília Vol. 1 (Ao Vivo)", "Felipe e Rodrigo", "album", false, null),
                    Album(2, 2, "60 Anos de Carreira (Ao Vivo)", "Maria Bethânia", "album", false, null),
                    Album(3, 3, "Single de Teste", "Artista", "single", true, null)
                )
            ),
            onRetry = {}
        )
    }
}
