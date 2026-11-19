package com.Baxwope9.Rufnim.data.local

import android.content.Context
import android.content.SharedPreferences

class FestivalPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("bigwins_festival", Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun writeInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun readString(key: String): String? = prefs.getString(key, null)

    fun writeString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun readSet(key: String): Set<String> = prefs.getStringSet(key, emptySet()).orEmpty()

    fun writeSet(key: String, value: Set<String>) {
        prefs.edit().putStringSet(key, value).apply()
    }

    companion object {
        const val KEY_BEST_SCORE = "best_score"
        const val KEY_BEST_GRADE = "best_grade"
        const val KEY_FESTIVALS = "festivals_played"
        const val KEY_SCALE = "festival_scale"
        const val KEY_PERMITS = "permits"
        const val KEY_OWNED = "owned_upgrades"
    }
}
