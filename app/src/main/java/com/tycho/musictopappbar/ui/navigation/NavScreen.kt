package com.tycho.musictopappbar.ui.navigation

sealed class NavScreen(
    val title: String,
) {
    data object FavoriteScreen : NavScreen(title = "Favorite")
    data object PlaylistScreen : NavScreen(title = "Playlist")
    data object TracksScreen : NavScreen(title = "Tracks")
    data object AlbumsScreen : NavScreen(title = "Albums")
}
