package com.Baxwope9.Rufnim.presentation.game

import com.Baxwope9.Rufnim.core.config.GameConfig
import com.Baxwope9.Rufnim.domain.model.IncidentCard
import com.Baxwope9.Rufnim.domain.model.ResourceType
import com.Baxwope9.Rufnim.domain.model.Weather
import com.Baxwope9.Rufnim.domain.model.ZoneId

enum class GamePhase { PLANNING, RESOLVING, INCIDENT, INTERMISSION, FINISHED }

data class ZoneUi(
    val zone: ZoneId,
    val demand: Map<ResourceType, Int>,
    val assigned: Map<ResourceType, Int>,
    val coverage: Float,
    val fatigued: Boolean
)

data class IncidentOptionUi(
    val index: Int,
    val resource: ResourceType?,
    val cost: Int,
    val affordable: Boolean
)

data class IncidentUi(
    val card: IncidentCard,
    val options: List<IncidentOptionUi>
)

data class GameUiState(
    val phase: GamePhase = GamePhase.PLANNING,
    val blockIndex: Int = 0,
    val satisfaction: Int = GameConfig.START_SATISFACTION,
    val buzz: Float = 1f,
    val permits: Int = 0,
    val pools: Map<ResourceType, Int> = emptyMap(),
    val reserve: Map<ResourceType, Int> = emptyMap(),
    val zones: List<ZoneUi> = emptyList(),
    val selectedZone: ZoneId? = null,
    val weather: Weather = Weather.CLEAR,
    val nextWeather: Weather = Weather.CLEAR,
    val incident: IncidentUi? = null,
    val lastDelta: Int? = null,
    val perfectBlock: Boolean = false,
    val paused: Boolean = false,
    val permitsEarned: Int = 0
) {
    val totalReserve: Int get() = reserve.values.sum()
    val interactive: Boolean get() = phase == GamePhase.PLANNING && !paused
}
