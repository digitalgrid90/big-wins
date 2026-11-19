package com.Baxwope9.Rufnim.data.repository

import com.Baxwope9.Rufnim.data.local.FestivalPrefs
import com.Baxwope9.Rufnim.domain.model.UpgradeId
import com.Baxwope9.Rufnim.domain.repository.UpgradeRepository

class UpgradeRepositoryImpl(private val prefs: FestivalPrefs) : UpgradeRepository {

    override fun permits(): Int = prefs.readInt(FestivalPrefs.KEY_PERMITS, 0)

    override fun setPermits(value: Int) {
        prefs.writeInt(FestivalPrefs.KEY_PERMITS, value.coerceAtLeast(0))
    }

    override fun owned(): Set<UpgradeId> =
        prefs.readSet(FestivalPrefs.KEY_OWNED).mapNotNull { stored ->
            UpgradeId.entries.firstOrNull { it.name == stored }
        }.toSet()

    override fun own(id: UpgradeId) {
        prefs.writeSet(FestivalPrefs.KEY_OWNED, owned().map { it.name }.toSet() + id.name)
    }

    override fun resetCampaign() {
        prefs.writeSet(FestivalPrefs.KEY_OWNED, emptySet())
        prefs.writeInt(FestivalPrefs.KEY_PERMITS, 0)
    }
}
