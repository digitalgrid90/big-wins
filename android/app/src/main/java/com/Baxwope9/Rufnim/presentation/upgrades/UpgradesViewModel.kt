package com.Baxwope9.Rufnim.presentation.upgrades

import androidx.lifecycle.ViewModel
import com.Baxwope9.Rufnim.data.sample.SampleData
import com.Baxwope9.Rufnim.domain.model.UpgradeBranch
import com.Baxwope9.Rufnim.domain.model.UpgradeId
import com.Baxwope9.Rufnim.domain.repository.UpgradeRepository
import com.Baxwope9.Rufnim.domain.usecase.BuyUpgradeUseCase
import com.Baxwope9.Rufnim.domain.usecase.PurchaseResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class NodeState { OWNED, AVAILABLE, LOCKED }

data class UpgradeNodeUi(
    val id: UpgradeId,
    val branch: UpgradeBranch,
    val tier: Int,
    val cost: Int,
    val state: NodeState,
    val parent: UpgradeId?
)

data class UpgradesUiState(
    val permits: Int = 0,
    val nodes: List<UpgradeNodeUi> = emptyList(),
    val lastPurchase: UpgradeId? = null,
    val notice: PurchaseResult? = null
) {
    val anyAffordable: Boolean
        get() = nodes.any { it.state == NodeState.AVAILABLE && it.cost <= permits }
}

class UpgradesViewModel(
    private val upgradeRepository: UpgradeRepository,
    private val buyUpgrade: BuyUpgradeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UpgradesUiState())
    val state: StateFlow<UpgradesUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val owned = upgradeRepository.owned()
        _state.update {
            it.copy(
                permits = upgradeRepository.permits(),
                notice = null,
                nodes = SampleData.UPGRADE_TREE.map { node ->
                    UpgradeNodeUi(
                        id = node.id,
                        branch = node.branch,
                        tier = node.tier,
                        cost = node.cost,
                        parent = node.parent,
                        state = when {
                            node.id in owned -> NodeState.OWNED
                            node.parent == null || node.parent in owned -> NodeState.AVAILABLE
                            else -> NodeState.LOCKED
                        }
                    )
                }
            )
        }
    }

    fun buy(id: UpgradeId) {
        val outcome = buyUpgrade.buy(id)
        refresh()
        _state.update {
            it.copy(
                notice = if (outcome == PurchaseResult.BOUGHT) null else outcome,
                lastPurchase = if (outcome == PurchaseResult.BOUGHT) id else it.lastPurchase
            )
        }
    }
}
