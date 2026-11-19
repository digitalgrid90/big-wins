package com.Baxwope9.Rufnim.presentation.common

import kotlin.math.abs
import kotlin.math.roundToInt

object Format {

    fun score(value: Int): String {
        val digits = abs(value).toString()
        val grouped = StringBuilder()
        digits.reversed().forEachIndexed { index, char ->
            if (index > 0 && index % 3 == 0) grouped.append(' ')
            grouped.append(char)
        }
        val body = grouped.reverse().toString()
        return if (value < 0) "-$body" else body
    }

    fun percent(fraction: Float): String = "${(fraction.coerceIn(0f, 1f) * 100).roundToInt()}%"

    fun percentValue(value: Int): String = "$value%"

    fun buzz(value: Float): String = "×${(value * 10).roundToInt() / 10f}"

    fun delta(value: Int): String = if (value >= 0) "+$value" else value.toString()

    fun ratio(current: Int, total: Int): String = "$current/$total"
}
