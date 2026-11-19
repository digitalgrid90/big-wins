package com.Baxwope9.Rufnim.presentation.gameover

import androidx.lifecycle.ViewModel
import com.Baxwope9.Rufnim.domain.model.Grade
import com.Baxwope9.Rufnim.domain.repository.FestivalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GameOverUiState(
    val bestScore: Int = 0,
    val bestGrade: Grade? = null,
    val festivalsPlayed: Int = 0
)

class GameOverViewModel(
    private val festivalRepository: FestivalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.update {
            it.copy(
                bestScore = festivalRepository.bestScore(),
                bestGrade = festivalRepository.bestGrade(),
                festivalsPlayed = festivalRepository.festivalsPlayed()
            )
        }
    }
}
