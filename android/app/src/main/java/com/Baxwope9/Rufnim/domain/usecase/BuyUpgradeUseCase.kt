package com.Baxwope9.Rufnim.domain.usecase

import com.Baxwope9.Rufnim.domain.model.UpgradeId
import com.Baxwope9.Rufnim.domain.model.UpgradeNode
import com.Baxwope9.Rufnim.domain.repository.UpgradeRepository

enum class PurchaseResult { BOUGHT, LOCKED, TOO_EXPENSIVE, ALREADY_OWNED }

class BuyUpgradeUseCase(
    private val tree: List<UpgradeNode>,
    private val repository: UpgradeRepository
) {

    fun buy(id: UpgradeId): PurchaseResult {
        val node = tree.firstOrNull { it.id == id } ?: return PurchaseResult.LOCKED
        val owned = repository.owned()
        if (node.id in owned) return PurchaseResult.ALREADY_OWNED
        val parent = node.parent
        if (parent != null && parent !in owned) return PurchaseResult.LOCKED
        val permits = repository.permits()
        if (permits < node.cost) return PurchaseResult.TOO_EXPENSIVE
        repository.setPermits(permits - node.cost)
        repository.own(node.id)
        return PurchaseResult.BOUGHT
    }
}
