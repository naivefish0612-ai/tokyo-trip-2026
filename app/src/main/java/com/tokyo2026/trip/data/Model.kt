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
    val official: String? = null
) {
    val photo: String? get() = photoAsset(id)
    val mapUrl: String get() = "https://www.google.com/maps/search/?api=1&query=$lat,$lng"
    val navUrl: String get() = "https://www.google.com/maps/dir/?api=1&destination=$lat,$lng&travelmode=transit"
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
