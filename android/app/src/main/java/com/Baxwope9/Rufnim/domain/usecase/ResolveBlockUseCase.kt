package com.Baxwope9.Rufnim.domain.usecase

import com.Baxwope9.Rufnim.domain.model.BlockOutcome
import com.Baxwope9.Rufnim.domain.model.BlockPlan
import com.Baxwope9.Rufnim.domain.model.ResourceType
import com.Baxwope9.Rufnim.domain.model.ZoneId
import com.Baxwope9.Rufnim.domain.model.ZoneOutcome
import kotlin.math.min
import kotlin.math.roundToInt

class ResolveBlockUseCase(
    private val pivot: Float,
    private val swing: Float,
    private val buzzStep: Float,
    private val buzzCap: Float,
    private val buzzBreak: Float,
    private val fatigueFactor: Float
) {

    fun resolve(
        plan: BlockPlan,
        assignments: Map<ZoneId, Map<ResourceType, Int>>,
        fatiguedZones: Set<ZoneId>,
        buzz: Float,
        lampGrid: Boolean
    ): BlockOutcome {
        val zones = plan.zones.map { zoneDemand ->
            val assigned = assignments[zoneDemand.zone].orEmpty()
            val demanded = zoneDemand.demanded
            val coverage = if (demanded.isEmpty()) 1f else demanded.map { type ->
                val need = zoneDemand.demand[type] ?: 0
                val raw = (assigned[type] ?: 0).toFloat()
                val effective = if (type == ResourceType.VOLUNTEERS && zoneDemand.zone in fatiguedZones) {
                    raw * fatigueFactor
                } else {
                    raw
                }
                if (need <= 0) 1f else min(1f, effective / need)
            }.average().toFloat()
            ZoneOutcome(
                zone = zoneDemand.zone,
                coverage = coverage,
                delta = ((coverage - pivot) * swing).roundToInt()
            )
        }
        val perfect = zones.all { it.coverage >= 0.999f }
        val broken = zones.any { it.coverage < buzzBreak }
        val nextBuzz = when {
            broken -> 1f
            perfect -> min(buzzCap, buzz + buzzStep)
            else -> buzz
        }
        val bonus = if (perfect && lampGrid) 4 else 0
        val total = ((zones.sumOf { it.delta } + bonus) * plan.weather.modifier).roundToInt()
        return BlockOutcome(
            zones = zones,
            satisfactionDelta = total,
            perfectBlock = perfect,
            buzz = nextBuzz,
            permitsEarned = 1 + nextBuzz.toInt()
        )
    }
}
