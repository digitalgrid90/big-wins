package com.Baxwope9.Rufnim.core.navigation

import com.Baxwope9.Rufnim.domain.model.FestivalResult

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data class Game(val runId: Int) : Screen
    data class GameOver(val result: FestivalResult) : Screen
    data class Upgrades(val fromGame: Boolean) : Screen
    data object HowToPlay : Screen
}
