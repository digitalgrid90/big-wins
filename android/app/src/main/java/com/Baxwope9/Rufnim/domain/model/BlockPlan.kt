package com.Baxwope9.Rufnim.domain.model

data class BlockPlan(
    val index: Int,
    val seed: Int,
    val weather: Weather,
    val nextWeather: Weather,
    val pools: Map<ResourceType, Int>,
    val zones: List<ZoneDemand>
)

data class ZoneOutcome(
    val zone: ZoneId,
    val coverage: Float,
    val delta: Int
)

data class BlockOutcome(
    val zones: List<ZoneOutcome>,
    val satisfactionDelta: Int,
    val perfectBlock: Boolean,
    val buzz: Float,
    val permitsEarned: Int
)
