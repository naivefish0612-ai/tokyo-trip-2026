package com.tokyo2026.trip.data

internal val day6 = Day(
    n = 6, date = "9/30", weekday = "週三",
    theme = "湘南．橫濱港灣",
    summary = "由西往東一路推進：江之島→八景島→港未來，最後移動到大井町。",
    hotelName = "VIA INN 東京大井町",
    hotelNote = "JR 大井町站中央口徒步 3 分。入住 15:00／退房 10:00。三線交會，最後兩晚位置最好。",
    alerts = listOf(
        "橫濱地標大廈 69F Sky Garden 自 2025/12/31 起整修停業，預計 2028 年後重開——當天無法登頂。",
        "替代方案已排入：The Tower 橫濱北仲 46F 免費展望台，9:00–22:00、360 度。",
        "COSMOWORLD 每週四公休，9/30 週三正常營業（平日 11:00–21:00）。"
    ),
    tips = listOf(
        "今天是全程最滿的一天，三地移動合計約 2.5 小時。想輕鬆就二擇一：海洋派砍江之島、風景派砍八景島。",
        "全跑法：江之島 09:00–12:30 → 八景島 13:45–17:00（只逛水族館不玩遊具）→ 港未來 18:00–21:00。",
        "從川崎出發走 JR 東海道線到藤澤最直接，不必繞新宿買周遊券。"
    ),
    stops = listOf(
        Stop("08:00",
            leg = Leg("電車", "川崎→JR 東海道線→藤澤，轉江之電→江之島站（徒步 15 分過弁天橋）", 60, "約 700 円"),
            spot = Spot(
                id = "d6_enoshima", nameZh = "江之島 Sea Candle", nameJa = "江の島シーキャンドル",
                kana = "えのしま シーキャンドル", art = Art.ISLAND, cat = Cat.NATURE,
                area = "藤澤市／神奈川縣", lat = 35.2996, lng = 139.4801,
                stay = "3.5 小時", hours = "9:00–20:00（末入場 19:30）",
                price = "Sea Candle 套票（含 Escar 手扶梯）大人 1,100 円、兒童 550 円",
                closed = "全年無休（強風時燈塔關閉）",
                notes = listOf(
                    "全島是陡坡，Escar 收費電扶梯只往上，省 20 分鐘爬坡。",
                    "順序：邊津宮→中津宮→Sea Candle→奧津宮→龍戀之鐘→稚兒之淵。",
                    "回程從岩屋搭觀光船「べんてん丸」回弁天橋，省大量腳程。",
                    "天氣好可同時看到富士山與伊豆半島，秋天機率高。"
                ),
                eats = listOf("丸焼きたこせんべい", "しらす丼（吻仔魚）", "貝殼燒"),
                warns = listOf("階梯極多，務必穿好走的鞋。", "岩屋洞窟另購 500 円，風浪大時封閉。"),
                official = "https://enoshima-seacandle.com/"
            )
        ),
        Stop("13:00",
            leg = Leg("電車", "江之島→江之電→藤澤→JR→橫濱→京急→金澤八景→海濱線→八景島", 75, "約 1,000 円"),
            spot = Spot(
                id = "d6_hakkeijima", nameZh = "橫濱八景島海島樂園", nameJa = "横浜・八景島シーパラダイス",
                kana = "はっけいじま シーパラダイス", art = Art.AQUARIUM, cat = Cat.THEME,
                area = "金澤區／橫濱市", lat = 35.3372, lng = 139.6437,
                stay = "3 小時", hours = "全島 8:30–22:30；館內與遊具多為 9:00–21:00",
                price = "One Day Pass 大人 5,700 円；只逛水族館 4 館較便宜", closed = "全年無休",
                notes = listOf(
                    "入島免費，時間不夠就只買「水族館 4 館入館券」。",
                    "アクアミュージアム 的 5 萬條沙丁魚大群迴游是招牌。",
                    "ドルフィン ファンタジー 是海豚在頭頂游的拱形水槽。",
                    "リヴァイアサン 雲霄飛車有一段衝出海面。"
                ),
                eats = listOf("うみファーム 現釣現烤竹筴魚", "島上海鮮咖哩"),
                warns = listOf("2026/8/1 起部分票種調價，購票前確認官網。", "上午若拖到 13:00 還沒離開江之島，就果斷放棄這站。"),
                official = "https://www.seaparadise.co.jp/"
            )
        ),
        Stop("17:45",
            leg = Leg("電車", "八景島→海濱線→金澤八景→京急→橫濱→港未來線→港未來（徒步 5 分）", 50, "約 800 円"),
            spot = Spot(
                id = "d6_cosmoworld", nameZh = "橫濱 COSMOWORLD", nameJa = "よこはまコスモワールド",
                kana = "よこはまコスモワールド", art = Art.PARK, cat = Cat.THEME,
                area = "港未來／橫濱市", lat = 35.4551, lng = 139.6363,
                stay = "1 小時", hours = "平日 11:00–21:00；週六日假日 11:00–22:00",
                price = "入園免費，各設施單獨計費", closed = "每週四",
                notes = listOf(
                    "入園免費，只想散步拍照完全不花錢。",
                    "招牌是衝進水裡的潛水艇 Vanish 與垂直落下 Diving Coaster。",
                    "週三晚上幾乎不用排隊，是最舒服的時段。"
                ),
                eats = listOf("隔壁 World Porters 美食街"),
                warns = listOf("單次購票，回數券較划算。", "雨天部分水上設施停開。"),
                official = "https://cosmoworld.jp/"
            )
        ),
        Stop("18:45",
            leg = Leg("步行", "COSMOWORLD 園內", 2, "—"),
            spot = Spot(
                id = "d6_cosmoclock", nameZh = "摩天輪 COSMOCLOCK 21", nameJa = "大観覧車 コスモクロック21",
                kana = "コスモクロック21", art = Art.FERRIS, cat = Cat.NIGHT,
                area = "港未來／橫濱市", lat = 35.4553, lng = 139.6367,
                stay = "30 分", hours = "同 COSMOWORLD（平日至 21:00）",
                price = "1 人 1,000 円（1 圈約 15 分）", closed = "每週四",
                notes = listOf(
                    "高 112.5m，輪體本身就是世界最大的鐘，整點有燈光演出。",
                    "先在外面看一輪燈光秀再上去坐，體驗最完整。",
                    "有 4 台透明車廂，人少時可指定。",
                    "拍摩天輪＋地標塔的機位在汽車道與萬國橋。"
                ),
                warns = listOf("強風停駛。", "透明車廂怕高者慎入。"),
                official = "https://cosmoworld.jp/"
            )
        ),
        Stop("19:30",
            leg = Leg("步行", "港未來→馬車道方向徒步 12 分", 12, "—"),
            spot = Spot(
                id = "d6_kitanaka", nameZh = "The Tower 橫濱北仲 46F 展望台", nameJa = "ザ・タワー横浜北仲 46階展望フロア",
                kana = "よこはまきたなか", art = Art.VIEW, cat = Cat.VIEW,
                area = "北仲通／橫濱市中區", lat = 35.4520, lng = 139.6367,
                stay = "45 分", hours = "9:00–22:00", price = "免費", closed = "無",
                notes = listOf(
                    "橫濱最強免費夜景：46 樓 360 度，摩天輪與地標塔盡收眼底。",
                    "入口在大樓南東側磚柱造型自動門，外觀完全看不出是展望台。",
                    "2026/1 起以星野 OMO5 橫濱馬車道的公共空間 OMO BASE 形式開放，非住客可入。",
                    "同層有餐廳，不消費站著看也可以。"
                ),
                warns = listOf("是住宅兼飯店大樓，請勿喧嘩、勿用腳架。"),
                official = "https://omo-hotels.com/yokohama/"
            )
        ),
        Stop("20:30",
            leg = Leg("步行", "北仲→櫻木町站方向徒步 8 分", 8, "—"),
            spot = Spot(
                id = "d6_landmark", nameZh = "橫濱地標大廈（展望台休館）", nameJa = "横浜ランドマークタワー",
                kana = "よこはまランドマークタワー", art = Art.VIEW, cat = Cat.SHOP,
                area = "港未來／橫濱市西區", lat = 35.4548, lng = 139.6317,
                stay = "30 分（逛商場與外拍）", hours = "商場 11:00–20:00、餐飲至 22:00",
                price = "商場免費；69F Sky Garden 休館中", closed = "69F 展望台 2025/12/31 起休館，預計 2028 年後重開",
                notes = listOf(
                    "展望台雖關閉，塔樓仍是橫濱天際線主角，汽車道步道拍夜景不受影響。",
                    "LANDMARK PLAZA 與舊船塢遺構 Dockyard Garden 照常開放。",
                    "想在高處看夜景，時間全給前一站的北仲 46F（免費）。",
                    "皇家花園酒店 68–70F 有餐廳酒吧，消費可享同樣高度。"
                ),
                eats = listOf("Dockyard Garden 啤酒餐廳", "紅磚倉庫"),
                warns = listOf("務必先知道展望台關閉，避免白排隊。"),
                official = "https://yokohama-landmark.jp/"
            )
        ),
        Stop("21:15",
            leg = Leg("電車", "櫻木町→JR 根岸線／京濱東北線→大井町（中央口徒步 3 分）", 40, "約 570 円"),
            spot = Spot(
                id = "d6_viainn", nameZh = "VIA INN 東京大井町（入住）", nameJa = "ヴィアイン東京大井町",
                kana = "ヴィアインおおいまち", art = Art.HOTEL, cat = Cat.STAY,
                area = "大井町／品川區", lat = 35.6064, lng = 139.7345,
                stay = "住宿（2 晚）", hours = "入住 15:00／退房 10:00", price = "已預訂", closed = "—",
                notes = listOf(
                    "JR 東日本旗下，房間新、隔音佳。",
                    "大井町站是 JR 京濱東北線、東急大井町線、臨海線交會。",
                    "站前「東小路」是還留著的昭和飲み屋橫丁，深夜有串燒拉麵。",
                    "最後一天到成田：先到品川或日暮里都很快。"
                ),
                eats = listOf("東小路燒鳥橫丁", "阪急大井町熟食"),
                warns = listOf("部分房型不含早餐。", "退房 10:00 較早，前一晚先整理行李。"),
                official = "https://www.viainn.com/"
            )
        )
    )
)

