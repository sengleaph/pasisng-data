package com.sifu.passdatascreen.passdata

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen

class ScreenTwo(
    private val game: Game
) : Screen {

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = game.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Genre: ${game.genre}")
            Text(text = "Platform: ${game.platform}")
            Text(text = "Publisher: ${game.publisher}")
            Text(text = "Release: ${game.releaseDate}")

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = game.shortDescription)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = game.description)
        }
    }
}