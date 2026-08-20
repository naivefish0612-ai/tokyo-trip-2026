package com.tokyo2026.trip

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.runtime.mutableStateOf
import androidx.test.core.app.ApplicationProvider
import com.tokyo2026.trip.data.Trip
import com.tokyo2026.trip.store.TripStore
import com.tokyo2026.trip.ui.ChecklistScreen
import com.tokyo2026.trip.ui.DayScreen
import com.tokyo2026.trip.ui.HomeScreen
import com.tokyo2026.trip.ui.SpotScreen
import com.tokyo2026.trip.ui.SpotsScreen
import com.tokyo2026.trip.ui.Tokyo2026Theme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], qualifiers = "w411dp-h891dp")
class AppRenderTest {

    @get:Rule
    val rule = createComposeRule()

    private fun store() = TripStore(ApplicationProvider.getApplicationContext())

    @Test
    fun tripDataIsConsistent() {
        assertEquals(8, Trip.days.size)
        val ids = Trip.allSpots.map { it.id }
        assertEquals("景點 id 必須唯一", ids.size, ids.toSet().size)
        val checkIds = Trip.checklist.map { it.id }
        assertEquals("清單 id 必須唯一", checkIds.size, checkIds.toSet().size)
        assertTrue("id 不可與清單衝突", ids.toSet().intersect(checkIds.toSet()).isEmpty())
        Trip.allSpots.forEach {
            assertTrue("${it.id} 缺少座標", it.lat > 20 && it.lng > 120)
            assertTrue("${it.id} 缺少營業時間", it.hours.isNotBlank())
            assertTrue("${it.id} 缺少在地筆記", it.notes.isNotEmpty())
        }
    }

    @Test
    fun photosArePackagedForMostSpots() {
        val ctx = ApplicationProvider.getApplicationContext<android.content.Context>()
        val files = ctx.assets.list("photos")!!.toSet()
        val withPhoto = Trip.allSpots.count { it.photo != null }
        assertTrue("實景照片應覆蓋多數景點，實際 $withPhoto/${Trip.allSpots.size}", withPhoto * 2 > Trip.allSpots.size)
        Trip.allSpots.mapNotNull { it.photo }.forEach {
            assertTrue("assets 缺少 $it", files.contains(it.removePrefix("photos/")))
        }
    }

    @Test
    fun everySpotHasOfficialLinkOrReason() {
        val missing = Trip.allSpots.filter { it.official == null }.map { it.id }
        assertTrue("僅路口／銅像等無官網景點可缺連結，實際: $missing", missing.size <= 3)
    }

    @Test
    fun spotsScreenSearchFiltersList() {
        val st = store()
        rule.setContent { Tokyo2026Theme { SpotsScreen(st) {} } }
        rule.onNodeWithText("搜尋景點、日文名、地區").performTextInput("渋谷スカイ")
        rule.waitForIdle()
        rule.onNodeWithText("1 個景點").assertExists()
        rule.onAllNodesWithText("澀谷 SKY").onFirst().assertExists()
    }

    @Test
    fun homeScreenRenders() {
        rule.setContent { Tokyo2026Theme { HomeScreen(store(), onDay = {}, onSpot = {}, onChecklist = {}) } }
        rule.onNodeWithText(Trip.title).assertExists()
        rule.onNodeWithText("每日行程").assertExists()
    }

    @Test
    fun everyDayScreenRenders() {
        val idx = mutableStateOf(0)
        val st = store()
        rule.setContent { Tokyo2026Theme { DayScreen(Trip.days[idx.value], st, onBack = {}, onSpot = {}) } }
        Trip.days.indices.forEach { i ->
            idx.value = i
            rule.waitForIdle()
            rule.onNodeWithText("行程動線").assertExists()
            rule.onNodeWithText("今晚住宿").assertExists()
        }
    }

    @Test
    fun everySpotScreenRenders() {
        val idx = mutableStateOf(0)
        val st = store()
        rule.setContent { Tokyo2026Theme { SpotScreen(Trip.allSpots[idx.value], st, onBack = {}) } }
        Trip.allSpots.indices.forEach { i ->
            idx.value = i
            rule.waitForIdle()
            rule.onAllNodesWithText(Trip.allSpots[i].kana).onFirst().assertExists()
            rule.onNodeWithText("重點").assertExists()
        }
    }

    @Test
    fun checkinPersistsAcrossStores() {
        val s = store()
        val id = Trip.allSpots.first().id
        val before = s.isChecked(id)
        s.toggle(id)
        assertEquals(!before, TripStore(ApplicationProvider.getApplicationContext()).isChecked(id))
        s.toggle(id)
    }

    @Test
    fun checklistScreenTogglesItem() {
        val s = store()
        rule.setContent { Tokyo2026Theme { ChecklistScreen(s, onBack = {}) } }
        val first = Trip.checklist.first()
        rule.onNodeWithText(first.text).performClick()
        rule.waitForIdle()
        assertTrue(s.isChecked(first.id))
    }
}
