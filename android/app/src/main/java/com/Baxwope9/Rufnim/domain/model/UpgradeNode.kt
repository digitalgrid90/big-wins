package com.Baxwope9.Rufnim.domain.model

enum class UpgradeBranch { LOGISTICS, POWER_GRID, CREW }

enum class UpgradeId {
    SHUTTLE_LOOP,
    NIGHT_ROUTES,
    PARK_AND_RIDE,
    SECOND_GENERATOR,
    CABLE_AUDIT,
    LAMP_GRID,
    VOLUNTEER_HQ,
    SHIFT_ROTATION,
    STAGE_MANAGERS
}

data class UpgradeNode(
    val id: UpgradeId,
    val branch: UpgradeBranch,
    val tier: Int,
    val cost: Int,
    val parent: UpgradeId?
)
