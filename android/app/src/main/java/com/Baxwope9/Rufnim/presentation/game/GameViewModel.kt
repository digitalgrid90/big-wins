package com.Baxwope9.Rufnim.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Baxwope9.Rufnim.core.config.GameConfig
import com.Baxwope9.Rufnim.domain.model.BlockPlan
import com.Baxwope9.Rufnim.domain.model.FestivalOutcome
import com.Baxwope9.Rufnim.domain.model.FestivalResult
import com.Baxwope9.Rufnim.domain.model.FestivalScale
import com.Baxwope9.Rufnim.domain.model.IncidentCard
import com.Baxwope9.Rufnim.domain.model.ResourceType
import com.Baxwope9.Rufnim.domain.model.UpgradeId
import com.Baxwope9.Rufnim.domain.model.Weather
import com.Baxwope9.Rufnim.domain.model.ZoneId
import com.Baxwope9.Rufnim.domain.repository.FestivalRepository
import com.Baxwope9.Rufnim.domain.repository.UpgradeRepository
import com.Baxwope9.Rufnim.domain.usecase.ApplyBranchUseCase
import com.Baxwope9.Rufnim.domain.usecase.BlockPoolsUseCase
import com.Baxwope9.Rufnim.domain.usecase.DrawIncidentUseCase
import com.Baxwope9.Rufnim.domain.usecase.GenerateBlockUseCase
import com.Baxwope9.Rufnim.domain.usecase.ResolveBlockUseCase
import com.Baxwope9.Rufnim.domain.usecase.ScoreFestivalUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

