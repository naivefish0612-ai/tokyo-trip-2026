package com.tokyo2026.trip.data

object Trip {
    const val title = "2026 東京 8 天 7 夜"
    const val subtitle = "9/25 – 10/2　CI 106 去／CI 105 回"
    const val flightOut = "CI 106　9/25（五）TPE T2 16:25 → NRT T2 20:50"
    const val flightBack = "CI 105　10/2（五）NRT T2 17:55 → TPE T2 20:35"
    const val startIso = "2026-09-25"

    val days: List<Day> = listOf(day1, day2, day3, day4, day5, day6, day7, day8)

    val allSpots: List<Spot> = days.flatMap { d -> d.stops.map { it.spot } }

    fun dayOf(spotId: String): Day? = days.firstOrNull { d -> d.stops.any { it.spot.id == spotId } }

    val photoCount: Int = allSpots.count { it.photo != null }

    val hotels: List<Pair<String, String>> = listOf(
        "9/25–9/27（2 晚）" to "東橫INN 東京日暮里．三之輪站前",
        "9/27–9/28（1 晚）" to "東京迪士尼度假區 玩具總動員飯店",
        "9/28–9/30（2 晚）" to "天然溫泉 Dormy Inn 川崎",
        "9/30–10/2（2 晚）" to "VIA INN 東京大井町"
    )

    /** 出發前必須先訂好的票（依開賣時間排序） */
    val bookings: List<Triple<String, String, String>> = listOf(
        Triple("三鷹之森吉卜力美術館", "✅ 已訂：9/27（日）14:00 入場", "入場需核對姓名與證件"),
        Triple("藤子・F・不二雄博物館", "官網 e-tix 日時指定券", "完全預約制，館內不售當日票"),
        Triple("澀谷 SKY", "官網日時指定券（20 分一梯）", "日落梯次最搶手，建議 1 個月前"),
        Triple("東京迪士尼樂園", "官網／App 日時指定票", "6 段變動票價，平日較便宜"),
        Triple("皮克斯的世界展", "✅ 已訂：9/29（二）14:00 入場", "建議 13:45 到現場排隊"),
        Triple("東京晴空塔／東京鐵塔", "不需購票", "已改為外拍不上塔（Day 2 中午、Day 5 晚上）"),
        Triple("豐洲千客萬來", "不需預約", "已移到 Day 8 上午，回程前的最後一站"),
        Triple("SMALL WORLDS TOKYO", "官網早鳥票", "無公休日，票價依平日／假日不同")
    )

    val checklist: List<CheckItem> = listOf(
        CheckItem("c_flight_out", "航班", "CI 106 去程 9/25 16:25 TPE T2（建議 14:30 到機場）"),
        CheckItem("c_flight_back", "航班", "CI 105 回程 10/2 17:55 NRT T2（14:50 前完成報到）"),
        CheckItem("c_pass", "證件", "護照效期 6 個月以上、影本另存手機"),
        CheckItem("c_visit", "證件", "Visit Japan Web 入境／海關 QR 碼先申辦完成"),
        CheckItem("c_ins", "證件", "旅平險與不便險（本行程含戶外花火與樂園）"),
        CheckItem("c_ticket_ghibli", "票券", "吉卜力美術館 9/27 14:00（已訂，記得帶證件）"),
        CheckItem("c_ticket_fujiko", "票券", "藤子・F・不二雄博物館 日時指定券"),
        CheckItem("c_ticket_sky", "票券", "澀谷 SKY 日落梯次"),
        CheckItem("c_ticket_tdl", "票券", "東京迪士尼樂園 9/28 入場券"),
        CheckItem("c_ticket_pixar", "票券", "皮克斯的世界展 9/29 14:00（已訂）"),
        CheckItem("c_suica", "交通", "Suica／PASMO 或手機票卡（先儲值 3,000 円）"),
        CheckItem("c_nex", "交通", "回程走市場前→新橋→Access 特急（約 80 分）已確認"),
        CheckItem("c_arrive", "交通", "抵達日：Skyliner 空港第2ビル→日暮里，末班 23:23"),
        CheckItem("c_app", "App", "東京迪士尼官方 App 並綁定住宿與票券"),
        CheckItem("c_map", "App", "Google Maps 離線地圖（東京、橫濱）已下載"),
        CheckItem("c_jorudan", "App", "乘換案內（Japan Transit Planner）"),
        CheckItem("c_sim", "通訊", "eSIM／WiFi 分享器（花火與樂園當天很吃網路）"),
        CheckItem("c_power", "行李", "行動電源（10,000mAh 以上，樂園日必備）"),
        CheckItem("c_shoes", "行李", "好走的鞋 2 雙（江之島階梯、迪士尼一日）"),
        CheckItem("c_jacket", "行李", "薄外套（9 月底早晚溫差、海邊風大）"),
        CheckItem("c_sheet", "行李", "野餐墊＋垃圾袋（9/26 赤羽花火用）"),
        CheckItem("c_umb", "行李", "折傘或輕便雨衣（樂園禁大傘的日子多）"),
        CheckItem("c_cash", "金錢", "現金 3–5 萬円（花火屋台、老店多不收卡）"),
        CheckItem("c_coin", "金錢", "100 円硬幣（秋葉原扭蛋、置物櫃）"),
        CheckItem("c_luggage", "住宿", "9/27、9/28、9/30 三次換宿的行李移動方式（宅急便或自行搬運）"),
        CheckItem("c_early", "住宿", "9/28 迪士尼日：行李先寄放或宅配至川崎")
    )
}
