package com.Baxwope9.Rufnim.domain.model

enum class Grade { A, B, C, D }

enum class FestivalOutcome { COMPLETED, EVACUATED }

data class FestivalResult(
    val outcome: FestivalOutcome,
    val satisfaction: Int,
    val blocksDelivered: Int,
    val efficiency: Int,
    val incidentsResolved: Int,
    val peakBuzz: Float,
    val permitsUnspent: Int,
    val score: Int,
    val grade: Grade,
    val zoneCoverage: List<Pair<ZoneId, Int>>,
    val newBest: Boolean
)
