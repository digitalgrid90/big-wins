package com.Baxwope9.Rufnim.domain.usecase

import com.Baxwope9.Rufnim.domain.model.FestivalOutcome
import com.Baxwope9.Rufnim.domain.model.FestivalResult
import com.Baxwope9.Rufnim.domain.model.FestivalScale
import com.Baxwope9.Rufnim.domain.model.Grade
import com.Baxwope9.Rufnim.domain.model.ZoneId
import kotlin.math.roundToInt

class ScoreFestivalUseCase(
    private val satisfactionWeight: Int,
    private val permitWeight: Int,
    private val buzzWeight: Int,
    private val incidentWeight: Int,
    private val gradeA: Int,
    private val gradeB: Int,
    private val gradeC: Int
) {

    fun score(
        outcome: FestivalOutcome,
        satisfaction: Int,
        blocksDelivered: Int,
        efficiency: Int,
        incidentsResolved: Int,
        peakBuzz: Float,
        permitsUnspent: Int,
        zoneCoverage: List<Pair<ZoneId, Int>>,
        scale: FestivalScale
    ): FestivalResult {
        val raw = satisfaction * satisfactionWeight +
            permitsUnspent * permitWeight +
            (peakBuzz * buzzWeight).roundToInt() +
            incidentsResolved * incidentWeight
        val score = (raw * scale.scoreMultiplier).roundToInt()
        val grade = when {
            score >= gradeA -> Grade.A
            score >= gradeB -> Grade.B
            score >= gradeC -> Grade.C
            else -> Grade.D
        }
        return FestivalResult(
            outcome = outcome,
            satisfaction = satisfaction,
            blocksDelivered = blocksDelivered,
            efficiency = efficiency,
            incidentsResolved = incidentsResolved,
            peakBuzz = peakBuzz,
            permitsUnspent = permitsUnspent,
            score = score,
            grade = grade,
            zoneCoverage = zoneCoverage,
            newBest = false
        )
    }
}
