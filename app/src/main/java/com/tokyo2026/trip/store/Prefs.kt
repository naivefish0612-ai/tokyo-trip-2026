package com.tokyo2026.trip.store

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap

/** 本機儲存的打卡、收藏與行前準備狀態（無帳號、離線可用） */
class TripStore(context: Context) {
    private val prefs = context.getSharedPreferences("tokyo2026", Context.MODE_PRIVATE)
    val checked: SnapshotStateMap<String, Boolean> = mutableStateMapOf()

    init {
        prefs.all.forEach { (k, v) -> if (v is Boolean) checked[k] = v }
    }

    fun isChecked(id: String): Boolean = checked[id] == true

    fun toggle(id: String) {
        val next = !isChecked(id)
        checked[id] = next
        prefs.edit().putBoolean(id, next).apply()
    }

    fun countChecked(ids: List<String>): Int = ids.count { isChecked(it) }

    fun isFav(id: String): Boolean = isChecked("fav_$id")
    fun toggleFav(id: String) = toggle("fav_$id")
    fun favCount(ids: List<String>): Int = ids.count { isFav(it) }
}
