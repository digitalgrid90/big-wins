package com.Baxwope9.Rufnim.domain.usecase

import com.Baxwope9.Rufnim.domain.model.BlockPlan
import com.Baxwope9.Rufnim.domain.model.ResourceType
import com.Baxwope9.Rufnim.domain.model.Weather
import com.Baxwope9.Rufnim.domain.model.ZoneDemand
import com.Baxwope9.Rufnim.domain.model.ZoneId
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.random.Random

class GenerateBlockUseCase(
    private val zoneRoster: List<ZoneId>,
    private val fallback: BlockPlan,
    private val maxAttempts: Int,
    private val zoneShare: Float
) {

    fun generate(
        blockIndex: Int,
        pools: Map<ResourceType, Int>,
        weather: Weather,
        demandBoosts: Map<ZoneId, Float>
    ): BlockPlan {
        var attempt = 1
        while (attempt <= maxAttempts) {
            val seed = blockIndex * 7919 xor (attempt * 104729)
            val random = Random(seed)
            val zones = zoneRoster.map { zone ->
                val count = 2 + random.nextInt(2)
                val picked = ResourceType.entries.shuffled(random).take(count)
                val boost = demandBoosts[zone] ?: 0f
                val demand = picked.associateWith { type ->
                    val base = 1 + random.nextInt(5)
                    max(1, (base * (1f + boost)).roundToInt())
                }
                ZoneDemand(zone, demand)
            }
            if (isSolvable(zones, pools, zoneShare)) {
                return BlockPlan(
                    index = blockIndex,
                    seed = seed,
                    weather = weather,
                    nextWeather = Weather.entries[random.nextInt(Weather.entries.size)],
                    pools = pools,
                    zones = zones
                )
            }
            attempt++
        }
        return fallback.copy(
            index = blockIndex,
            pools = pools,
            weather = weather,
            nextWeather = Weather.CLEAR
        )
    }

    private fun isSolvable(
        zones: List<ZoneDemand>,
        pools: Map<ResourceType, Int>,
        share: Float
    ): Boolean {
        ResourceType.entries.forEach { type ->
            val pool = pools[type] ?: 0
            val total = zones.sumOf { it.demand[type] ?: 0 }
            if (total > pool) return false
            val peak = zones.maxOf { it.demand[type] ?: 0 }
            if (peak > pool * share) return false
        }
        return zones.all { it.demand.isNotEmpty() }
    }
}
