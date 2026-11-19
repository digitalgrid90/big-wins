package com.Baxwope9.Rufnim.domain.model

enum class IncidentId {
    POWER_DIP,
    GATE_CRUSH,
    RAIN_FRONT,
    STAGE_DELAY,
    VENDOR_RUSH,
    CROWD_SURGE
}

enum class IncidentTone { HAZARD, OPPORTUNITY }

data class IncidentBranch(
    val costResource: ResourceType?,
    val cost: Int,
    val satisfactionDelta: Int,
    val buzzDelta: Float,
    val demandBoost: Float
)

data class IncidentCard(
    val id: IncidentId,
    val tone: IncidentTone,
    val zone: ZoneId,
    val branches: List<IncidentBranch>
)