internal val day7 = Day(
    n = 7, date = "10/1", weekday = "週四",
    theme = "哆啦A夢．招財貓．澀谷夜",
    summary = "改走川崎→登戶最短路線，補上池袋 LOFT，再一路往東收在澀谷 SKY。",
    hotelName = "VIA INN 東京大井町（第 2 晚）",
    hotelNote = "澀谷→大井町：JR 山手線至品川，轉京濱東北線 1 站，約 20 分。",
    alerts = listOf(
        "藤子・F・不二雄博物館為完全日時指定制，只在官網售 QR 票、館內不售當日票；每日 7 梯（10:00–16:00）。",
        "澀谷 SKY 為 20 分一梯的日時指定券；網路 2,700 円（–14:59）／3,400 円（15:00–），現場各多 300 円；末入場 21:20。",
        "池袋 LOFT 由 Day 2 移到今天：豪德寺→新宿→池袋順路，不用另跑一趟。",
        "都廳南展望室每月第 1、3 週二休室；10/1 週四正常開放。"
    ),
    tips = listOf(
        "去登戶不要繞新宿：大井町→川崎→JR 南武線→登戶只要約 50 分、560 円，比走小田急快 20 分。",
        "回程順序 登戶→豪德寺→新宿→澀谷 全在小田急與山手線上，完全不走回頭路。",
        "10 月初日落約 17:25：都廳先看夕陽，18:00 後在都民廣場看免費投影，再往歌舞伎町吃晚餐。"
    ),
    stops = listOf(
        Stop("09:20",
            leg = Leg("電車", "大井町→JR 京濱東北線→川崎，轉 JR 南武線→登戶，轉哆啦A夢接駁巴士", 55, "約 560 円＋巴士 220 円"),
            spot = Spot(
                id = "d7_fujiko", nameZh = "藤子・F・不二雄博物館", nameJa = "川崎市 藤子・F・不二雄ミュージアム",
                kana = "ふじこ・エフ・ふじお ミュージアム", art = Art.MUSEUM, cat = Cat.CULTURE,
                area = "多摩區／川崎市", lat = 35.6067, lng = 139.5626,
                stay = "3 小時", hours = "10:00–18:00；入館 7 梯（10:00–16:00）",
                price = "大人 1,000 円、國高中 700 円、兒童 500 円", closed = "每週二、年末年始",
                booking = "官網 e-tix QR 票，館內不售票",
                notes = listOf(
                    "登戶站的哆啦A夢彩繪接駁巴士本身就是第一個拍照點。",
                    "入館領「おはなしデンワ」語音導覽有中文版。",
                    "屋頂空地有水管三兄弟與任意門，人潮集中在梯次開始 30 分內。",
                    "3F 咖啡廳供應漫畫裡的食物，需館內抽號碼牌，越早越好。"
                ),
                eats = listOf("記憶吐司法式吐司", "哆啦A夢銅鑼燒"),
                warns = listOf("展示室禁攝影，只有屋頂可拍。", "票綁購買者姓名，入館核對。"),
                official = "https://fujiko-museum.com/"
            )
        ),
        Stop("13:30",
            leg = Leg("電車", "登戶→小田急小田原線→豪德寺，轉東急世田谷線→宮之坂（徒步 5 分）", 30, "約 280 円"),
            spot = Spot(
                id = "d7_gotokuji", nameZh = "世田谷 豪德寺", nameJa = "大谿山 豪徳寺",
                kana = "ごうとくじ", art = Art.SHRINE, cat = Cat.CULTURE,
                area = "世田谷區", lat = 35.6497, lng = 139.6479,
                stay = "1.5 小時", hours = "境內 6:00–17:00；寺務所 8:00–15:00",
                price = "免費；招福貓兒 500–5,000 円", closed = "無",
                notes = listOf(
                    "招財貓發祥地之一，這裡的貓叫「招福猫児」。",
                    "招福殿旁奉納所數千隻白貓層層疊疊，是東京最上鏡畫面之一。",
                    "貓右手抬起、不拿金幣——招的是緣不是錢。",
                    "御朱印與招福貓兒只在 8:00–15:00 領，下午 3 點後只能參拜。"
                ),
                eats = listOf("商店街招財貓最中", "松陰神社前的咖啡與麵包"),
                warns = listOf("是現役寺院與國指定史蹟墓所，勿喧嘩、勿用腳架。"),
                official = "https://gotokuji.jp/"
            )
        ),
        Stop("15:40",
            leg = Leg("電車", "宮之坂→世田谷線→山下，步行至豪德寺站→小田急→新宿，轉 JR 埼京線→池袋（東口）", 40, "約 430 円"),
            spot = Spot(
                id = "d2_loft", nameZh = "池袋 LOFT", nameJa = "池袋ロフト",
                kana = "いけぶくろロフト", art = Art.SHOP, cat = Cat.SHOP,
                area = "池袋／豐島區", lat = 35.7288, lng = 139.7132,
                stay = "45 分", hours = "週一至週六 10:00–21:00；週日假日 10:00–20:00",
                price = "免費入場", closed = "不定休",
                notes = listOf(
                    "現址在南池袋 LINKS 池袋 9–12F，出東口，不是舊西武別館。",
                    "強項是文具與生活雜貨，伴手禮 CP 值最高。",
                    "12F 常有動畫聯名快閃店。"
                ),
                warns = listOf("16:30 一定要離開，才接得上都廳的日落與投影。"),
                official = "https://www.loft.co.jp/"
            )
        ),
        Stop("17:00",
            leg = Leg("電車", "池袋→JR 埼京線→新宿（西口徒步 10 分）", 15, "180 円"),
            spot = Spot(
                id = "d7_tocho", nameZh = "東京都廳舍 展望室", nameJa = "東京都庁舎 展望室",
                kana = "とうきょうとちょう てんぼうしつ", art = Art.GOV, cat = Cat.VIEW,
                area = "西新宿／新宿區", lat = 35.6896, lng = 139.6921,
                stay = "1.5 小時", hours = "南 9:30–22:00；北 9:30–17:30（末入室閉室前 30 分）",
                price = "免費", closed = "南：每月第 1、3 週二；北：第 2、4 週一",
                notes = listOf(
                    "202m 高、完全免費，晴天可看到富士山，10 月機率變高。",
                    "外牆投影「TOKYO Night & Light」是金氏認定世界最大常設建築投影，免費免預約。",
                    "看投影站都民廣場即可，不必進大樓，日落後至 21:30 多場。",
                    "南展望室有咖啡吧可坐著看夜景。"
                ),
                eats = listOf("展望室咖啡吧", "西口思い出橫丁串燒（徒步 10 分）"),
                warns = listOf("需過安檢，旺季排 20–30 分。", "大型行李無法帶入。"),
                official = "https://www.yokoso.metro.tokyo.lg.jp/"
            )
        ),
        Stop("18:40",
            leg = Leg("步行", "都民廣場看完投影→新宿站→歌舞伎町徒步 15 分", 15, "—"),
            spot = Spot(
                id = "d7_kabukicho", nameZh = "歌舞伎町", nameJa = "歌舞伎町",
                kana = "かぶきちょう", art = Art.NIGHT, cat = Cat.NIGHT,
                area = "新宿區", lat = 35.6950, lng = 139.7020,
                stay = "1.5 小時", hours = "全時段（多數店 17:00 後熱鬧）",
                price = "散步免費", closed = "無",
                notes = listOf(
                    "必拍新宿東寶大樓的哥吉拉頭，從一番街正面仰拍。",
                    "東急歌舞伎町 TOWER B1「歌舞伎橫丁」24 小時、日本祭典風。",
                    "思い出橫丁與ゴールデン街在西側，後者多有 500–1,500 円座位費。"
                ),
                eats = listOf("歌舞伎橫丁鄉土料理", "思い出橫丁燒鳥"),
                warns = listOf("絕對不要跟路上拉客的人走，那在東京都條例是違法行為。", "深夜避開巷弄走大路。"),
                official = "https://www.kabukicho.or.jp/"
            )
        ),
        Stop("20:15",
            leg = Leg("電車", "新宿→JR 山手線→澀谷（徒步 3 分）", 8, "160 円"),
            spot = Spot(
                id = "d7_scramble", nameZh = "澀谷 Scramble 交叉路口", nameJa = "渋谷スクランブル交差点",
                kana = "しぶやスクランブルこうさてん", art = Art.CROSSING, cat = Cat.NIGHT,
                area = "澀谷區", lat = 35.6595, lng = 139.7005,
                stay = "30 分", hours = "24 小時", price = "免費", closed = "無",
                notes = listOf(
                    "一次綠燈最多約 3,000 人通過。",
                    "免費俯拍點：澀谷站 2F 連通道玻璃；付費最佳角度是 MAGNET 頂樓（600 円）。",
                    "慢速快門 1/4 秒可拍出人流殘影。",
                    "雨天反而最好拍：地面反光＋雨傘。"
                ),
                warns = listOf("路口中央不可停下拍照。", "扒手多，背包背前面。")
            )
        ),
        Stop("20:45",
            leg = Leg("步行", "澀谷站八公口徒步 1 分", 1, "—"),
            spot = Spot(
                id = "d7_hachiko", nameZh = "忠犬八公像", nameJa = "忠犬ハチ公像",
                kana = "ちゅうけんハチこう", art = Art.DOG, cat = Cat.CULTURE,
                area = "澀谷區", lat = 35.6590, lng = 139.7006,
                stay = "15 分", hours = "24 小時", price = "免費", closed = "無",
                notes = listOf(
                    "現在的銅像是 1948 年重鑄的第二代，初代在二戰時被熔掉。",
                    "舊青蛙電車已移到秋田縣大館市，現地沒有了，別找錯。",
                    "尖峰拍照要等 10–15 分，清晨 6 點前幾乎沒人。"
                ),
                warns = listOf("全澀谷最擠的集合點，注意隨身物品。")
            )
        ),
        Stop("21:00",
            leg = Leg("步行", "澀谷 Scramble Square 14F 售票口→45F", 5, "—"),
            spot = Spot(
                id = "d7_shibuyasky", nameZh = "澀谷 SKY", nameJa = "SHIBUYA SKY（渋谷スカイ）",
                kana = "しぶやスカイ", art = Art.SKY, cat = Cat.VIEW,
                area = "澀谷 Scramble Square", lat = 35.6580, lng = 139.7016,
                stay = "1.5 小時", hours = "10:00–22:30（末入場 21:20）",
                price = "網路 2,700 円（–14:59）／3,400 円（15:00–）；現場 3,000／3,700 円", closed = "強風雷雨時屋頂關閉",
                booking = "20 分一梯日時指定，日落梯建議一個月前買",
                notes = listOf(
                    "賣點是 46F 無遮蔽屋頂 SKY STAGE，230m 露天，正下方就是 Scramble 路口。",
                    "招牌是無邊際角落 SKY EDGE 與吊床 CLOUD HAMMOCK，兩者都要排隊。",
                    "屋頂只能帶手機（需繫掛繩），其他物品一律寄免費置物櫃。",
                    "選日落前 40 分入場，一張票看夕陽與夜景兩種。"
                ),
                eats = listOf("46F Paradise Lounge", "12–13F 餐廳樓層"),
                warns = listOf("強風雷雨關屋頂且不退票。", "屋頂風大，建議褲裝與好走的鞋。"),
                official = "https://www.shibuya-scramble-square.com/sky/"
            )
        )
    )
)

