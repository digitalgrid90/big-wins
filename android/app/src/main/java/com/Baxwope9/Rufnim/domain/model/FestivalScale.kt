package com.Baxwope9.Rufnim.domain.model

enum class FestivalScale(
    val poolFactor: Float,
    val incidentChance: Int,
    val scoreMultiplier: Float
) {
    CITY_BLOCK(0.8f, 25, 0.8f),
    DISTRICT(1.0f, 35, 1.0f),
    METRO(1.15f, 55, 1.4f)
}
