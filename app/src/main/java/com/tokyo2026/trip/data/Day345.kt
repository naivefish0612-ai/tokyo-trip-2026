package com.tokyo2026.trip.data

internal val day3 = Day(
    n = 3, date = "9/27", weekday = "週日",
    theme = "吉祥寺．吉卜力 14:00．澀谷",
    summary = "配合 14:00 入館場次重排：上午吉祥寺，下午吉卜力，傍晚澀谷，晚上進舞濱。",
    hotelName = "東京迪士尼度假區 玩具總動員飯店",
    hotelNote = "入住 15:00／退房 12:00。早上先把行李寄放在東橫INN 或宅配到舞濱；舞濱站轉度假區線（300 円）。",
    alerts = listOf(
        "已預訂：吉卜力美術館 9/27（日）14:00 入場。入場需核對購票姓名與證件，遲到逾 30 分可能無法入場。",
        "動線已依 14:00 場次重排，並把原本的豐洲千客萬來移到 Day 5（有明→市場前僅 2 站），今天才不會撐到深夜。"
    ),
    tips = listOf(
        "回程走吉祥寺：看完美術館沿井之頭公園走回吉祥寺（20 分），搭井之頭線急行到澀谷只要 17 分，比繞新宿快。",
        "入場梯次只限制進場時間，進去後待多久都行，14:00 場可待到 16:30。",
        "澀谷 MIYASHITA PARK 屋頂草坪看夕陽，樓下渋谷横丁解決晚餐最省時。"
    ),
    stops = listOf(
        Stop("09:40",
            leg = Leg("電車", "日比谷線 三之輪→秋葉原，轉 JR 中央線快速→吉祥寺", 45, "約 400 円"),
            spot = Spot(
                id = "d3_petitmura", nameZh = "吉祥寺貓村（Petit Mura）", nameJa = "吉祥寺プティット村",
                kana = "きちじょうじプティットむら", art = Art.CAT, cat = Cat.CULTURE,
                area = "吉祥寺／武藏野市", lat = 35.7060, lng = 139.5820,
                stay = "1 小時", hours = "平日 11:00–20:00、週六日假日 10:00–20:00",
                price = "入村免費；貓咖啡「てまりのおしろ」約 1,600 円／小時起",
                closed = "各店不定休", booking = "貓咖啡建議先線上預約，週日常滿",
                notes = listOf(
                    "繪本世界主題的小中庭，10 分鐘走完，重點是拍照與貓咖啡。",
                    "別跑錯：同系列另有「てまりのおうち」在其他地點。",
                    "順走井之頭恩賜公園（8 分），週日有街頭藝人與手作市集。"
                ),
                eats = listOf("TEA HOUSE はっぱ 的司康", "いせや総本店 燒鳥", "SUNROAD 商店街 小ざさ最中"),
                warns = listOf("貓咖啡禁止抱貓與叫醒貓。", "中庭狹小，拍照請留意其他客人。"),
                official = "https://petitmura.com/"
            )
        ),
        Stop("13:40",
            leg = Leg("步行／公車", "井之頭公園內徒步 20 分，或 JR 三鷹站南口みたかシティバス 5 分", 20, "220 円"),
            spot = Spot(
                id = "d3_ghibli", nameZh = "三鷹之森吉卜力美術館", nameJa = "三鷹の森ジブリ美術館",
                kana = "ジブリびじゅつかん", art = Art.GHIBLI, cat = Cat.CULTURE,
                area = "三鷹／井之頭公園西園", lat = 35.6962, lng = 139.5704,
                stay = "2.5 小時（已訂 14:00 場）", hours = "10:00–18:00，入場梯次 10:00／12:00／14:00／16:00",
                price = "大人 1,000 円、國高中 700 円、小學 400 円、幼兒 100 円",
                closed = "每週二（9/22 特別開館）；11/4–11/18 維修休館",
                booking = "已預訂 9/27 14:00 場",
                notes = listOf(
                    "館方主張「一起迷路吧」，沒有規定路線。",
                    "先衝 2F「電影誕生的地方」與 3F 屋頂機器人兵。",
                    "1F 土星座只放這裡才看得到的原創短片，票根是隨機 35mm 底片。",
                    "商店マンマユート的限定品他處買不到，留 20 分鐘。"
                ),
                eats = listOf("麦わらぼうし 外帶熱狗與吉卜力生啤"),
                warns = listOf("館內全面禁止攝影（屋頂庭園可拍）。", "需核對購票姓名與證件，轉讓票無效。"),
                official = "https://www.ghibli-museum.jp/"
            )
        ),
        Stop("17:10",
            leg = Leg("電車", "步行回吉祥寺→京王井之頭線急行→澀谷（徒步 3 分）", 40, "約 200 円"),
            spot = Spot(
                id = "d3_miyashita", nameZh = "MIYASHITA PARK（宮下公園）", nameJa = "MIYASHITA PARK",
                kana = "ミヤシタパーク", art = Art.PARK, cat = Cat.FOOD,
                area = "澀谷／渋谷區", lat = 35.6612, lng = 139.7017,
                stay = "1.5 小時", hours = "屋頂公園 8:00–23:00；店舖 11:00–21:00、餐飲至 22:30",
                price = "公園免費（滑板場、攀岩需付費預約）", closed = "檢修日不定休",
                notes = listOf(
                    "把公園架在商場屋頂：1–3F 約 90 間店，4F 是渋谷區立宮下公園。",
                    "南側「渋谷横丁」24 小時營業，依日本各地鄉土料理分店。",
                    "屋頂草坪傍晚可看澀谷天際線與渋谷 SKY，免費。"
                ),
                eats = listOf("渋谷横丁的沖繩與北海道料理", "3F 屋台風燒鳥"),
                warns = listOf("屋頂公園禁菸禁酒。", "橫丁部分店家收 charge，先確認。"),
                official = "https://www.miyashita-park.tokyo/"
            )
        ),
        Stop("19:20",
            leg = Leg("電車", "澀谷→JR 山手線→東京，轉 JR 京葉線→舞濱，轉度假區線", 50, "約 570 円"),
            spot = Spot(
                id = "d3_tshotel", nameZh = "玩具總動員飯店", nameJa = "東京ディズニーリゾート・トイ・ストーリーホテル",
                kana = "トイ・ストーリーホテル", art = Art.HOTEL, cat = Cat.STAY,
                area = "舞濱／浦安市", lat = 35.6350, lng = 139.8880,
                stay = "住宿", hours = "入住 15:00／退房 12:00", price = "已預訂", closed = "—",
                notes = listOf(
                    "住宿特典 Happy Entry 可提早 15 分入園，僅限迪士尼樂園（海洋不適用）。",
                    "2026 年部分日期為特典對象外，出發前用官方 App 確認。",
                    "飯店本身就是景點：胡迪雕像、彈簧狗庭園、全套主題房。",
                    "官方 App 先綁定住宿預約號碼，才能抽 DPA 與預約餐廳。"
                ),
                eats = listOf("館內ロッツォガーデンカフェ 自助早餐（需預約）", "舞濱 Ikspiari 深夜餐飲"),
                warns = listOf("沒有大浴場，只有房內浴缸。", "度假區線單軌需付費 300 円。"),
                official = "https://www.tokyodisneyresort.jp/hotel/tsh.html"
            )
        )
    )
)

