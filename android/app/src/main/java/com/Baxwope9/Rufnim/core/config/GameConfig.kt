package com.Baxwope9.Rufnim.core.config

import com.Baxwope9.Rufnim.domain.model.ResourceType

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val BLOCKS = 6
    const val START_SATISFACTION = 72
    const val MAX_SATISFACTION = 100
    const val RESOLVE_DURATION_MS = 1100L
    const val COVERAGE_PIVOT = 0.72f
    const val COVERAGE_SWING = 18f
    const val BUZZ_STEP = 0.2f
    const val BUZZ_CAP = 2.0f
    const val BUZZ_BREAK_COVERAGE = 0.5f
    const val FATIGUE_FACTOR = 0.75f
    const val MAX_GENERATION_ATTEMPTS = 20
    const val ZONE_DEMAND_SHARE = 0.6f
    const val SCORE_SATISFACTION = 40
    const val SCORE_PERMIT = 25
    const val SCORE_BUZZ = 150
    const val SCORE_INCIDENT = 120
    const val GRADE_A = 4500
    const val GRADE_B = 3200
    const val GRADE_C = 2000

    val BASE_POOLS: Map<ResourceType, Int> = mapOf(
        ResourceType.VOLUNTEERS to 12,
        ResourceType.POWER to 10,
        ResourceType.TRANSPORT to 8,
        ResourceType.STAGE to 6
    )
}
