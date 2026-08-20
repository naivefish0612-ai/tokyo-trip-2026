package com.tokyo2026.trip.data

/** 景點照片來源（Wikimedia Commons，皆為 CC/公有領域授權） */
data class PhotoCredit(val file: String, val author: String, val license: String, val page: String)

val photoCredits: Map<String, PhotoCredit> = mapOf(
    "d1_hotel" to PhotoCredit("Tokyo-Metro Minowa-STA Minowa-Intersection-Gate.jpg", "MaedaAkihiko", "CC0", "https://commons.wikimedia.org/wiki/File%3ATokyo-Metro_Minowa-STA_Minowa-Intersection-Gate.jpg"),
    "d1_skytree" to PhotoCredit("Tokyo Skytree at night view.jpg", "VaneTrz20", "CC0", "https://commons.wikimedia.org/wiki/File%3ATokyo_Skytree_at_night_view.jpg"),
    "d1_tokyotower" to PhotoCredit("Tokyo tower at night 2024.jpg", "Immanuelle", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File%3ATokyo_tower_at_night_2024.jpg"),
    "d2_aczone" to PhotoCredit("Sotokanda, Akihabara Electric Town at night 20231114.png", "Phineyes", "CC0", "https://commons.wikimedia.org/wiki/File%3ASotokanda%2C_Akihabara_Electric_Town_at_night_20231114.png"),
    "d2_gacha" to PhotoCredit("Gashapon Machines.jpg", "Ominae", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AGashapon_Machines.jpg"),
    "d2_glory" to PhotoCredit("Okachimachi town.JPG", "Hide1228", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AOkachimachi_town.JPG"),
    "d2_hanayashiki" to PhotoCredit("Asakusa Hanayashiki-2.jpg", "江戸村のとくぞう", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AAsakusa_Hanayashiki-2.jpg"),
    "d2_radiokaikan" to PhotoCredit("AKIHABARA New Radio Kaikan.jpg", "Yuukokusya", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AAKIHABARA_New_Radio_Kaikan.jpg"),
    "d2_sensoji" to PhotoCredit("Kaminarimon Gate of Sensoji Temple.JPG", "そらみみ (Soramimi)", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AKaminarimon_Gate_of_Sensoji_Temple.JPG"),
    "d2_surugaya_ekimae" to PhotoCredit("A-too Suruga-ya Akihabara.jpg", "photo: Qurren (talk) Taken with Samsung Galaxy S9 (NTT docom", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AA-too_Suruga-ya_Akihabara.jpg"),
    "d2_surugaya_hobby" to PhotoCredit("Akihabara Surugaya IMG 5995.jpg", "Bject", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AAkihabara_Surugaya_IMG_5995.jpg"),
    "d2_surugaya_main" to PhotoCredit("Akihabara Chuo-dori Crossing SW 20110925.jpg", "Vantey", "Public domain", "https://commons.wikimedia.org/wiki/File%3AAkihabara_Chuo-dori_Crossing_SW_20110925.jpg"),
    "d3_ghibli" to PhotoCredit("Ghibli Museum, Mitaka, Tokyo, 20240823 1131 5545.jpg", "Jakub Hałun", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File%3AGhibli_Museum%2C_Mitaka%2C_Tokyo%2C_20240823_1131_5545.jpg"),
    "d3_miyashita" to PhotoCredit("Miyashita Park seen from Shibuya Stream.jpg", "Syced", "CC0", "https://commons.wikimedia.org/wiki/File%3AMiyashita_Park_seen_from_Shibuya_Stream.jpg"),
    "d3_petitmura" to PhotoCredit("Kichijoji road.jpg", "Douglaspperkins", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File%3AKichijoji_road.jpg"),
    "d3_senkyaku" to PhotoCredit("Toyosu Senkyaku Banrai.jpg", "Fred Cherrygarden", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AToyosu_Senkyaku_Banrai.jpg"),
    "d3_tshotel" to PhotoCredit("Room at the Toy Story Hotel in Tokyo Disney Resort.jpg", "nagi usano", "CC BY 4.0", "https://commons.wikimedia.org/wiki/File%3ARoom_at_the_Toy_Story_Hotel_in_Tokyo_Disney_Resort.jpg"),
    "d4_dormy" to PhotoCredit("Lazona Kawasaki Plaza 2022-10-18 (1).jpg", "NickeldimeC", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3ALazona_Kawasaki_Plaza_2022-10-18_%281%29.jpg"),
    "d4_tdl" to PhotoCredit("Cinderella Castle, Tokyo Disney Resort in Japan.jpg", "Speedway at English Wikipedia", "Public domain", "https://commons.wikimedia.org/wiki/File%3ACinderella_Castle%2C_Tokyo_Disney_Resort_in_Japan.jpg"),
    "d5_azabudai" to PhotoCredit("Azabudai Hills Mori JP Tower on opening day, seen from Roppongi Hills.jpg", "Syced", "CC0", "https://commons.wikimedia.org/wiki/File%3AAzabudai_Hills_Mori_JP_Tower_on_opening_day%2C_seen_from_Roppongi_Hills.jpg"),
    "d5_smallworlds" to PhotoCredit("SMALL WORLDS Miniature Museum 01.jpg", "Bea Phi", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3ASMALL_WORLDS_Miniature_Museum_01.jpg"),
    "d6_cosmoclock" to PhotoCredit("20250101 Cosmo Clock 21, Landmark Tower 122120.jpg", "Matthide127", "CC0", "https://commons.wikimedia.org/wiki/File%3A20250101_Cosmo_Clock_21%2C_Landmark_Tower_122120.jpg"),
    "d6_cosmoworld" to PhotoCredit("Yokohama Cosmo World (130918).jpg", "Thirteen-fri", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File%3AYokohama_Cosmo_World_%28130918%29.jpg"),
    "d6_enoshima" to PhotoCredit("Enoshima Sea Candle - Samuel Cocking Garden - Enoshima, Japan - DSC07749.jpg", "Daderot", "CC0", "https://commons.wikimedia.org/wiki/File%3AEnoshima_Sea_Candle_-_Samuel_Cocking_Garden_-_Enoshima%2C_Japan_-_DSC07749.jpg"),
    "d6_hakkeijima" to PhotoCredit("Yokohama Hakkeijima Sea Paradise - 01.jpg", "Quercus acuta", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AYokohama_Hakkeijima_Sea_Paradise_-_01.jpg"),
    "d6_kitanaka" to PhotoCredit("The Tower Yokohama Kitanaka 20221127-1.jpg", "Suicasmo", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AThe_Tower_Yokohama_Kitanaka_20221127-1.jpg"),
    "d6_landmark" to PhotoCredit("Yokohama-Japon112.JPG", "Diego Delso", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AYokohama-Japon112.JPG"),
    "d6_viainn" to PhotoCredit("Walk around Oimachi station (30319).jpg", "Syced", "CC0", "https://commons.wikimedia.org/wiki/File%3AWalk_around_Oimachi_station_%2830319%29.jpg"),
    "d7_fujiko" to PhotoCredit("Fujiko･F･Fujio Museum -01.jpg", "Aimaimyi", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File%3AFujiko%EF%BD%A5F%EF%BD%A5Fujio_Museum_-01.jpg"),
    "d7_gotokuji" to PhotoCredit("Gōtoku-ji Maneki-neko cats.jpg", "Nacaru", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AG%C5%8Dtoku-ji_Maneki-neko_cats.jpg"),
    "d7_hachiko" to PhotoCredit("Hachiko (53083712909).jpg", "Dick Thomas Johnson from Tokyo, Japan", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File%3AHachiko_%2853083712909%29.jpg"),
    "d7_kabukicho" to PhotoCredit("Colorful neon street signs in Kabukichō, Shinjuku, Tokyo.jpg", "Basile Morin", "CC BY-SA 4.0", "https://commons.wikimedia.org/wiki/File%3AColorful_neon_street_signs_in_Kabukich%C5%8D%2C_Shinjuku%2C_Tokyo.jpg"),
    "d7_scramble" to PhotoCredit("Shibuya Scramble crossing.jpg", "Syced", "CC0", "https://commons.wikimedia.org/wiki/File%3AShibuya_Scramble_crossing.jpg"),
    "d7_shibuyasky" to PhotoCredit("Shibuya Sky Observation Deck (53415730632).jpg", "Stephen Kelly from San Francisco, CA, USA", "CC BY 2.0", "https://commons.wikimedia.org/wiki/File%3AShibuya_Sky_Observation_Deck_%2853415730632%29.jpg"),
    "d7_tocho" to PhotoCredit("Tokyo Metropolitan Government Building 2022-03-02.jpg", "Asanagi", "CC0", "https://commons.wikimedia.org/wiki/File%3ATokyo_Metropolitan_Government_Building_2022-03-02.jpg"),
    "d8_narita" to PhotoCredit("Narita International Airport Terminal 1.JPG", "名無し野電車区", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File%3ANarita_International_Airport_Terminal_1.JPG"),
    "d1_arrive" to PhotoCredit("Narita International Airport Terminal 1.JPG", "名無し野電車区", "CC BY-SA 3.0", "https://commons.wikimedia.org/wiki/File%3ANarita_International_Airport_Terminal_1.JPG")
)

fun photoAsset(id: String): String? = if (photoCredits.containsKey(id)) "photos/$id.jpg" else null
