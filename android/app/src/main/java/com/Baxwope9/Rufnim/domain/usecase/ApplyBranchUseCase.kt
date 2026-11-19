package com.Baxwope9.Rufnim.domain.usecase

import com.Baxwope9.Rufnim.domain.model.IncidentCard
import com.Baxwope9.Rufnim.domain.model.ResourceType
import com.Baxwope9.Rufnim.domain.model.UpgradeId
import com.Baxwope9.Rufnim.domain.model.ZoneId

data class BranchEffect(
    val accepted: Boolean,
    val satisfactionDelta: Int,
    val buzzDelta: Float,
    val spentResource: ResourceType?,
    val spentAmount: Int,
    val boostedZone: ZoneId?,
    val demandBoost: Float
)

class ApplyBranchUseCase {

    fun choose(
        card: IncidentCard,
        branchIndex: Int,
        reserve: Map<ResourceType, Int>,
        owned: Set<UpgradeId>
    ): BranchEffect {
        val branch = card.branches.getOrNull(branchIndex)
            ?: return BranchEffect(false, 0, 0f, null, 0, null, 0f)
        val discount = if (owned.contains(UpgradeId.CABLE_AUDIT) && branch.costResource == ResourceType.POWER) 1 else 0
        val cost = (branch.cost - discount).coerceAtLeast(0)
        val resource = branch.costResource
        if (resource != null && (reserve[resource] ?: 0) < cost) {
            return BranchEffect(false, 0, 0f, null, 0, null, 0f)
        }
        return BranchEffect(
            accepted = true,
            satisfactionDelta = branch.satisfactionDelta,
            buzzDelta = branch.buzzDelta,
            spentResource = if (cost > 0) resource else null,
            spentAmount = cost,
            boostedZone = if (branch.demandBoost > 0f) card.zone else null,
            demandBoost = branch.demandBoost
        )
    }

    fun affordable(
        card: IncidentCard,
        branchIndex: Int,
        reserve: Map<ResourceType, Int>,
        owned: Set<UpgradeId>
    ): Boolean {
        val branch = card.branches.getOrNull(branchIndex) ?: return false
        val resource = branch.costResource ?: return true
        val discount = if (owned.contains(UpgradeId.CABLE_AUDIT) && resource == ResourceType.POWER) 1 else 0
        val cost = (branch.cost - discount).coerceAtLeast(0)
        return (reserve[resource] ?: 0) >= cost
    }

    fun displayCost(card: IncidentCard, branchIndex: Int, owned: Set<UpgradeId>): Int {
        val branch = card.branches.getOrNull(branchIndex) ?: return 0
        val resource = branch.costResource ?: return 0
        val discount = if (owned.contains(UpgradeId.CABLE_AUDIT) && resource == ResourceType.POWER) 1 else 0
        return (branch.cost - discount).coerceAtLeast(0)
    }
}
