package com.Baxwope9.Rufnim.domain.usecase

import com.Baxwope9.Rufnim.domain.model.IncidentCard
import kotlin.random.Random

class DrawIncidentUseCase(private val deck: List<IncidentCard>) {

    fun draw(seed: Int, chancePercent: Int, alreadyDrawn: Set<IncidentCard>): IncidentCard? {
        if (deck.isEmpty()) return null
        val random = Random(seed xor 31337)
        if (random.nextInt(100) >= chancePercent) return null
        val pool = deck.filterNot { it in alreadyDrawn }.ifEmpty { deck }
        val card = pool[random.nextInt(pool.size)]
        return card.copy(branches = card.branches.shuffled(random))
    }
}
