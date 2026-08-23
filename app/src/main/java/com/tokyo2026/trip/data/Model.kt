package com.tokyo2026.trip.data

enum class Art {
    SKYTREE, TOWER, TEMPLE, GATE, COASTER, GACHA, ANIME, SHOP, FIREWORKS, BAR,
    CAT, GHIBLI, MARKET, PARK, DISNEY, HOTEL, EXHIBIT, MINIATURE, DREAM, HILLS,
    ISLAND, AQUARIUM, FERRIS, VIEW, SHRINE, GOV, NIGHT, CROSSING, DOG, SKY, AIRPORT, MUSEUM
}

enum class Cat(val label: String) {
    VIEW("展望"), CULTURE("文化"), OTAKU("御宅"), THEME("樂園"), NATURE("自然"),
    FOOD("美食"), SHOP("購物"), NIGHT("夜間"), STAY("住宿")
}

data class Spot(
    val id: String,
    val nameZh: String,
    val nameJa: String,
    val kana: String,
    val art: Art,
    val cat: Cat,
    val area: String,
    val lat: Double,
    val lng: Double,
    val stay: String,
    val hours: String,
    val price: String,
    val closed: String,
    val booking: String? = null,
    val notes: List<String> = emptyList(),
    val eats: List<String> = emptyList(),
    val warns: List<String> = emptyList(),
    val official: String? = null,
    /** 地圖查詢字串。留空則用日文名；名稱含／＠或活動名時才需覆寫成實際地標。 */
    val mapQuery: String? = null
) {
    val photo: String? get() = photoAsset(id)

    // 用地標名而非座標查詢，Google Maps 才會顯示店名、評價與營業時間；
    // 只給經緯度的話畫面上只會是一個無名的圖釘。
    private val q: String get() = java.net.URLEncoder.encode(mapQuery ?: nameJa, "UTF-8")
    val mapUrl: String get() = "https://www.google.com/maps/search/?api=1&query=$q"
    val navUrl: String get() = "https://www.google.com/maps/dir/?api=1&destination=$q&travelmode=transit"
}

data class Leg(val mode: String, val route: String, val minutes: Int, val fare: String)

data class Stop(val time: String, val spot: Spot, val leg: Leg? = null)

data class Day(
    val n: Int,
    val date: String,
    val weekday: String,
    val theme: String,
    val summary: String,
    val hotelName: String,
    val hotelNote: String,
    val stops: List<Stop>,
    val tips: List<String> = emptyList(),
    val alerts: List<String> = emptyList()
) {
    val moveMinutes: Int get() = stops.sumOf { it.leg?.minutes ?: 0 }
}

data class CheckItem(val id: String, val group: String, val text: String)
