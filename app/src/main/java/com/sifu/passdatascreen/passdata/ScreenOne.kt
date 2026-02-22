package com.sifu.passdatascreen.passdata

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class ScreenOne : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        // Your Game object
        val game = Game(
            id = 452,
            title = "Call of Duty: Warzone",
            thumbnail = "https://www.freetogame.com/g/452/thumbnail.jpg",
            status = "Live",
            shortDescription = "A standalone free-to-play battle royale.",
            description = "Full game description here...",
            gameUrl = "https://www.freetogame.com/open/call-of-duty-warzone",
            genre = "fff",
            platform = "Windows",
            publisher = "Activision",
            developer = "Infinity Ward",
            releaseDate = "2020-03-10",
            profileUrl = "https://www.freetogame.com/call-of-duty-warzone",
            minimumSystemRequirements = MinimumSystemRequirements(
                os = "Windows 10",
                processor = "Intel Core i3",
                memory = "8GB",
                graphics = "GTX 670",
                storage = "175GB"
            ),
            screenshots = listOf(
                Screenshot(1124, "https://www.freetogame.com/g/452/Call-of-Duty-Warzone-1.jpg")
            )
        )

        Box() {
            Button(
                onClick = {
                    navigator.push(ScreenTwo(game))
                }
            ) {
                Text("Open Game Details")
            }
        }
    }
}