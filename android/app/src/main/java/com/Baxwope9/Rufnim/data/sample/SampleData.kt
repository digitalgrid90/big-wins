package com.Baxwope9.Rufnim.data.sample

import com.Baxwope9.Rufnim.domain.model.BlockPlan
import com.Baxwope9.Rufnim.domain.model.IncidentBranch
import com.Baxwope9.Rufnim.domain.model.IncidentCard
import com.Baxwope9.Rufnim.domain.model.IncidentId
import com.Baxwope9.Rufnim.domain.model.IncidentTone
import com.Baxwope9.Rufnim.domain.model.ResourceType
import com.Baxwope9.Rufnim.domain.model.UpgradeBranch
import com.Baxwope9.Rufnim.domain.model.UpgradeId
import com.Baxwope9.Rufnim.domain.model.UpgradeNode
import com.Baxwope9.Rufnim.domain.model.Weather
import com.Baxwope9.Rufnim.domain.model.ZoneDemand
import com.Baxwope9.Rufnim.domain.model.ZoneId

object SampleData {

    val ZONE_ROSTER: List<ZoneId> = listOf(
        ZoneId.MAIN_STAGE,
        ZoneId.FOOD_COURT,
        ZoneId.TRANSIT_GATE,
        ZoneId.PLAZA_MARKET
    )

    val UPGRADE_TREE: List<UpgradeNode> = listOf(
        UpgradeNode(UpgradeId.SHUTTLE_LOOP, UpgradeBranch.LOGISTICS, 1, 3, null),
        UpgradeNode(UpgradeId.NIGHT_ROUTES, UpgradeBranch.LOGISTICS, 2, 5, UpgradeId.SHUTTLE_LOOP),
        UpgradeNode(UpgradeId.PARK_AND_RIDE, UpgradeBranch.LOGISTICS, 3, 8, UpgradeId.NIGHT_ROUTES),
        UpgradeNode(UpgradeId.SECOND_GENERATOR, UpgradeBranch.POWER_GRID, 1, 3, null),
        UpgradeNode(UpgradeId.CABLE_AUDIT, UpgradeBranch.POWER_GRID, 2, 5, UpgradeId.SECOND_GENERATOR),
        UpgradeNode(UpgradeId.LAMP_GRID, UpgradeBranch.POWER_GRID, 3, 8, UpgradeId.CABLE_AUDIT),
        UpgradeNode(UpgradeId.VOLUNTEER_HQ, UpgradeBranch.CREW, 1, 3, null),
        UpgradeNode(UpgradeId.SHIFT_ROTATION, UpgradeBranch.CREW, 2, 5, UpgradeId.VOLUNTEER_HQ),
        UpgradeNode(UpgradeId.STAGE_MANAGERS, UpgradeBranch.CREW, 3, 8, UpgradeId.SHIFT_ROTATION)
    )

    val INCIDENT_DECK: List<IncidentCard> = listOf(
        IncidentCard(
            id = IncidentId.POWER_DIP,
            tone = IncidentTone.HAZARD,
            zone = ZoneId.MAIN_STAGE,
            branches = listOf(
                IncidentBranch(ResourceType.POWER, 2, 6, 0.1f, 0f),
                IncidentBranch(ResourceType.VOLUNTEERS, 3, 3, 0f, 0f),
                IncidentBranch(null, 0, -8, -0.2f, 0.3f)
            )
        ),
        IncidentCard(
            id = IncidentId.GATE_CRUSH,
            tone = IncidentTone.HAZARD,
            zone = ZoneId.TRANSIT_GATE,
            branches = listOf(
                IncidentBranch(ResourceType.TRANSPORT, 3, 7, 0.1f, 0f),
                IncidentBranch(ResourceType.VOLUNTEERS, 2, 4, 0f, 0.1f),
                IncidentBranch(null, 0, -10, -0.2f, 0.3f)
            )
        ),
        IncidentCard(
            id = IncidentId.RAIN_FRONT,
            tone = IncidentTone.HAZARD,
            zone = ZoneId.PLAZA_MARKET,
            branches = listOf(
                IncidentBranch(ResourceType.VOLUNTEERS, 3, 6, 0f, 0f),
                IncidentBranch(ResourceType.STAGE, 1, 3, 0.1f, 0.1f),
                IncidentBranch(null, 0, -7, -0.2f, 0.3f)
            )
        ),
        IncidentCard(
            id = IncidentId.STAGE_DELAY,
            tone = IncidentTone.HAZARD,
            zone = ZoneId.MAIN_STAGE,
            branches = listOf(
                IncidentBranch(ResourceType.STAGE, 2, 8, 0.2f, 0f),
                IncidentBranch(ResourceType.VOLUNTEERS, 4, 4, 0f, 0f),
                IncidentBranch(null, 0, -9, -0.2f, 0.3f)
            )
        ),
        IncidentCard(
            id = IncidentId.VENDOR_RUSH,
            tone = IncidentTone.OPPORTUNITY,
            zone = ZoneId.FOOD_COURT,
            branches = listOf(
                IncidentBranch(ResourceType.VOLUNTEERS, 2, 9, 0.2f, 0.15f),
                IncidentBranch(ResourceType.POWER, 2, 5, 0.1f, 0f),
                IncidentBranch(null, 0, 0, 0f, 0f)
            )
        ),
        IncidentCard(
            id = IncidentId.CROWD_SURGE,
            tone = IncidentTone.OPPORTUNITY,
            zone = ZoneId.PLAZA_MARKET,
            branches = listOf(
                IncidentBranch(ResourceType.VOLUNTEERS, 3, 10, 0.2f, 0.1f),
                IncidentBranch(ResourceType.TRANSPORT, 2, 6, 0.1f, 0f),
                IncidentBranch(null, 0, -4, 0f, 0.2f)
            )
        )
    )

    val FALLBACK_BLOCK: BlockPlan = BlockPlan(
        index = 0,
        seed = 0,
        weather = Weather.CLEAR,
        nextWeather = Weather.CLEAR,
        pools = mapOf(
            ResourceType.VOLUNTEERS to 12,
            ResourceType.POWER to 10,
            ResourceType.TRANSPORT to 8,
            ResourceType.STAGE to 6
        ),
        zones = listOf(
            ZoneDemand(
                ZoneId.MAIN_STAGE,
                mapOf(ResourceType.VOLUNTEERS to 3, ResourceType.POWER to 2, ResourceType.STAGE to 2)
            ),
            ZoneDemand(
                ZoneId.FOOD_COURT,
                mapOf(ResourceType.VOLUNTEERS to 3, ResourceType.POWER to 2)
            ),
            ZoneDemand(
                ZoneId.TRANSIT_GATE,
                mapOf(ResourceType.TRANSPORT to 3, ResourceType.VOLUNTEERS to 2)
            ),
            ZoneDemand(
                ZoneId.PLAZA_MARKET,
                mapOf(ResourceType.VOLUNTEERS to 2, ResourceType.POWER to 2, ResourceType.STAGE to 1)
            )
        )
    )
}
