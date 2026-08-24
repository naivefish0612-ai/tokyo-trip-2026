# 加密行程網站

一份私人旅行行程的靜態網站。內容以 AES-256-GCM 加密後才進入這個公開 repo，
在瀏覽器端輸入密碼解密。

**線上位置**：https://naivefish0612-ai.github.io/tokyo-trip-2026/

## 這個 repo 裡有什麼

```
web/            原始檔：index.html、style.css、app.js、hero.svg、build.py、photos/
docs/           建置產物，GitHub Pages 由此發布（內容皆為密文或無意義檔名）
trip-data.json  行程資料 —— 明文，不在版控內
web/.passphrase 網站密碼 —— 不在版控內
```

`trip-data.json` 與 `web/.passphrase` 是私有資料，`.gitignore` 已排除。
**絕不要提交它們**：這個 repo 是公開的，明文行程等於對外公告不在家的日期與每晚住宿。

`web/photos/` 的檔名是無意義編號（`01.jpg`…），對應關係只存在於 `trip-data.json`
與加密後的輸出中，因此照片留在公開 repo 不會洩漏行程結構。

## 建置

只需要 Python 3 與 `cryptography`：

```bash
pip install cryptography
python3 web/build.py --passphrase "$(cat web/.passphrase)"
git add docs && git commit -m "..." && git push
```

GitHub Pages 會自動重新部署，約 1–2 分鐘生效。

密碼務必沿用既有的，否則使用者已儲存的密碼會失效。
`build.py` 在下列情況會直接讓建置失敗：

- 景點缺必要欄位、座標不合理、沒有筆記、找不到照片
- 景點 id 重複，或與行前清單 id 衝突
- `app.js` 用到的 CSS class 在 `style.css` 中沒有定義

## 移植到另一台機器

網站由 GitHub Pages 代管，**沒有伺服器需要搬**。要搬的只有建置環境：

```bash
git clone https://github.com/naivefish0612-ai/tokyo-trip-2026.git
cd tokyo-trip-2026
pip install cryptography
```

然後從原機器**手動複製**這兩項過去（不要經由 GitHub 或任何第三方服務）：

```
trip-data.json      → 專案根目錄
web/.passphrase     → web/ 下
```

推送需要一組具 Contents 寫入權限的 GitHub PAT。完成後跑一次建置確認輸出一致即可。

### 選配：視覺驗證

`app.js` 的版面問題無法用靜態檢查抓出來（例如 CSP 曾封鎖所有 inline style，
導致圖片以原生尺寸撐破版面）。若要在改動 UI 後做視覺驗證：

```bash
npm install playwright && npx playwright install chromium
cd docs && python3 -m http.server 8765 &
# 以手機視窗尺寸開啟、解鎖、逐頁截圖，並量測每張圖片與容器的比例
```

## 安全設計

- 行程資料以 **AES-256-GCM** 加密，金鑰由 **PBKDF2-SHA256（600,000 次迭代）** 推導，
  僅在瀏覽器記憶體中解密。GCM 會偵測密文遭竄改。
- 照片檔名為以密碼推導的 salt 加鹽雜湊。
- 純靜態、無後端，不存在 SQL 注入、RCE、權限繞過等整類威脅。
- CSP 僅允許同源資源，無任何第三方相依（不引用 CDN、字型、分析工具）。
- DOM 全以 `createElement`／`textContent` 建構，不使用 `innerHTML`。
- `frame-ancestors` 經 `<meta>` 依規格無效，點擊劫持由 `app.js` 開頭的 frame guard 處理。
- 全站 `noindex` 且 `robots.txt` 禁止索引。

**密碼強度即為全部防線**：密文可公開下載並離線暴力破解，勿改用好記的短密碼。

### 已知限制

2026/8/21–8/24 期間，行程資料曾以 Kotlin 原始碼形式明文存在於本 repo。
目前版本已移除，但**仍留在 git 歷史中**，可透過舊 commit 讀取。
徹底清除需改寫歷史（`git filter-repo`）並強制推送。

## 授權

照片取自 Wikimedia Commons，皆為 CC 或公有領域授權，作者與授權條款列於各景點頁。
