package com.Baxwope9.Rufnim.data.repository

import com.Baxwope9.Rufnim.data.local.FestivalPrefs
import com.Baxwope9.Rufnim.domain.model.FestivalResult
import com.Baxwope9.Rufnim.domain.model.FestivalScale
import com.Baxwope9.Rufnim.domain.model.Grade
import com.Baxwope9.Rufnim.domain.repository.FestivalRepository

class FestivalRepositoryImpl(private val prefs: FestivalPrefs) : FestivalRepository {

    override fun bestScore(): Int = prefs.readInt(FestivalPrefs.KEY_BEST_SCORE, 0)

    override fun bestGrade(): Grade? =
        prefs.readString(FestivalPrefs.KEY_BEST_GRADE)?.let { stored ->
            Grade.entries.firstOrNull { it.name == stored }
        }

    override fun festivalsPlayed(): Int = prefs.readInt(FestivalPrefs.KEY_FESTIVALS, 0)

    override fun scale(): FestivalScale {
        val stored = prefs.readString(FestivalPrefs.KEY_SCALE)
        return FestivalScale.entries.firstOrNull { it.name == stored } ?: FestivalScale.DISTRICT
    }

    override fun setScale(scale: FestivalScale) {
        prefs.writeString(FestivalPrefs.KEY_SCALE, scale.name)
    }

    override fun recordFestival(result: FestivalResult): Boolean {
        prefs.writeInt(FestivalPrefs.KEY_FESTIVALS, festivalsPlayed() + 1)
        val previous = bestScore()
        if (result.score <= previous) return false
        prefs.writeInt(FestivalPrefs.KEY_BEST_SCORE, result.score)
        prefs.writeString(FestivalPrefs.KEY_BEST_GRADE, result.grade.name)
        return true
    }
}