internal val day8 = Day(
    n = 8, date = "10/2", weekday = "週五",
    theme = "豐洲早市．CI 105 返程",
    summary = "退房後把最後半天給豐洲千客萬來，中午直接從市場前搭車進成田。",
    hotelName = "—（返程）",
    hotelNote = "VIA INN 東京大井町 退房 10:00。行李直接帶著走，千客萬來 1F 與市場前站都有大型置物櫃。",
    alerts = listOf(
        "CI 105 成田 NRT T2 17:55 → 台北 TPE 20:35。國際線建議 14:50 前完成報到，本行程抓 14:20 抵達航廈。",
        "豐洲千客萬來由 Day 5 移到今天上午——早市時段才是它的精華，海鮮丼名店多在 9–11 點最齊。",
        "萬葉俱樂部入館 11:00 起、泡湯至少 2 小時，今天時間不夠，只建議看屋上足湯庭園與吃早午餐。"
    ),
    tips = listOf(
        "路線：市場前→百合海鷗線→新橋→都營淺草線 Access 特急直通→空港第2ビル，約 80 分、約 1,400 円，不用換到 JR。",
        "備案：若想更保險，改從新橋轉 JR 到品川搭 N'EX（約 3,250 円、全車指定席、行李空間大）。",
        "伴手禮在千客萬來的江戶前市場買最有特色（海鮮仙貝、玉子燒、和菓子），機場只補買免稅品。"
    ),
    stops = listOf(
        Stop("10:20",
            leg = Leg("電車", "大井町→JR 京濱東北線→新橋，轉百合海鷗線→市場前（徒步 1 分）", 40, "約 570 円"),
            spot = Spot(
                id = "d3_senkyaku", nameZh = "豐洲千客萬來", nameJa = "豊洲 千客万来",
                kana = "とよす せんきゃくばんらい", art = Art.MARKET, cat = Cat.FOOD,
                area = "豐洲／江東區", lat = 35.6450, lng = 139.7856,
                stay = "2 小時", hours = "食樂棟約 9:00–21:00（各店異）；萬葉俱樂部入館 11:00–翌 2:00",
                price = "入場免費；萬葉俱樂部一日入館約 3,850 円", closed = "食樂棟依豐洲市場休市日",
                notes = listOf(
                    "與豐洲市場相連，重現江戶町屋的街景，上午最有市場氣氛。",
                    "海鮮丼偏觀光價（3,000 円起），在地人多單點玉子燒與海膽軍艦。",
                    "屋上足湯庭園可看東京灣與彩虹大橋，屬萬葉俱樂部、11:00 起。",
                    "10/2 為週五，市場正常營業；若遇臨時休市，改到豐洲 LaLaport 打發時間。"
                ),
                eats = listOf("鮪魚中落丼", "築地系玉子燒、炸海鮮串", "海鮮仙貝伴手禮"),
                warns = listOf("12:40 一定要離開，才留得住 3 小時的機場緩衝。", "大行李先寄放置物櫃再逛。"),
                official = "https://www.toyosu-senkyakubanrai.jp/"
            )
        ),
        Stop("12:45",
            leg = Leg("電車", "市場前→百合海鷗線→新橋，轉都營淺草線 Access 特急直通→空港第2ビル", 80, "約 1,400 円"),
            spot = Spot(
                id = "d8_narita", nameZh = "成田國際機場 第2航廈（CI 105）", nameJa = "成田国際空港 第2ターミナル",
                kana = "なりたくうこう だい2ターミナル", art = Art.AIRPORT, cat = Cat.STAY,
                area = "千葉縣成田市", lat = 35.7736, lng = 140.3853,
                stay = "3 小時 30 分", hours = "航廈 24 小時（部分店舖 6:00–21:00）",
                price = "交通費 1,400–3,250 円", closed = "—",
                notes = listOf(
                    "CI 105 17:55 起飛、20:35 抵台北，第 2 航廈出發。",
                    "Access 特急直達不用換車；N'EX 需從品川搭、較貴但有指定席。",
                    "第 2 航廈 4F 有觀景台可看飛機，時間有餘可以上去。",
                    "免稅店的抹茶與 KitKat 限定口味是最後補貨點。"
                ),
                eats = listOf("航廈壽司與拉麵", "成田市場的最後一餐"),
                warns = listOf("退稅品需保留封條，出境可能查驗。", "報到與安檢尖峰約 40 分，14:50 前完成報到。"),
                official = "https://www.narita-airport.jp/"
            )
        )
    )
)
