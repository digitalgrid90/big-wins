package com.Baxwope9.Rufnim.presentation.app

import androidx.lifecycle.ViewModel
import com.Baxwope9.Rufnim.core.navigation.Screen
import com.Baxwope9.Rufnim.domain.model.FestivalResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    private val backStack = ArrayDeque<Screen>()
    private var runCounter = 0

    fun onSplashFinished() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun startFestival() {
        runCounter++
        push(Screen.Game(runCounter))
    }

    fun openUpgrades(fromGame: Boolean) {
        push(Screen.Upgrades(fromGame))
    }

    fun openHowToPlay() {
        push(Screen.HowToPlay)
    }

    fun finishFestival(result: FestivalResult) {
        backStack.clear()
        _screen.value = Screen.GameOver(result)
        syncBack()
    }

    fun playAgain() {
        runCounter++
        backStack.clear()
        _screen.value = Screen.Game(runCounter)
        syncBack()
    }

    fun toMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        _screen.value = previous
        syncBack()
        return true
    }

    private fun push(target: Screen) {
        backStack.addLast(_screen.value)
        _screen.value = target
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
