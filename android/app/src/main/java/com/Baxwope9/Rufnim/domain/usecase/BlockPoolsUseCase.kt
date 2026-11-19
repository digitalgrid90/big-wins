package com.Baxwope9.Rufnim.domain.usecase

import com.Baxwope9.Rufnim.domain.model.FestivalScale
import com.Baxwope9.Rufnim.domain.model.ResourceType
import com.Baxwope9.Rufnim.domain.model.UpgradeId
import kotlin.math.floor

class BlockPoolsUseCase(private val basePools: Map<ResourceType, Int>) {

    fun pools(scale: FestivalScale, owned: Set<UpgradeId>): Map<ResourceType, Int> {
        return ResourceType.entries.associateWith { type ->
            val base = basePools[type] ?: 0
            val scaled = floor(base * scale.poolFactor).toInt()
            scaled + bonus(type, owned)
        }
    }

    private fun bonus(type: ResourceType, owned: Set<UpgradeId>): Int = when (type) {
        ResourceType.TRANSPORT -> if (UpgradeId.SHUTTLE_LOOP in owned) 2 else 0
        ResourceType.POWER -> if (UpgradeId.SECOND_GENERATOR in owned) 2 else 0
        ResourceType.VOLUNTEERS -> if (UpgradeId.VOLUNTEER_HQ in owned) 2 else 0
        ResourceType.STAGE -> if (UpgradeId.STAGE_MANAGERS in owned) 1 else 0
    }
}
