package com.Baxwope9.Rufnim.presentation.menu

import androidx.lifecycle.ViewModel
import com.Baxwope9.Rufnim.domain.model.FestivalScale
import com.Baxwope9.Rufnim.domain.repository.FestivalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val festivalRepository: FestivalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.update {
            it.copy(
                bestScore = festivalRepository.bestScore(),
                bestGrade = festivalRepository.bestGrade(),
                festivalsPlayed = festivalRepository.festivalsPlayed(),
                scale = festivalRepository.scale()
            )
        }
    }

    fun selectScale(scale: FestivalScale) {
        festivalRepository.setScale(scale)
        _state.update { it.copy(scale = scale) }
    }
}