class GameViewModel(
    private val festivalRepository: FestivalRepository,
    private val upgradeRepository: UpgradeRepository,
    private val blockPools: BlockPoolsUseCase,
    private val generateBlock: GenerateBlockUseCase,
    private val resolveBlock: ResolveBlockUseCase,
    private val drawIncident: DrawIncidentUseCase,
    private val applyBranch: ApplyBranchUseCase,
    private val scoreFestival: ScoreFestivalUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _results = Channel<FestivalResult>(Channel.BUFFERED)
    val results: Flow<FestivalResult> = _results.receiveAsFlow()

    private var runId = -1
    private var scale = FestivalScale.DISTRICT
    private var plan: BlockPlan = BlockPlan(0, 0, Weather.CLEAR, Weather.CLEAR, emptyMap(), emptyList())
    private var resolveJob: Job? = null
    private var demandBoosts = mutableMapOf<ZoneId, Float>()
    private var volunteerStreak = mutableMapOf<ZoneId, Int>()
    private var drawnCards = mutableSetOf<IncidentCard>()
    private var coverageHistory = mutableListOf<Float>()
    private var blocksDelivered = 0
    private var incidentsResolved = 0
    private var peakBuzz = 1f

    fun start(requestedRunId: Int) {
        if (runId == requestedRunId) return
        runId = requestedRunId
        resolveJob?.cancel()
        upgradeRepository.resetCampaign()
        scale = festivalRepository.scale()
        demandBoosts = mutableMapOf()
        volunteerStreak = mutableMapOf()
        drawnCards = mutableSetOf()
        coverageHistory = mutableListOf()
        blocksDelivered = 0
        incidentsResolved = 0
        peakBuzz = 1f
        _state.value = GameUiState()
        openBlock(0, Weather.CLEAR)
    }

    fun pause() {
        resolveJob?.cancel()
        resolveJob = null
    }

    fun showPause() {
        _state.update { it.copy(paused = true) }
    }

    fun hidePause() {
        _state.update { it.copy(paused = false) }
    }

    fun selectZone(zone: ZoneId) {
        if (!_state.value.interactive) return
        _state.update { it.copy(selectedZone = zone) }
    }

    fun increase(resource: ResourceType) {
        val current = _state.value
        if (!current.interactive) return
        val zone = current.selectedZone ?: return
        val available = current.pools[resource] ?: 0
        if (available <= 0) return
        val target = current.zones.firstOrNull { it.zone == zone } ?: return
        val need = target.demand[resource] ?: 0
        val assigned = target.assigned[resource] ?: 0
        if (need <= 0 || assigned >= need) return
        _state.update { state ->
            state.copy(
                pools = state.pools.toMutableMap().apply { this[resource] = available - 1 },
                zones = state.zones.map { zoneUi ->
                    if (zoneUi.zone != zone) zoneUi else {
                        val updated = zoneUi.assigned.toMutableMap()
                            .apply { this[resource] = assigned + 1 }
                        zoneUi.copy(assigned = updated, coverage = previewCoverage(zoneUi.demand, updated))
                    }
                }
            )
        }
    }

    fun decrease(resource: ResourceType) {
        val current = _state.value
        if (!current.interactive) return
        val zone = current.selectedZone ?: return
        val target = current.zones.firstOrNull { it.zone == zone } ?: return
        val assigned = target.assigned[resource] ?: 0
        if (assigned <= 0) return
        _state.update { state ->
            state.copy(
                pools = state.pools.toMutableMap()
                    .apply { this[resource] = (state.pools[resource] ?: 0) + 1 },
                zones = state.zones.map { zoneUi ->
                    if (zoneUi.zone != zone) zoneUi else {
                        val updated = zoneUi.assigned.toMutableMap()
                            .apply { this[resource] = assigned - 1 }
                        zoneUi.copy(assigned = updated, coverage = previewCoverage(zoneUi.demand, updated))
                    }
                }
            )
        }
    }

    fun holdInReserve(resource: ResourceType) {
        val current = _state.value
        if (!current.interactive) return
        val available = current.pools[resource] ?: 0
        if (available <= 0) return
        _state.update { state ->
            state.copy(
                pools = state.pools.toMutableMap().apply { this[resource] = available - 1 },
                reserve = state.reserve.toMutableMap()
                    .apply { this[resource] = (state.reserve[resource] ?: 0) + 1 }
            )
        }
    }

    fun holdLargest() {
        val current = _state.value
        val best = current.pools.maxByOrNull { it.value } ?: return
        if (best.value <= 0) return
        holdInReserve(best.key)
    }

    fun commitBlock() {
        val current = _state.value
        if (!current.interactive) return
        resolveJob?.cancel()
        _state.update { it.copy(phase = GamePhase.RESOLVING, lastDelta = null) }
        resolveJob = viewModelScope.launch {
            val assignments = current.zones.associate { it.zone to it.assigned }
            val fatigued = volunteerStreak.filterValues { it >= fatigueThreshold() }.keys
            val outcome = resolveBlock.resolve(
                plan = plan,
                assignments = assignments,
                fatiguedZones = fatigued,
                buzz = current.buzz,
                lampGrid = UpgradeId.LAMP_GRID in upgradeRepository.owned()
            )
            delay(GameConfig.RESOLVE_DURATION_MS)
            val satisfaction = (current.satisfaction + outcome.satisfactionDelta)
                .coerceIn(0, GameConfig.MAX_SATISFACTION)
            peakBuzz = max(peakBuzz, outcome.buzz)
            val earned = outcome.permitsEarned
            upgradeRepository.setPermits(upgradeRepository.permits() + earned)
            coverageHistory.add(outcome.zones.map { it.coverage }.average().toFloat())
            outcome.zones.forEach { zoneOutcome ->
                val gaveVolunteers = (assignments[zoneOutcome.zone]?.get(ResourceType.VOLUNTEERS) ?: 0) > 0
                volunteerStreak[zoneOutcome.zone] =
                    if (gaveVolunteers) (volunteerStreak[zoneOutcome.zone] ?: 0) + 1 else 0
            }
            demandBoosts = mutableMapOf()
            val leftover = current.pools
            _state.update { state ->
                state.copy(
                    phase = GamePhase.RESOLVING,
                    satisfaction = satisfaction,
                    buzz = outcome.buzz,
                    permits = upgradeRepository.permits(),
                    permitsEarned = earned,
                    lastDelta = outcome.satisfactionDelta,
                    perfectBlock = outcome.perfectBlock,
                    pools = emptyMapOfResources(),
                    reserve = mergeReserve(state.reserve, leftover),
                    zones = state.zones.map { zoneUi ->
                        val resolved = outcome.zones.firstOrNull { it.zone == zoneUi.zone }
                        zoneUi.copy(
                            coverage = resolved?.coverage ?: zoneUi.coverage,
                            fatigued = (volunteerStreak[zoneUi.zone] ?: 0) >= fatigueThreshold()
                        )
                    }
                )
            }
            delay(450)
            if (satisfaction <= 0) {
                finish(FestivalOutcome.EVACUATED)
                return@launch
            }
            blocksDelivered++
            val card = drawIncident.draw(
                seed = plan.seed,
                chancePercent = scale.incidentChance,
                alreadyDrawn = drawnCards
            )
            if (card != null) {
                drawnCards.add(card)
                _state.update { it.copy(phase = GamePhase.INCIDENT, incident = toIncidentUi(card, it.reserve)) }
            } else if (plan.index >= GameConfig.BLOCKS - 1) {
                finish(FestivalOutcome.COMPLETED)
            } else {
                _state.update { it.copy(phase = GamePhase.INTERMISSION) }
            }
        }
    }

    fun chooseBranch(index: Int) {
        val current = _state.value
        val incident = current.incident ?: return
        val owned = upgradeRepository.owned()
        val effect = applyBranch.choose(incident.card, index, current.reserve, owned)
        if (!effect.accepted) return
        incidentsResolved++
        val satisfaction = (current.satisfaction + effect.satisfactionDelta)
            .coerceIn(0, GameConfig.MAX_SATISFACTION)
        val buzz = (current.buzz + effect.buzzDelta).coerceIn(1f, GameConfig.BUZZ_CAP)
        peakBuzz = max(peakBuzz, buzz)
        if (effect.boostedZone != null) {
            demandBoosts[effect.boostedZone] = effect.demandBoost
        }
        val reserve = current.reserve.toMutableMap()
        if (effect.spentResource != null) {
            reserve[effect.spentResource] =
                (reserve[effect.spentResource] ?: 0) - effect.spentAmount
        }
        _state.update {
            it.copy(
                satisfaction = satisfaction,
                buzz = buzz,
                reserve = reserve,
                incident = null,
                lastDelta = effect.satisfactionDelta
            )
        }
        if (satisfaction <= 0) {
            finish(FestivalOutcome.EVACUATED)
        } else if (plan.index >= GameConfig.BLOCKS - 1) {
            finish(FestivalOutcome.COMPLETED)
        } else {
            _state.update { it.copy(phase = GamePhase.INTERMISSION) }
        }
    }

    fun nextBlock() {
        if (_state.value.phase != GamePhase.INTERMISSION) return
        openBlock(plan.index + 1, plan.nextWeather)
    }

    fun syncPermits() {
        _state.update { it.copy(permits = upgradeRepository.permits()) }
    }

    private fun openBlock(index: Int, weather: Weather) {
        val owned = upgradeRepository.owned()
        val pools = blockPools.pools(scale, owned)
        plan = generateBlock.generate(index, pools, weather, demandBoosts)
        _state.update { state ->
            state.copy(
                phase = GamePhase.PLANNING,
                blockIndex = index,
                pools = plan.pools,
                weather = plan.weather,
                nextWeather = plan.nextWeather,
                permits = upgradeRepository.permits(),
                permitsEarned = 0,
                incident = null,
                lastDelta = null,
                perfectBlock = false,
                selectedZone = plan.zones.firstOrNull()?.zone,
                zones = plan.zones.map { zoneDemand ->
                    ZoneUi(
                        zone = zoneDemand.zone,
                        demand = zoneDemand.demand,
                        assigned = emptyMap(),
                        coverage = 0f,
                        fatigued = (volunteerStreak[zoneDemand.zone] ?: 0) >= fatigueThreshold()
                    )
                }
            )
        }
    }

    private fun finish(outcome: FestivalOutcome) {
        val current = _state.value
        val efficiency = if (coverageHistory.isEmpty()) 0 else {
            (coverageHistory.average() * 100).roundToInt()
        }
        val result = scoreFestival.score(
            outcome = outcome,
            satisfaction = current.satisfaction,
            blocksDelivered = blocksDelivered,
            efficiency = efficiency,
            incidentsResolved = incidentsResolved,
            peakBuzz = peakBuzz,
            permitsUnspent = upgradeRepository.permits(),
            zoneCoverage = current.zones.map { it.zone to (it.coverage * 100).roundToInt() },
            scale = scale
        )
        val newBest = festivalRepository.recordFestival(result)
        _state.update { it.copy(phase = GamePhase.FINISHED, paused = false) }
        viewModelScope.launch { _results.send(result.copy(newBest = newBest)) }
    }

    private fun toIncidentUi(card: IncidentCard, reserve: Map<ResourceType, Int>): IncidentUi {
        val owned = upgradeRepository.owned()
        return IncidentUi(
            card = card,
            options = card.branches.indices.map { index ->
                IncidentOptionUi(
                    index = index,
                    resource = card.branches[index].costResource,
                    cost = applyBranch.displayCost(card, index, owned),
                    affordable = applyBranch.affordable(card, index, reserve, owned)
                )
            }
        )
    }

    private fun fatigueThreshold(): Int =
        if (UpgradeId.SHIFT_ROTATION in upgradeRepository.owned()) 3 else 2

    private fun previewCoverage(
        demand: Map<ResourceType, Int>,
        assigned: Map<ResourceType, Int>
    ): Float {
        val demanded = demand.filterValues { it > 0 }
        if (demanded.isEmpty()) return 1f
        return demanded.map { (type, need) ->
            minOf(1f, (assigned[type] ?: 0).toFloat() / need)
        }.average().toFloat()
    }

    private fun emptyMapOfResources(): Map<ResourceType, Int> =
        ResourceType.entries.associateWith { 0 }

    private fun mergeReserve(
        reserve: Map<ResourceType, Int>,
        leftover: Map<ResourceType, Int>
    ): Map<ResourceType, Int> =
        ResourceType.entries.associateWith { (reserve[it] ?: 0) + (leftover[it] ?: 0) }
}
