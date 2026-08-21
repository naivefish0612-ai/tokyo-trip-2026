package com.tokyo2026.trip.data

internal val day1 = Day(
    n = 1, date = "9/25", weekday = "週五",
    theme = "抵達日（CI 106 20:50 落地）",
    summary = "20:50 才落地，出關已近 22:00，今天只做一件事：安全抵達旅館。",
    hotelName = "東橫INN 東京日暮里．三之輪站前",
    hotelNote = "日比谷線三之輪站徒步 2 分。櫃台 24 小時，深夜入住沒問題；線上先 check-in 可省 10 分鐘。",
    alerts = listOf(
        "CI 106 台北 16:25 → 成田 20:50（第 2 航廈）。入境＋提行李通常 40–60 分，實際出關約 21:30–21:50。",
        "原本排在今天的晴空塔（末入場 21:00）與東京鐵塔（末入場 22:30）都趕不上，已分別移到 Day 2 中午與 Day 5 晚上。",
        "Skyliner 空港第2ビル 站末班 23:23 發、日暮里 23:59 到，時間充裕；但日比谷線末班約 0:20，別在機場拖太久。"
    ),
    tips = listOf(
        "先在飛機上填好 Visit Japan Web 的 QR，出關可走電子閘門。",
        "航廈 B1 京成櫃台可直接買 Skyliner；用自動售票機比排人工快。",
        "第一晚不要買太多東西，行李箱留空給後面 7 天。"
    ),
    stops = listOf(
        Stop("20:50",
            leg = Leg("航班", "CI 106　台北 TPE T2 16:25 → 成田 NRT T2 20:50（3 小時 25 分）", 205, "已購票"),
            spot = Spot(
                id = "d1_arrive", nameZh = "成田機場入境", nameJa = "成田国際空港 第2ターミナル",
                kana = "なりたくうこう だい2ターミナル", art = Art.AIRPORT, cat = Cat.STAY,
                area = "千葉縣成田市", lat = 35.7736, lng = 140.3853,
                stay = "60 分", hours = "航廈 24 小時；京成售票窗口至 23:00 前後",
                price = "Skyliner 約 2,580 円", closed = "—",
                notes = listOf(
                    "T2 入境後直接下 B1 搭 Skyliner，站名是「空港第2ビル」。",
                    "深夜想省錢可搭京成本線特急（約 1,050 円、多 30 分）。",
                    "先在機場超商買明天早餐與飲料，三之輪一帶夜間店少。"
                ),
                warns = listOf("行李轉盤等待常 20–30 分，別急著訂太早的車。"),
                official = "https://www.narita-airport.jp/"
            )
        ),
        Stop("22:00",
            leg = Leg("電車", "Skyliner 空港第2ビル→日暮里（36 分）→JR 山手線 1 站至上野→日比谷線→三之輪", 60, "約 2,760 円"),
            spot = Spot(
                id = "d1_hotel", nameZh = "東橫INN 三之輪站前（入住）", nameJa = "東横INN東京日暮里三ノ輪駅前",
                kana = "とうよこイン みのわえきまえ", art = Art.HOTEL, cat = Cat.STAY,
                area = "三之輪／台東區", lat = 35.7276, lng = 139.7924,
                stay = "住宿（2 晚）", hours = "入住 16:00／退房 10:00，櫃台 24 小時", price = "已預訂", closed = "無",
                notes = listOf(
                    "免費早餐 06:30 起，7 點半後常滿，明天要早出門就 7 點前下樓。",
                    "步行 7 分有「ジョイフル三の輪」商店街。",
                    "日比谷線可直達上野、秋葉原、六本木，前兩天移動都靠它。"
                ),
                eats = listOf("站前超商深夜補給", "三ノ輪的天婦羅蕎麥麵（隔天）"),
                warns = listOf("房間偏小，大行李建議只開一個。", "洗衣機數量少，21 點後要搶。"),
                official = "https://www.toyoko-inn.com/"
            )
        )
    )
)

