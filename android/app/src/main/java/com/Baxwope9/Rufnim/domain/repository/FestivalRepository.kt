package com.Baxwope9.Rufnim.domain.repository

import com.Baxwope9.Rufnim.domain.model.FestivalResult
import com.Baxwope9.Rufnim.domain.model.FestivalScale
import com.Baxwope9.Rufnim.domain.model.Grade

interface FestivalRepository {
    fun bestScore(): Int
    fun bestGrade(): Grade?
    fun festivalsPlayed(): Int
    fun scale(): FestivalScale
    fun setScale(scale: FestivalScale)
    fun recordFestival(result: FestivalResult): Boolean
}
