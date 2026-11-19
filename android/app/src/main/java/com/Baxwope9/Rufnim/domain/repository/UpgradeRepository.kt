package com.Baxwope9.Rufnim.domain.repository

import com.Baxwope9.Rufnim.domain.model.UpgradeId

interface UpgradeRepository {
    fun permits(): Int
    fun setPermits(value: Int)
    fun owned(): Set<UpgradeId>
    fun own(id: UpgradeId)
    fun resetCampaign()
}