internal val day2 = Day(
    n = 2, date = "9/26", weekday = "週六",
    theme = "淺草．晴空塔．秋葉原．赤羽花火",
    summary = "雷門開門就到，中午補上晴空塔，午後秋葉原掃貨，18:30 赤羽看花火。",
    hotelName = "東橫INN 三之輪站前（第 2 晚）",
    hotelNote = "花火散場：赤羽→JR 京濱東北線→上野→日比谷線→三之輪，約 35 分。",
    alerts = listOf(
        "晴空塔由 Day 1 移到今天中午，且依你的需求改成「拍照就走」：不上展望台、不用買票，省下 2 小時與 2,200 円。",
        "北區花火會 2026/9/26（六）18:30–19:40，荒川岩淵水門，赤羽免費區 15:00 開場。",
        "池袋 LOFT 已移到 Day 7（豪德寺→池袋順路），今天不排。"
    ),
    tips = listOf(
        "塔不上樓後今天寬鬆許多：秋葉原有 4 小時，16:40 就能往赤羽卡位。",
        "週六秋葉原中央通 13:00–18:00 步行者天國，好走但店內最擠；六家店都在 5 分鐘徒步圈內。",
        "花火免費區無座位，帶野餐墊與垃圾袋；17:30 前一定要離開秋葉原。"
    ),
    stops = listOf(
        Stop("08:30",
            leg = Leg("電車", "日比谷線 三之輪→上野，轉銀座線→淺草（1 號出口）", 20, "約 220 円"),
            spot = Spot(
                id = "d2_sensoji", nameZh = "淺草寺 雷門", nameJa = "浅草寺 雷門",
                kana = "せんそうじ かみなりもん", art = Art.GATE, cat = Cat.CULTURE,
                area = "淺草／台東區", lat = 35.7118, lng = 139.7966,
                stay = "1.5 小時", hours = "本堂 6:30–17:00；仲見世約 9:30–19:00",
                price = "免費（御朱印 500 円）", closed = "全年無休",
                notes = listOf(
                    "7–8 點來：仲見世未開，雷門可拍到無人畫面。",
                    "雷門大燈籠底部有木雕龍，抬頭才看得到。",
                    "淺草寺凶籤比例高（約 3 成），抽到綁在架上即可。",
                    "旁邊淺草神社人少很多，週末常有神前婚禮。"
                ),
                eats = listOf("人形燒、揚饅頭", "浅草メンチ、舟和芋羊羹", "ペリカン吐司當早餐"),
                warns = listOf("仲見世禁止邊走邊吃。", "10 點後人潮爆量，務必早到。"),
                official = "https://www.senso-ji.jp/"
            )
        ),
        Stop("09:50",
            leg = Leg("步行", "淺草寺本堂西側徒步 3 分", 3, "—"),
            spot = Spot(
                id = "d2_hanayashiki", nameZh = "淺草花屋敷", nameJa = "浅草花やしき",
                kana = "あさくさはなやしき", art = Art.COASTER, cat = Cat.THEME,
                area = "淺草／台東區", lat = 35.7150, lng = 139.7947,
                stay = "1.2 小時", hours = "10:00–18:00（末入園閉園前 30 分）",
                price = "入園 1,200 円；One Day Pass 約 2,800 円", closed = "不定休",
                notes = listOf(
                    "1853 年開園，日本現存最古老遊樂園。",
                    "雲霄飛車 1953 年製，會擦過住宅屋簷。",
                    "玩 3 樣以下買單次券划算，4 樣以上才買 One Day Pass。",
                    "重點是昭和感：Bee Tower、旋轉木馬與復古看板。"
                ),
                eats = listOf("園內熊貓車造型點心"),
                warns = listOf("雲霄飛車身高限制 110cm。", "雨天部分遊具停開，票價不變。"),
                official = "https://www.hanayashiki.net/"
            )
        ),
        Stop("11:15",
            leg = Leg("電車", "東武晴空塔線 淺草→とうきょうスカイツリー（1 站 3 分）", 8, "160 円"),
            spot = Spot(
                id = "d1_skytree", nameZh = "東京晴空塔", nameJa = "東京スカイツリー",
                kana = "とうきょうスカイツリー", art = Art.SKYTREE, cat = Cat.VIEW,
                area = "押上／墨田區", lat = 35.7101, lng = 139.8107,
                stay = "40 分（純拍照，不上塔）", hours = "塔下 24 小時可拍；展望台 10:00–22:00",
                price = "外拍免費（上天望甲板為變動票價，大人 1,800–3,600 円；現場購票 +500 円）",
                closed = "全年無休", booking = "不上塔就不需要買票",
                notes = listOf(
                    "只要拍照的話最佳點是十間橋：水面倒影＋全塔入鏡，押上站徒步 12 分。",
                    "更省時的選擇：淺草吾妻橋，晴空塔＋朝日啤酒金色物體同框，從花屋敷走 10 分就到。",
                    "晴空街道 1F 廣場可仰拍塔底，配合廣角很有張力。",
                    "上塔要 2 小時以上，今天有花火，建議直接放棄展望台。"
                ),
                eats = listOf("晴空街道 1F 食品區（伴手禮補貨）"),
                warns = listOf("陰雨天塔頂會沒入雲中，拍照請趁天氣好時。", "12:00 前離開，秋葉原才有充足時間。"),
                official = "https://www.tokyo-skytree.jp/"
            )
        ),
        Stop("12:20",
            leg = Leg("電車", "押上→都營淺草線→淺草橋，轉 JR 總武線→秋葉原（徒步 5 分，先吃午餐）", 20, "約 310 円"),
            spot = Spot(
                id = "d2_gacha", nameZh = "秋葉原扭蛋會館", nameJa = "秋葉原ガチャポン会館",
                kana = "ガチャポンかいかん", art = Art.GACHA, cat = Cat.OTAKU,
                area = "秋葉原／千代田區", lat = 35.7010, lng = 139.7714,
                stay = "45 分", hours = "平日 11:00–20:00、週六 11:00–22:00、週日假日 11:00–19:00",
                price = "免費入場（單台 200–500 円）", closed = "全年無休",
                notes = listOf(
                    "500 台以上，每月約 50 種新作。",
                    "門口是熱門動畫，最裡面才是在地人挖的冷門款。",
                    "先在車站超商換 100 円硬幣，館內換鈔機常排隊。"
                ),
                warns = listOf("走道狹窄，後背包請背胸前。", "扭蛋殼投店內回收箱。"),
                official = "https://www.akibagacha.com/"
            )
        ),
        Stop("13:15",
            leg = Leg("步行", "中央通り徒步 4 分", 4, "—"),
            spot = Spot(
                id = "d2_radiokaikan", nameZh = "秋葉原無線電會館", nameJa = "秋葉原ラジオ会館",
                kana = "ラジオかいかん", art = Art.SHOP, cat = Cat.OTAKU,
                area = "秋葉原／千代田區", lat = 35.6986, lng = 139.7716,
                stay = "1 小時", hours = "多數 10:00–20:00；B1F 12:00–22:00",
                price = "免費入場", closed = "幾乎全年無休",
                notes = listOf(
                    "秋葉原電器街原點（1950 年創業），10 層樓的模型公仔聖地。",
                    "重點：3F 海洋堂、4F あみあみ、5F K-BOOKS、6F 卡牌。",
                    "《命運石之門》舞台就是這棟。",
                    "二手玻璃櫃同款不同櫃常差 3 成，多走幾層再下手。"
                ),
                warns = listOf("多數店禁攝影。", "退稅只在部分店家，需護照正本。"),
                official = "https://akihabara-radiokaikan.co.jp/"
            )
        ),
        Stop("14:05",
            leg = Leg("步行", "秋葉原站電氣街口徒步 1 分", 1, "—"),
            spot = Spot(
                id = "d2_surugaya_ekimae", nameZh = "駿河屋 秋葉原車站前店", nameJa = "駿河屋 秋葉原駅前店",
                kana = "するがや えきまえてん", art = Art.ANIME, cat = Cat.OTAKU,
                area = "秋葉原／千代田區", lat = 35.6992, lng = 139.7719,
                stay = "40 分", hours = "平日 9:00–翌 1:00；週六與假日前 9:00–翌 3:00",
                price = "免費入場", closed = "全年無休",
                notes = listOf(
                    "營業到凌晨，是秋葉原夜行性玩家的最後一站。",
                    "定價＝回收價＋固定利潤，冷門作品常出現離譜低價。",
                    "秋葉原三家駿河屋價格不同，貴的東西比過再買。"
                ),
                warns = listOf("標籤 A/B/ジャンク 為狀態分級，ジャンク不保證能動。"),
                official = "https://www.suruga-ya.jp/"
            )
        ),
        Stop("14:45",
            leg = Leg("步行", "中央通り往北徒步 5 分", 5, "—"),
            spot = Spot(
                id = "d2_aczone", nameZh = "AKIBA Cultures ZONE", nameJa = "AKIBAカルチャーズZONE",
                kana = "アキバカルチャーズゾーン", art = Art.ANIME, cat = Cat.OTAKU,
                area = "秋葉原／千代田區", lat = 35.7003, lng = 139.7707,
                stay = "40 分", hours = "11:00–23:00（各店略異）",
                price = "免費入場（劇場、女僕咖啡另計）", closed = "全年無休",
                notes = listOf(
                    "B1F 劇場幾乎每天有地下偶像／聲優活動，可當日買票。",
                    "4F ASTOP 是大型寄賣展示櫃，挖寶重點。",
                    "5F @home cafe 是女僕咖啡代表店，週六先抽號碼。"
                ),
                eats = listOf("@home cafe 蛋包飯"),
                warns = listOf("女僕咖啡為入場費＋飲料＋拍照另計，先確認總價。"),
                official = "https://www.akibacultureszone.com/"
            )
        ),
        Stop("15:25",
            leg = Leg("步行", "徒步 3 分", 3, "—"),
            spot = Spot(
                id = "d2_surugaya_hobby", nameZh = "駿河屋 秋葉原動漫嗜好館", nameJa = "駿河屋 秋葉原アニメ・ホビー館",
                kana = "アニメ・ホビーかん", art = Art.ANIME, cat = Cat.OTAKU,
                area = "秋葉原／千代田區", lat = 35.7000, lng = 139.7712,
                stay = "40 分", hours = "平日 11:00–21:00；週六日假日 10:00–21:00",
                price = "免費入場", closed = "全年無休",
                notes = listOf(
                    "以動畫周邊、扭蛋、一番賞尾款為主。",
                    "樓層越高越冷門也越便宜。",
                    "常有整箱「まとめ売り」出清。"
                ),
                warns = listOf("各樓層獨立結帳，跨樓層無法合併退稅。"),
                official = "https://www.suruga-ya.jp/"
            )
        ),
        Stop("16:00",
            leg = Leg("步行", "徒步 3 分", 3, "—"),
            spot = Spot(
                id = "d2_surugaya_main", nameZh = "駿河屋 秋葉原本館", nameJa = "駿河屋 秋葉原本館",
                kana = "するがや ほんかん", art = Art.ANIME, cat = Cat.OTAKU,
                area = "秋葉原／千代田區", lat = 35.7004, lng = 139.7717,
                stay = "30 分", hours = "約 11:00–21:00",
                price = "免費入場", closed = "全年無休",
                notes = listOf(
                    "三家中「老件」最強：復古遊戲、懷舊卡帶、絕版模型。",
                    "看標籤上的「箱・説」判斷是否含盒書，價差極大。"
                ),
                warns = listOf("復古主機多數不含保固，通電確認請當場提出。"),
                official = "https://www.suruga-ya.jp/"
            )
        ),
        Stop("16:40",
            leg = Leg("電車", "秋葉原→JR 京濱東北線→赤羽（東口徒步 20 分至河川敷）", 20, "約 230 円"),
            spot = Spot(
                id = "d2_hanabi", nameZh = "北區花火會．赤羽會場", nameJa = "北区花火会 RED×BLUE SPARKLE GATE",
                kana = "きたくはなびかい", art = Art.FIREWORKS, cat = Cat.NIGHT,
                area = "赤羽／荒川岩淵水門", lat = 35.7856, lng = 139.7261,
                stay = "3 小時（含卡位散場）", hours = "9/26 18:30–19:40 施放；免費區 15:00 開場",
                price = "免費區 0 円；有料座席需事前購票", closed = "雨天可能中止",
                notes = listOf(
                    "以紅水門與青水門為背景約 1 萬發，並有無人機與音樂同步。",
                    "赤羽站東口徒步 20–25 分，越靠水門視野越好也越早滿。",
                    "在地作法：先鋪墊子，再輪流去買晚餐。",
                    "赤羽站前「OK 橫丁」白天就能喝，是東京有名的居酒屋街。"
                ),
                eats = listOf("OK 橫丁串燒與煮込み", "屋台炒麵、べビーカステラ"),
                warns = listOf(
                    "20:00 前先離場或 21:00 後再走，可避開最擠 30 分。",
                    "河川敷幾乎沒廁所，垃圾要自行帶走。",
                    "草地碎石地，請穿球鞋。"
                ),
                official = "https://kitaku-hanabi.tokyo/"
            )
        ),
        Stop("21:30",
            leg = Leg("電車", "赤羽→JR 京濱東北線→御徒町（徒步 5 分）", 25, "約 230 円"),
            spot = Spot(
                id = "d2_glory", nameZh = "Glory Hole Bar（成人場所）", nameJa = "GLORY HOLE BAR 上野",
                kana = "グローリーホールバー", art = Art.BAR, cat = Cat.NIGHT,
                area = "上野廣小路／台東區", lat = 35.7070, lng = 139.7740,
                stay = "1–2 小時", hours = "每日 13:00–23:30（新宿店至翌 0:00）",
                price = "入場約 1,800 円（含 1 杯）", closed = "全年無休",
                notes = listOf(
                    "御徒町站徒步 5 分、上野廣小路 A3 出口 2 分。",
                    "屬日本男同志圈的発展バー，非一般觀光酒吧，限成年。",
                    "同系列有新宿分店，Day 7 在新宿時更順路。"
                ),
                warns = listOf("全面禁止攝影錄音，並有著衣與行為規範。", "花火散場後很累，體力不足建議直接回旅館。"),
                official = "https://www.gloryholebar.com/"
            )
        )
    )
)
