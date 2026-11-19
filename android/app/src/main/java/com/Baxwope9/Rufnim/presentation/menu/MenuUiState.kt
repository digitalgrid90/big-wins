package com.Baxwope9.Rufnim.presentation.menu

import com.Baxwope9.Rufnim.domain.model.FestivalScale
import com.Baxwope9.Rufnim.domain.model.Grade

data class MenuUiState(
    val bestScore: Int = 0,
    val bestGrade: Grade? = null,
    val festivalsPlayed: Int = 0,
    val scale: FestivalScale = FestivalScale.DISTRICT
)
