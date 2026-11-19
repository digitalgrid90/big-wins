package com.Baxwope9.Rufnim.core.di

import android.content.Context
import com.Baxwope9.Rufnim.core.config.GameConfig
import com.Baxwope9.Rufnim.data.local.FestivalPrefs
import com.Baxwope9.Rufnim.data.repository.FestivalRepositoryImpl
import com.Baxwope9.Rufnim.data.repository.UpgradeRepositoryImpl
import com.Baxwope9.Rufnim.data.sample.SampleData
import com.Baxwope9.Rufnim.domain.repository.FestivalRepository
import com.Baxwope9.Rufnim.domain.repository.UpgradeRepository
import com.Baxwope9.Rufnim.domain.usecase.ApplyBranchUseCase
import com.Baxwope9.Rufnim.domain.usecase.BlockPoolsUseCase
import com.Baxwope9.Rufnim.domain.usecase.BuyUpgradeUseCase
import com.Baxwope9.Rufnim.domain.usecase.DrawIncidentUseCase
import com.Baxwope9.Rufnim.domain.usecase.GenerateBlockUseCase
import com.Baxwope9.Rufnim.domain.usecase.ResolveBlockUseCase
import com.Baxwope9.Rufnim.domain.usecase.ScoreFestivalUseCase

object ServiceLocator {

    private var prefs: FestivalPrefs? = null

    lateinit var festivalRepository: FestivalRepository
        private set
    lateinit var upgradeRepository: UpgradeRepository
        private set
    lateinit var generateBlock: GenerateBlockUseCase
        private set
    lateinit var resolveBlock: ResolveBlockUseCase
        private set
    lateinit var drawIncident: DrawIncidentUseCase
        private set
    lateinit var applyBranch: ApplyBranchUseCase
        private set
    lateinit var buyUpgrade: BuyUpgradeUseCase
        private set
    lateinit var scoreFestival: ScoreFestivalUseCase
        private set
    lateinit var blockPools: BlockPoolsUseCase
        private set
    lateinit var viewModelFactory: AppViewModelFactory
        private set

    fun init(context: Context) {
        if (prefs != null) return
        val storage = FestivalPrefs(context.applicationContext)
        prefs = storage
        festivalRepository = FestivalRepositoryImpl(storage)
        upgradeRepository = UpgradeRepositoryImpl(storage)
        generateBlock = GenerateBlockUseCase(
            zoneRoster = SampleData.ZONE_ROSTER,
            fallback = SampleData.FALLBACK_BLOCK,
            maxAttempts = GameConfig.MAX_GENERATION_ATTEMPTS,
            zoneShare = GameConfig.ZONE_DEMAND_SHARE
        )
        resolveBlock = ResolveBlockUseCase(
            pivot = GameConfig.COVERAGE_PIVOT,
            swing = GameConfig.COVERAGE_SWING,
            buzzStep = GameConfig.BUZZ_STEP,
            buzzCap = GameConfig.BUZZ_CAP,
            buzzBreak = GameConfig.BUZZ_BREAK_COVERAGE,
            fatigueFactor = GameConfig.FATIGUE_FACTOR
        )
        drawIncident = DrawIncidentUseCase(SampleData.INCIDENT_DECK)
        applyBranch = ApplyBranchUseCase()
        buyUpgrade = BuyUpgradeUseCase(SampleData.UPGRADE_TREE, upgradeRepository)
        scoreFestival = ScoreFestivalUseCase(
            satisfactionWeight = GameConfig.SCORE_SATISFACTION,
            permitWeight = GameConfig.SCORE_PERMIT,
            buzzWeight = GameConfig.SCORE_BUZZ,
            incidentWeight = GameConfig.SCORE_INCIDENT,
            gradeA = GameConfig.GRADE_A,
            gradeB = GameConfig.GRADE_B,
            gradeC = GameConfig.GRADE_C
        )
        blockPools = BlockPoolsUseCase(GameConfig.BASE_POOLS)
        viewModelFactory = AppViewModelFactory()
    }
}
