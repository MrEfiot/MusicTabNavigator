package com.tycho.musictopappbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tycho.musictopappbar.ui.component.topAppBar
import com.tycho.musictopappbar.ui.navigation.NavScreen
import com.tycho.musictopappbar.ui.screens.AlbumsScreen
import com.tycho.musictopappbar.ui.screens.FavoriteScreen
import com.tycho.musictopappbar.ui.screens.PlaylistScreen
import com.tycho.musictopappbar.ui.screens.TracksScreen
import com.tycho.musictopappbar.ui.theme.MusicTopAppBarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicTopAppBarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val selectedScreen = topAppBar()
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        when (selectedScreen) {
                            NavScreen.FavoriteScreen -> FavoriteScreen()
                            NavScreen.PlaylistScreen -> PlaylistScreen()
                            NavScreen.TracksScreen -> TracksScreen()
                            NavScreen.AlbumsScreen -> AlbumsScreen()
                        }
                    }

                }
            }
        }
    }
}