internal val day4 = Day(
    n = 4, date = "9/28", weekday = "週一",
    theme = "東京迪士尼樂園一日",
    summary = "用住宿特典早入園，玩到煙火，晚上帶行李轉川崎。",
    hotelName = "天然溫泉 Dormy Inn 川崎",
    hotelNote = "JR 川崎站徒步 8 分。溫泉大浴場 15:00–翌 9:00，21:30–23:00 大廳免費「夜鳴きそば」。",
    alerts = listOf(
        "迪士尼 6 段變動票價 7,900–12,400 円；9/28 為平日屬較低價位帶，須事前買日時指定票。",
        "行李：前一晚在玩具總動員飯店櫃台辦宅急便寄到川崎最省事。"
    ),
    tips = listOf(
        "入園前先完成官方 App 註冊、綁票、開定位；入園第一件事是搶 DPA。",
        "免費的 Standby Pass 與抽選每 60 分可再抽一次，別忘記重抽。",
        "21:00 煙火後往出口會塞 40 分，趕車就煙火一結束先走。"
    ),
    stops = listOf(
        Stop("08:00",
            leg = Leg("單軌", "飯店→迪士尼度假區線→東京迪士尼樂園站", 15, "300 円"),
            spot = Spot(
                id = "d4_tdl", nameZh = "東京迪士尼樂園", nameJa = "東京ディズニーランド",
                kana = "とうきょうディズニーランド", art = Art.DISNEY, cat = Cat.THEME,
                area = "舞濱／浦安市", lat = 35.6329, lng = 139.8804,
                stay = "全日", hours = "多為 9:00–21:00（住宿者提早 15 分）",
                price = "1 Day Passport 7,900–12,400 円（6 段變動）", closed = "全年無休",
                booking = "官網／App 日時指定票",
                notes = listOf(
                    "DPA 付費快速通關 1,500–2,500 円，常在開園 1 小時內售完。",
                    "餐廳可在 App 預約優先入席，午餐不預約要等 40 分以上。",
                    "爆米花桶各口味只在特定攤位賣，地圖上有標。",
                    "平日 Happy Entry 15 分足以搶到第一項熱門設施。"
                ),
                eats = listOf("限定爆米花（醬油奶油、蜂蜜）", "鑽石馬蹄餐廳表演秀"),
                warns = listOf("禁帶酒精與罐裝飲料。", "9 月底白天仍可能 28°C，晚上海風冷，帶外套。", "強風時煙火中止不退費。"),
                official = "https://www.tokyodisneyresort.jp/tdl/"
            )
        ),
        Stop("21:30",
            leg = Leg("電車", "舞濱→JR 京葉線→東京，轉東海道線／京濱東北線→川崎", 60, "約 780 円"),
            spot = Spot(
                id = "d4_dormy", nameZh = "Dormy Inn 川崎（入住）", nameJa = "天然温泉 スパ・ドーミーイン川崎",
                kana = "ドーミーインかわさき", art = Art.HOTEL, cat = Cat.STAY,
                area = "川崎／神奈川縣", lat = 35.5305, lng = 139.6975,
                stay = "住宿（2 晚）", hours = "入住 15:00／退房 11:00；大浴場 15:00–翌 9:00",
                price = "已預訂", closed = "—",
                notes = listOf(
                    "招牌免費宵夜「夜鳴きそば」21:30–23:00，玩完迪士尼剛好趕上。",
                    "天然溫泉大浴場含桑拿，是全程解疲勞的關鍵一晚。",
                    "川崎往橫濱 10 分、往品川 10 分，Day 5、6 都省時。"
                ),
                eats = listOf("元祖ニュータンタンメン本舗（在地靈魂拉麵）", "LAZONA 川崎的餐飲樓層"),
                warns = listOf("大浴場對刺青有規範。", "帶大行李走 JR 川崎站中央東口，電梯好找。"),
                official = "https://www.hotespa.net/hotels/kawasaki/"
            )
        )
    )
)

