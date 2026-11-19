package com.Baxwope9.Rufnim.presentation.common

import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.domain.model.FestivalScale
import com.Baxwope9.Rufnim.domain.model.Grade
import com.Baxwope9.Rufnim.domain.model.IncidentId
import com.Baxwope9.Rufnim.domain.model.ResourceType
import com.Baxwope9.Rufnim.domain.model.UpgradeBranch
import com.Baxwope9.Rufnim.domain.model.UpgradeId
import com.Baxwope9.Rufnim.domain.model.Weather
import com.Baxwope9.Rufnim.domain.model.ZoneId

object UiLabels {

    fun zoneName(zone: ZoneId): Int = when (zone) {
        ZoneId.MAIN_STAGE -> R.string.zone_main_stage
        ZoneId.FOOD_COURT -> R.string.zone_food_court
        ZoneId.TRANSIT_GATE -> R.string.zone_transit_gate
        ZoneId.PLAZA_MARKET -> R.string.zone_plaza_market
    }

    fun zoneSprite(zone: ZoneId): Int = when (zone) {
        ZoneId.MAIN_STAGE -> R.drawable.sprite_4
        ZoneId.FOOD_COURT -> R.drawable.sprite_5
        ZoneId.TRANSIT_GATE -> R.drawable.sprite_3
        ZoneId.PLAZA_MARKET -> R.drawable.sprite_2
    }

    fun resourceLabel(resource: ResourceType): Int = when (resource) {
        ResourceType.VOLUNTEERS -> R.string.res_volunteers
        ResourceType.POWER -> R.string.res_power
        ResourceType.TRANSPORT -> R.string.res_transport
        ResourceType.STAGE -> R.string.res_stage
    }

    fun weatherLabel(weather: Weather): Int = when (weather) {
        Weather.CLEAR -> R.string.weather_clear
        Weather.DRIZZLE -> R.string.weather_drizzle
        Weather.HEAT -> R.string.weather_heat
        Weather.STORM -> R.string.weather_storm
    }

    fun blockLabel(index: Int): Int = when (index) {
        0 -> R.string.block_1
        1 -> R.string.block_2
        2 -> R.string.block_3
        3 -> R.string.block_4
        4 -> R.string.block_5
        else -> R.string.block_6
    }

    fun scaleLabel(scale: FestivalScale): Int = when (scale) {
        FestivalScale.CITY_BLOCK -> R.string.scale_city_block
        FestivalScale.DISTRICT -> R.string.scale_district
        FestivalScale.METRO -> R.string.scale_metro
    }

    fun gradeLabel(grade: Grade): Int = when (grade) {
        Grade.A -> R.string.grade_a
        Grade.B -> R.string.grade_b
        Grade.C -> R.string.grade_c
        Grade.D -> R.string.grade_d
    }

    fun incidentTitle(id: IncidentId): Int = when (id) {
        IncidentId.POWER_DIP -> R.string.incident_power_dip_title
        IncidentId.GATE_CRUSH -> R.string.incident_gate_crush_title
        IncidentId.RAIN_FRONT -> R.string.incident_rain_front_title
        IncidentId.STAGE_DELAY -> R.string.incident_stage_delay_title
        IncidentId.VENDOR_RUSH -> R.string.incident_vendor_rush_title
        IncidentId.CROWD_SURGE -> R.string.incident_crowd_surge_title
    }

    fun incidentBody(id: IncidentId): Int = when (id) {
        IncidentId.POWER_DIP -> R.string.incident_power_dip_body
        IncidentId.GATE_CRUSH -> R.string.incident_gate_crush_body
        IncidentId.RAIN_FRONT -> R.string.incident_rain_front_body
        IncidentId.STAGE_DELAY -> R.string.incident_stage_delay_body
        IncidentId.VENDOR_RUSH -> R.string.incident_vendor_rush_body
        IncidentId.CROWD_SURGE -> R.string.incident_crowd_surge_body
    }

    fun upgradeName(id: UpgradeId): Int = when (id) {
        UpgradeId.SHUTTLE_LOOP -> R.string.upgrade_shuttle_loop
        UpgradeId.NIGHT_ROUTES -> R.string.upgrade_night_routes
        UpgradeId.PARK_AND_RIDE -> R.string.upgrade_park_and_ride
        UpgradeId.SECOND_GENERATOR -> R.string.upgrade_second_generator
        UpgradeId.CABLE_AUDIT -> R.string.upgrade_cable_audit
        UpgradeId.LAMP_GRID -> R.string.upgrade_lamp_grid
        UpgradeId.VOLUNTEER_HQ -> R.string.upgrade_volunteer_hq
        UpgradeId.SHIFT_ROTATION -> R.string.upgrade_shift_rotation
        UpgradeId.STAGE_MANAGERS -> R.string.upgrade_stage_managers
    }

    fun upgradeEffect(id: UpgradeId): Int = when (id) {
        UpgradeId.SHUTTLE_LOOP -> R.string.upgrade_shuttle_loop_effect
        UpgradeId.NIGHT_ROUTES -> R.string.upgrade_night_routes_effect
        UpgradeId.PARK_AND_RIDE -> R.string.upgrade_park_and_ride_effect
        UpgradeId.SECOND_GENERATOR -> R.string.upgrade_second_generator_effect
        UpgradeId.CABLE_AUDIT -> R.string.upgrade_cable_audit_effect
        UpgradeId.LAMP_GRID -> R.string.upgrade_lamp_grid_effect
        UpgradeId.VOLUNTEER_HQ -> R.string.upgrade_volunteer_hq_effect
        UpgradeId.SHIFT_ROTATION -> R.string.upgrade_shift_rotation_effect
        UpgradeId.STAGE_MANAGERS -> R.string.upgrade_stage_managers_effect
    }

    fun branchName(branch: UpgradeBranch): Int = when (branch) {
        UpgradeBranch.LOGISTICS -> R.string.branch_logistics
        UpgradeBranch.POWER_GRID -> R.string.branch_power
        UpgradeBranch.CREW -> R.string.branch_crew
    }

    fun branchSprite(branch: UpgradeBranch): Int = when (branch) {
        UpgradeBranch.LOGISTICS -> R.drawable.sprite_3
        UpgradeBranch.POWER_GRID -> R.drawable.sprite_2
        UpgradeBranch.CREW -> R.drawable.sprite_1
    }
}
