package com.tokyo2026.trip

import com.tokyo2026.trip.data.Spot
import com.tokyo2026.trip.data.Trip
import com.tokyo2026.trip.data.photoCredits
import org.junit.Test
import java.io.File

/**
 * 把行程資料匯出成給靜態網站用的 JSON。
 *
 * 直接讀取 Kotlin 物件而非解析原始碼，網站資料因此保證與 App 一致。
 * 純 JVM，不需要 Robolectric。以 `./gradlew testDebugUnitTest --tests '*ExportWebData*'` 執行。
 */
class ExportWebData {

    private fun esc(s: String): String = buildString {
        for (c in s) when (c) {
            '"' -> append("\\\"")
            '\\' -> append("\\\\")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> if (c < ' ') append("\\u%04x".format(c.code)) else append(c)
        }
    }

    private fun str(s: String?): String = if (s == null) "null" else "\"${esc(s)}\""
    private fun arr(items: List<String>): String = items.joinToString(",", "[", "]") { str(it) }

    private fun spotJson(s: Spot): String = buildString {
        append("{")
        append("\"id\":${str(s.id)},")
        append("\"nameZh\":${str(s.nameZh)},")
        append("\"nameJa\":${str(s.nameJa)},")
        append("\"kana\":${str(s.kana)},")
        append("\"art\":${str(s.art.name)},")
        append("\"cat\":${str(s.cat.name)},")
        append("\"catLabel\":${str(s.cat.label)},")
        append("\"area\":${str(s.area)},")
        append("\"lat\":${s.lat},")
        append("\"lng\":${s.lng},")
        append("\"stay\":${str(s.stay)},")
        append("\"hours\":${str(s.hours)},")
        append("\"price\":${str(s.price)},")
        append("\"closed\":${str(s.closed)},")
        append("\"booking\":${str(s.booking)},")
        append("\"notes\":${arr(s.notes)},")
        append("\"eats\":${arr(s.eats)},")
        append("\"warns\":${arr(s.warns)},")
        append("\"official\":${str(s.official)},")
        append("\"photo\":${str(s.photo)},")
        append("\"mapUrl\":${str(s.mapUrl)},")
        append("\"navUrl\":${str(s.navUrl)}")
        append("}")
    }

    @Test
    fun exportJson() {
        val json = buildString {
            append("{")
            append("\"title\":${str(Trip.title)},")
            append("\"subtitle\":${str(Trip.subtitle)},")
            append("\"flightOut\":${str(Trip.flightOut)},")
            append("\"flightBack\":${str(Trip.flightBack)},")
            append("\"startIso\":${str(Trip.startIso)},")

            append("\"days\":[")
            append(Trip.days.joinToString(",") { d ->
                buildString {
                    append("{")
                    append("\"n\":${d.n},")
                    append("\"date\":${str(d.date)},")
                    append("\"weekday\":${str(d.weekday)},")
                    append("\"theme\":${str(d.theme)},")
                    append("\"summary\":${str(d.summary)},")
                    append("\"hotelName\":${str(d.hotelName)},")
                    append("\"hotelNote\":${str(d.hotelNote)},")
                    append("\"moveMinutes\":${d.moveMinutes},")
                    append("\"tips\":${arr(d.tips)},")
                    append("\"alerts\":${arr(d.alerts)},")
                    append("\"stops\":[")
                    append(d.stops.joinToString(",") { st ->
                        buildString {
                            append("{\"time\":${str(st.time)},\"spot\":${spotJson(st.spot)},\"leg\":")
                            val l = st.leg
                            if (l == null) append("null") else append(
                                "{\"mode\":${str(l.mode)},\"route\":${str(l.route)}," +
                                    "\"minutes\":${l.minutes},\"fare\":${str(l.fare)}}"
                            )
                            append("}")
                        }
                    })
                    append("]}")
                }
            })
            append("],")

            append("\"hotels\":[")
            append(Trip.hotels.joinToString(",") { "{\"when\":${str(it.first)},\"name\":${str(it.second)}}" })
            append("],")

            append("\"bookings\":[")
            append(Trip.bookings.joinToString(",") {
                "{\"name\":${str(it.first)},\"status\":${str(it.second)},\"note\":${str(it.third)}}"
            })
            append("],")

            append("\"checklist\":[")
            append(Trip.checklist.joinToString(",") {
                "{\"id\":${str(it.id)},\"group\":${str(it.group)},\"text\":${str(it.text)}}"
            })
            append("],")

            append("\"photoCredits\":{")
            append(photoCredits.entries.joinToString(",") { (k, c) ->
                "${str(k)}:{\"file\":${str(c.file)},\"author\":${str(c.author)}," +
                    "\"license\":${str(c.license)},\"page\":${str(c.page)}}"
            })
            append("}")
            append("}")
        }

        val out = File("../web-build/data.json")
        out.parentFile.mkdirs()
        out.writeText(json, Charsets.UTF_8)
        println("exported ${json.length} bytes -> ${out.absolutePath}")
        println("days=${Trip.days.size} spots=${Trip.allSpots.size} photos=${Trip.photoCount}")
    }
}
