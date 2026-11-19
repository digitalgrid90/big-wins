package com.Baxwope9.Rufnim.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Baxwope9.Rufnim.presentation.app.AppViewModel
import com.Baxwope9.Rufnim.presentation.game.GameViewModel
import com.Baxwope9.Rufnim.presentation.gameover.GameOverViewModel
import com.Baxwope9.Rufnim.presentation.menu.MenuViewModel
import com.Baxwope9.Rufnim.presentation.splash.SplashViewModel
import com.Baxwope9.Rufnim.presentation.upgrades.UpgradesViewModel

class AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel() as T
        modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel() as T
        modelClass.isAssignableFrom(MenuViewModel::class.java) ->
            MenuViewModel(ServiceLocator.festivalRepository) as T
        modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
            festivalRepository = ServiceLocator.festivalRepository,
            upgradeRepository = ServiceLocator.upgradeRepository,
            blockPools = ServiceLocator.blockPools,
            generateBlock = ServiceLocator.generateBlock,
            resolveBlock = ServiceLocator.resolveBlock,
            drawIncident = ServiceLocator.drawIncident,
            applyBranch = ServiceLocator.applyBranch,
            scoreFestival = ServiceLocator.scoreFestival
        ) as T
        modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
            GameOverViewModel(ServiceLocator.festivalRepository) as T
        modelClass.isAssignableFrom(UpgradesViewModel::class.java) ->
            UpgradesViewModel(ServiceLocator.upgradeRepository, ServiceLocator.buyUpgrade) as T
        else -> throw IllegalArgumentException(modelClass.name)
    }
}
