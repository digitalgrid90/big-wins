package com.Baxwope9.Rufnim.domain.model

enum class ResourceType { VOLUNTEERS, POWER, TRANSPORT, STAGE }

enum class ZoneId { MAIN_STAGE, FOOD_COURT, TRANSIT_GATE, PLAZA_MARKET }

enum class Weather(val modifier: Float) {
    CLEAR(1.0f),
    DRIZZLE(0.9f),
    HEAT(0.9f),
    STORM(0.8f)
}

data class ZoneDemand(
    val zone: ZoneId,
    val demand: Map<ResourceType, Int>
) {
    val demanded: List<ResourceType> get() = ResourceType.entries.filter { (demand[it] ?: 0) > 0 }
}
