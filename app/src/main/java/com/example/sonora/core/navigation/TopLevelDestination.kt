package com.example.sonora.core.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.sonora.R

/**
 * Abas da barra inferior. Para adicionar a aba de outro integrante,
 * basta incluir uma nova entrada aqui e o respectivo composable() em SonoraApp.
 */
enum class TopLevelDestination(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    TRACKS(
        route = "tracks",
        labelRes = R.string.nav_tracks,
        icon = Icons.Filled.Star
    ),
    DECADES(
        route = "decades",
        labelRes = R.string.nav_decades,
        icon = Icons.Filled.DateRange
    ),
    ARTISTS(
        route = "artists",
        labelRes = R.string.nav_artists,
        icon = Icons.Filled.Person
    ),
    INSTRUMENTS(
        route = "instruments",
        labelRes = R.string.nav_instruments,
        // O conjunto básico de ícones não tem instrumento; o sino é o mais próximo
        icon = Icons.Filled.Notifications
    ),
    ALBUMS(
        route = "albums",
        labelRes = R.string.nav_albums,
        icon = Icons.Filled.PlayArrow
    ),
    FESTIVALS(
        route = "festivals",
        labelRes = R.string.nav_festivals,
        icon = Icons.Filled.LocationOn
    )
}
