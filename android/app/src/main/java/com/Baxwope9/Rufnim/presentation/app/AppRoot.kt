package com.Baxwope9.Rufnim.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Baxwope9.Rufnim.core.di.ServiceLocator
import com.Baxwope9.Rufnim.core.navigation.Screen
import com.Baxwope9.Rufnim.presentation.game.GameScreen
import com.Baxwope9.Rufnim.presentation.gameover.GameOverScreen
import com.Baxwope9.Rufnim.presentation.howto.HowToPlayScreen
import com.Baxwope9.Rufnim.presentation.menu.MenuScreen
import com.Baxwope9.Rufnim.presentation.splash.SplashScreen
import com.Baxwope9.Rufnim.presentation.upgrades.UpgradesScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            (fadeIn(tween(300)) + slideInVertically(tween(300)) { full -> full / 12 })
                .togetherWith(fadeOut(tween(220)))
        },
        modifier = Modifier.fillMaxSize(),
        label = "screen"
    ) { target ->
        when (target) {
            is Screen.Splash -> SplashScreen(onFinished = appViewModel::onSplashFinished)
            is Screen.Menu -> MenuScreen(
                onPlay = appViewModel::startFestival,
                onUpgrades = { appViewModel.openUpgrades(false) },
                onHowToPlay = appViewModel::openHowToPlay
            )
            is Screen.Game -> GameScreen(
                runId = target.runId,
                onFinished = appViewModel::finishFestival,
                onSpendPermits = { appViewModel.openUpgrades(true) },
                onMenu = appViewModel::toMenu
            )
            is Screen.GameOver -> GameOverScreen(
                result = target.result,
                onPlayAgain = appViewModel::playAgain,
                onMenu = appViewModel::toMenu
            )
            is Screen.Upgrades -> UpgradesScreen(
                fromGame = target.fromGame,
                onBack = { appViewModel.back() }
            )
            is Screen.HowToPlay -> HowToPlayScreen(onBack = { appViewModel.back() })
        }
    }
}