internal val day5 = Day(
    n = 5, date = "9/29", weekday = "週二",
    theme = "有明沉浸式．麻布台與東京鐵塔夜",
    summary = "配合皮克斯展 14:00 場重排；晚上補上 Day 1 沒去成的東京鐵塔，就在麻布台旁邊。",
    hotelName = "Dormy Inn 川崎（第 2 晚）",
    hotelNote = "回程：東京鐵塔→赤羽橋站→大江戶線→大門→JR→川崎，約 45 分，21:15 出發約 22:00 到。",
    alerts = listOf(
        "已預訂：皮克斯的世界展 9/29（二）14:00 入場，日時指定制，遲到可能無法入場。",
        "東京鐵塔由 Day 1 移到今晚，並依你的需求改成「拍照就走」：不上展望台，麻布台徒步 12 分直接到最佳機位。",
        "麻布台 33F Sky Lobby 自 2024/4 起停止一般開放，別當免費展望台。",
        "豐洲千客萬來已移到 Day 8 上午（早市時段才是它的精華）。"
    ),
    tips = listOf(
        "有明三館彼此徒步 10–15 分，上午先把 TOKYO DREAM PARK 走完，13:30 前移動到展場最保險。",
        "SMALL WORLDS 末入場 18:00，排在皮克斯展之後剛剛好。",
        "兩座塔都只拍照不上樓，今天回川崎的時間提前到 21:45，隔天橫濱才有體力。"
    ),
    stops = listOf(
        Stop("10:00",
            leg = Leg("電車", "川崎→JR→新橋，轉百合海鷗線→有明（徒步 3 分）", 50, "約 700 円"),
            spot = Spot(
                id = "d5_dreampark", nameZh = "TOKYO DREAM PARK（含 EX STUDIO7）", nameJa = "TOKYO DREAM PARK／EX STUDIO7",
                kana = "とうきょうドリームパーク", art = Art.DREAM, cat = Cat.THEME,
                area = "有明／江東區", lat = 35.6353, lng = 139.7930,
                stay = "3 小時", hours = "10:00–19:00（各展演依場次）",
                price = "入館免費，展演另購票", closed = "不定休",
                booking = "熱門展覽建議線上先買日時指定券",
                notes = listOf(
                    "2026/3/27 開幕，朝日電視台的 9 層複合娛樂設施。",
                    "EX STUDIO7 是館內約 870㎡ 展場，開幕檔為 100% 哆啦A夢展。",
                    "8F 光影劇場 RÊVE DES LUMIÈRES 平日約 2,800 円。",
                    "屋頂 DREAM TERRACE 免費，人少好拍。"
                ),
                eats = listOf("館內哆啦A夢聯名餐點", "有明 GARDEN 美食街（徒步 8 分）"),
                warns = listOf("展覽會換檔，出發前確認 9/29 在展什麼。", "13:15 一定要離開，才趕得上 14:00 的皮克斯展。"),
                official = "https://tdp.tv-asahi.co.jp/"
            )
        ),
        Stop("13:40",
            leg = Leg("步行", "有明地區徒步 10 分（建議 13:45 前抵達展場）", 10, "—"),
            spot = Spot(
                id = "d5_pixar", nameZh = "皮克斯的世界展（已訂 14:00）", nameJa = "ピクサーの世界展＠CREVIA BASE Tokyo",
                kana = "ピクサーのせかいてん", art = Art.EXHIBIT, cat = Cat.CULTURE,
                area = "有明／江東區", lat = 35.6362, lng = 139.7906,
                stay = "2 小時", hours = "10:00–20:50（末入場 19:00）",
                price = "約 2,000–3,000 円（依平日假日與時段）", closed = "原則每週一",
                booking = "已預訂 9/29 14:00 入場",
                notes = listOf(
                    "會期 2026/3/20–10/12，你去時接近尾聲、人潮偏多，14:00 場提早 10 分到排隊最順。",
                    "巡迴 7 國 9 城、350 萬人的沉浸式體驗展，走進等身大場景。",
                    "和 Day 3 玩具總動員飯店、Day 4 迪士尼連成一條皮克斯線。",
                    "出場前的限定商店是全程唯一買得到展覽周邊的地方。"
                ),
                eats = listOf("展場限定咖啡廳角色甜點"),
                warns = listOf("日時指定制，遲到可能無法入場。", "部分展區禁閃光燈與自拍棒。"),
                official = "https://mundopixar.jp/"
            )
        ),
        Stop("16:10",
            leg = Leg("步行", "有明地區徒步 12 分（末入場 18:00）", 12, "—"),
            spot = Spot(
                id = "d5_smallworlds", nameZh = "SMALL WORLDS TOKYO", nameJa = "スモールワールズTOKYO",
                kana = "スモールワールズトーキョー", art = Art.MINIATURE, cat = Cat.CULTURE,
                area = "有明／江東區", lat = 35.6320, lng = 139.7960,
                stay = "2 小時", hours = "9:00–19:00（末入場 18:00）",
                price = "大人 2,700 円起、國高中 1,900 円、兒童 1,500 円", closed = "無公休日",
                notes = listOf(
                    "世界最大級室內微縮樂園，1/80 比例八大區。",
                    "重點是「會動」：火箭發射、飛機起降、24 分一次的晝夜循環，入口先拿演出時刻表。",
                    "EVA 區第三新東京市會整個升起，全館必看。",
                    "可付費做自己的 1/80 人偶「住民權」（約 9,900 円起，需預約）。"
                ),
                eats = listOf("館內咖啡廳聯名餐點"),
                warns = listOf("可拍照但禁腳架與閃光燈。", "18:15 離開，接下來還有兩站。"),
                official = "https://www.smallworlds.jp/"
            )
        ),
        Stop("19:00",
            leg = Leg("電車", "有明→百合海鷗線→新橋，轉日比谷線→神谷町（直結）", 40, "約 600 円"),
            spot = Spot(
                id = "d5_azabudai", nameZh = "麻布台之丘 森JP Tower", nameJa = "麻布台ヒルズ 森JPタワー",
                kana = "あざぶだいヒルズ", art = Art.HILLS, cat = Cat.NIGHT,
                area = "麻布台／港區", lat = 35.6620, lng = 139.7420,
                stay = "1 小時", hours = "戶外中央廣場 24 小時；商業區約 11:00–20:00、餐飲至 23:00",
                price = "戶外免費；teamLab Borderless 約 3,800 円起", closed = "不定休",
                booking = "teamLab Borderless 需線上預約",
                notes = listOf(
                    "33F Sky Lobby 已不開放一般客，別跑錯期待。",
                    "免費好拍的是 Heatherwick 設計的戶外中央廣場，綠地＋東京鐵塔同框。",
                    "1F Market 有 34 間食材店，晚餐可在這裡解決。",
                    "看完直接徒步 12 分到東京鐵塔，是今晚最順的接法。"
                ),
                eats = listOf("Azabudai Hills Market 熟食與麵包"),
                warns = listOf("商業區 20:00 打烊，想買東西要在 19:45 前。"),
                official = "https://www.azabudai-hills.com/"
            )
        ),
        Stop("20:15",
            leg = Leg("步行", "麻布台之丘→東京鐵塔 徒步 12 分（沿櫻田通り）", 12, "—"),
            spot = Spot(
                id = "d1_tokyotower", nameZh = "東京鐵塔", nameJa = "東京タワー",
                kana = "とうきょうタワー", art = Art.TOWER, cat = Cat.VIEW,
                area = "芝公園／港區", lat = 35.6586, lng = 139.7454,
                stay = "40 分（純拍照，不上塔）", hours = "塔外 24 小時可拍；主甲板 9:00–23:00",
                price = "外拍免費（上主甲板才需約 1,500 円）",
                closed = "全年無休", booking = "不上塔就不需要買票",
                notes = listOf(
                    "免費經典機位：芝公園 4 號地草坪（塔全身）、增上寺大殿前（寺院＋塔同框）。",
                    "赤羽橋交叉口是 IG 最常見的角度，站在斑馬線邊就能拍到完整塔身。",
                    "橘色經典燈色到 22:00，之後轉較暗的深夜版本，想拍請在 22:00 前。",
                    "上主甲板要排隊約 30–40 分，今晚只拍照最划算。"
                ),
                eats = listOf("芝大門一帶的老舖蕎麥"),
                warns = listOf("21:15 前離開，回川崎約 45 分。", "夜間三腳架在人行道會擋路，手持高 ISO 即可。"),
                official = "https://www.tokyotower.co.jp/"
            )
        )
    )
)
