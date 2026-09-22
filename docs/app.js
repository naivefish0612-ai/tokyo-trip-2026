/* 東京2026 行程 — 純靜態、無後端、無第三方相依。
 *
 * 行程資料以 AES-256-GCM 加密存放，金鑰由 PBKDF2-SHA256 從密碼推導，
 * 僅在瀏覽器記憶體中解密。所有 DOM 以 createElement／textContent 建構，
 * 不使用 innerHTML，因此資料內容無法被當成標記或指令碼執行。
 */
'use strict';

/* 防點擊劫持。CSP 的 frame-ancestors 只有透過 HTTP 標頭才有效，
 * 而 GitHub Pages 不允許自訂標頭，因此改在此處擋下被嵌入的情況。 */
if (window.top !== window.self) {
  document.documentElement.textContent = '';
  throw new Error('framed');
}

var TRIP = null;
var KEY_PREFIX = 'tokyo2026:';

/* ---------- DOM 工具（XSS-safe by construction） ---------- */

/* 透過 CSSOM 逐項套用樣式。
 * 不可改用 setAttribute('style', ...)：CSP 的 style-src 'self' 會封鎖 style 屬性，
 * 導致所有動態樣式失效、圖片以原生尺寸撐破版面。CSSOM 則不受該指令限制。 */
function setStyle(node, css) {
  css.split(';').forEach(function (decl) {
    var i = decl.indexOf(':');
    if (i < 0) return;
    var prop = decl.slice(0, i).trim();
    var val = decl.slice(i + 1).trim();
    if (prop) node.style.setProperty(prop, val);
  });
}

function el(tag, attrs, kids) {
  var n = document.createElement(tag);
  if (attrs) Object.keys(attrs).forEach(function (k) {
    var v = attrs[k];
    if (v === null || v === undefined || v === false) return;
    if (k === 'class') n.className = v;
    else if (k === 'text') n.textContent = v;
    else if (k === 'style') setStyle(n, v);
    else if (k.slice(0, 2) === 'on') n.addEventListener(k.slice(2), v);
    else n.setAttribute(k, v === true ? '' : v);
  });
  (kids || []).forEach(function (c) {
    if (c === null || c === undefined || c === false) return;
    n.appendChild(typeof c === 'string' ? document.createTextNode(c) : c);
  });
  return n;
}
function svgIcon(path, cls) {
  var ns = 'http://www.w3.org/2000/svg';
  var s = document.createElementNS(ns, 'svg');
  s.setAttribute('viewBox', '0 0 24 24');
  if (cls) s.setAttribute('class', cls);
  s.setAttribute('aria-hidden', 'true');
  var p = document.createElementNS(ns, 'path');
  p.setAttribute('d', path);
  s.appendChild(p);
  return s;
}
var ICON = {
  back: 'M20 11H7.8l5.6-5.6L12 4l-8 8 8 8 1.4-1.4L7.8 13H20v-2z',
  heart: 'M12 21s-8-4.9-8-10.4A4.6 4.6 0 0 1 12 7a4.6 4.6 0 0 1 8 3.6C20 16.1 12 21 12 21z',
  heartOff: 'M12 21s-8-4.9-8-10.4A4.6 4.6 0 0 1 12 7a4.6 4.6 0 0 1 8 3.6C20 16.1 12 21 12 21zm0-2.7c2.3-1.6 6-4.8 6-7.7A2.6 2.6 0 0 0 12 9.4 2.6 2.6 0 0 0 6 10.6c0 2.9 3.7 6.1 6 7.7z',
  check: 'M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm0 2a8 8 0 1 1 0 16 8 8 0 0 1 0-16zm-1.2 11.4L7 11.6l1.4-1.4 2.4 2.4 4.8-4.8L17 9.2z',
  circle: 'M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm0 2a8 8 0 1 1 0 16 8 8 0 0 1 0-16z',
  nav: 'M12 2 3 21l9-4 9 4L12 2z',
  map: 'M20 4l-6 2-4-2-6 2v14l6-2 4 2 6-2V4zm-6 14.5-4-2V5.5l4 2v11z',
  web: 'M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm7.9 9h-3a15 15 0 0 0-1.2-5.2A8 8 0 0 1 19.9 11zM12 4.2c.8 1.1 1.5 3 1.7 6.8h-3.4c.2-3.8.9-5.7 1.7-6.8zM4.1 13h3a15 15 0 0 0 1.2 5.2A8 8 0 0 1 4.1 13zm3-2h-3a8 8 0 0 1 4.2-5.2A15 15 0 0 0 7.1 11zm4.9 8.8c-.8-1.1-1.5-3-1.7-6.8h3.4c-.2 3.8-.9 5.7-1.7 6.8zm3.7-.6a15 15 0 0 0 1.2-5.2h3a8 8 0 0 1-4.2 5.2z',
  chev: 'M12 15.4 6.6 10 8 8.6l4 4 4-4L17.4 10z',
  tick: 'M9 16.2 4.8 12l-1.4 1.4L9 19 21 7l-1.4-1.4z',
  clock: 'M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm0 2a8 8 0 1 1 0 16 8 8 0 0 1 0-16zm1 3h-2v6l4.4 2.6.9-1.6-3.3-1.9z',
  yen: 'M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm0 2a8 8 0 1 1 0 16 8 8 0 0 1 0-16zM8.4 7h1.9l1.7 2.9L13.7 7h1.9l-2.2 3.7h1.7v1.2h-2.3v.9h2.3v1.2h-2.3V16h-1.6v-2h-2.3v-1.2h2.3v-.9H8.6v-1.2h1.7L8.4 7z',
  hourglass: 'M6 2h12v5l-4 5 4 5v5H6v-5l4-5-4-5V2zm2.6 3.6L12 9.4l3.4-3.8H8.6zM12 14.6l-3.4 3.8h6.8L12 14.6z',
  closed: 'M7 2v2H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2h-3V2h-2v2H9V2H7zm13 8v10H4V10h16zM8.4 12 7 13.4 10.6 17 17 10.6 15.6 9.2l-5 5-2.2-2.2z',
  ticket: 'M4 5h16a1 1 0 0 1 1 1v3.5a2.5 2.5 0 0 0 0 5V18a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1v-3.5a2.5 2.5 0 0 0 0-5V6a1 1 0 0 1 1-1zm10 2v2h2V7h-2zm0 4v2h2v-2h-2zm0 4v2h2v-2h-2z',
  bulb: 'M12 2a7 7 0 0 0-4 12.7V17a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1v-2.3A7 7 0 0 0 12 2zM9.5 20h5a1 1 0 0 1 0 2h-5a1 1 0 0 1 0-2z',
  food: 'M7 2v8a3 3 0 0 0 2 2.8V22h2V12.8A3 3 0 0 0 13 10V2h-1.6v6.4h-1.2V2H8.6v6.4H7.4V2H7zm10 0c-1.7 0-3 2.7-3 6 0 2.5.8 4.4 2 5.2V22h2V2h-1z',
  warn: 'M12 2 1.5 20.5h21L12 2zm0 5 6.6 11.5H5.4L12 7zm-1 3.6v4.2h2v-4.2h-2zm0 5.4v2h2v-2h-2z',
  alert: 'M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm0 2a8 8 0 1 1 0 16 8 8 0 0 1 0-16zm-1 3h2v6h-2V7zm0 8h2v2h-2v-2z',
  close: 'M19 6.4 17.6 5 12 10.6 6.4 5 5 6.4 10.6 12 5 17.6 6.4 19 12 13.4 17.6 19 19 17.6 13.4 12z'
};

/* 提示類別：每一類有自己的圖示與色系，取代先前的「！」「◆」裸字元 */
var KIND = {
  note:  { icon: ICON.bulb,  color: 'var(--tertiary)', label: '在地達人筆記' },
  eat:   { icon: ICON.food,  color: 'var(--eat)',      label: '必吃必買' },
  warn:  { icon: ICON.warn,  color: 'var(--secondary)', label: '注意與避雷' },
  tip:   { icon: ICON.bulb,  color: 'var(--tertiary)', label: '今日小訣竅' },
  alert: { icon: ICON.alert, color: 'var(--secondary)', label: '今日提醒' },
  fork:  { icon: ICON.nav,   color: 'var(--primary)',  label: '今日分岔點' }
};

/* ---------- 本機狀態（打卡／收藏） ---------- */
var Store = {
  get: function (id) { return localStorage.getItem(KEY_PREFIX + id) === '1'; },
  set: function (id, v) {
    if (v) localStorage.setItem(KEY_PREFIX + id, '1');
    else localStorage.removeItem(KEY_PREFIX + id);
  },
  toggle: function (id) { var v = !this.get(id); this.set(id, v); return v; },
  isFav: function (id) { return this.get('fav_' + id); },
  /* 分岔點存的不是有／沒有，而是選了哪個代號，所以要另外一組存取 */
  pick: function (id) { return localStorage.getItem(KEY_PREFIX + id) || ''; },
  setPick: function (id, v) {
    if (v) localStorage.setItem(KEY_PREFIX + id, v);
    else localStorage.removeItem(KEY_PREFIX + id);
  },
  count: function (ids) { var s = this, n = 0; ids.forEach(function (i) { if (s.get(i)) n++; }); return n; }
};

/* ---------- 解密 ---------- */
function b64ToBytes(b64) {
  var bin = atob(b64), a = new Uint8Array(bin.length);
  for (var i = 0; i < bin.length; i++) a[i] = bin.charCodeAt(i);
  return a;
}
async function decryptTrip(passphrase) {
  var res = await fetch('data.enc.json', { cache: 'no-store' });
  if (!res.ok) throw new Error('無法載入資料檔');
  var blob = await res.json();
  var enc = new TextEncoder();
  var base = await crypto.subtle.importKey('raw', enc.encode(passphrase), 'PBKDF2', false, ['deriveKey']);
  var key = await crypto.subtle.deriveKey(
    { name: 'PBKDF2', salt: b64ToBytes(blob.salt), iterations: blob.iterations, hash: 'SHA-256' },
    base, { name: 'AES-GCM', length: 256 }, false, ['decrypt']
  );
  // GCM 會驗證完整性：密碼錯誤或密文被竄改都會在這裡丟出例外。
  var plain = await crypto.subtle.decrypt(
    { name: 'AES-GCM', iv: b64ToBytes(blob.iv) }, key, b64ToBytes(blob.ct)
  );
  return JSON.parse(new TextDecoder().decode(plain));
}

/* ---------- 共用片段 ---------- */
function photoEl(spot, cls) {
  if (spot.photo) {
    return el('img', { src: spot.photo, alt: spot.nameZh, loading: 'lazy', decoding: 'async', class: cls || null });
  }
  if (spot.illustration) {
    return handDrawnIllustration(spot.illustration, cls);
  }
  var hue = 0; for (var i = 0; i < spot.id.length; i++) hue = (hue * 31 + spot.id.charCodeAt(i)) % 360;
  return el('div', {
    class: (cls ? cls + ' ' : '') + 'noimg',
    style: 'background:linear-gradient(150deg,hsl(' + hue + ',42%,38%),hsl(' + ((hue + 45) % 360) + ',48%,58%))'
  });
}

/** 找不到合適實景照的景點，用手繪風格插畫取代純色背景（見 ILLUSTRATIONS）。 */
var ILLUSTRATIONS = {
  /* TOKYO DREAM PARK：階梯狀的複合娛樂大樓、屋頂天線與煙火般的星芒。 */
  dreampark: [
    'M8,88 L92,88',
    'M22,88 L22,58 L54,58 L54,88',
    'M30,58 L30,36 L46,36 L46,58',
    'M34,36 L34,20 L42,20 L42,36',
    'M34,20 L42,20',
    'M38,20 L38,10',
    'M38,13 L20,2', 'M38,13 L56,2',
    'M70,16 L72,22 L78,23 L72,24 L70,30 L68,24 L62,23 L68,22 Z',
    'M62,50 L64,55 L69,56 L64,57 L62,62 L60,57 L55,56 L60,55 Z'
  ],
  /* 皮克斯的世界展：可走進去的畫框世界、票根與滿場星芒，不描繪任何角色。 */
  pixar: [
    'M22,18 h44 a6,6 0 0 1 6,6 v42 a6,6 0 0 1 -6,6 h-44 a6,6 0 0 1 -6,-6 v-42 a6,6 0 0 1 6,-6 Z',
    'M38,38 C48,34 58,40 54,48 C51,54 42,52 44,46 C45,42 50,42 50,45',
    'M30,30 L32,35 L37,36 L32,37 L30,42 L28,37 L23,36 L28,35 Z',
    'M65,54 L67,59 L72,60 L67,61 L65,66 L63,61 L58,60 L63,59 Z',
    'M14,76 h34 v16 h-34 Z',
    'M31,76 v16'
  ],
  /* 品川サウナ：サウナストーブ 與上頭的サウナストーン、蒸氣，旁邊是桶與柄杓。 */
  sauna: [
    'M10,86 h80',
    'M30,52 h28 v34 h-28 Z',
    'M30,62 h28',
    'M33,52 a6,5 0 0 1 12,0', 'M44,52 a5,4 0 0 1 10,0',
    'M38,44 q5,-6 0,-12', 'M50,44 q5,-6 0,-12',
    'M64,64 h24 l-3,22 h-18 Z',
    'M66,64 q10,-12 20,0',
    'M70,54 L86,40', 'M84,36 a4,4 0 1 1 5,5 Z'
  ],
  /* 都廳燈光秀：第一本庁舎的雙塔輪廓、東側牆面的光束與投射機，不畫任何作品畫面。 */
  mapping: [
    'M8,92 L92,92',
    'M32,92 L32,46 L68,46 L68,92',
    'M36,46 L36,16 L46,16 L46,46',
    'M54,46 L54,16 L64,16 L64,46',
    'M36,28 L46,28', 'M54,28 L64,28',
    'M32,62 L68,62',
    'M46,46 L54,46',
    'M10,88 h8 v6 h-8 Z',
    'M18,88 L32,70', 'M18,92 L32,80',
    'M50,52 L52,57 L57,58 L52,59 L50,64 L48,59 L43,58 L48,57 Z'
  ],
  /* 千成もなか：現烤どら焼き 的上下兩片皮與中間的餡，冒著熱氣，底下是茶托。 */
  dorayaki: [
    'M22,52 a28,17 0 0 1 56,0',
    'M22,52 q14,7 28,7 q14,0 28,-7',
    'M22,58 a28,17 0 0 0 56,0',
    'M40,28 q6,-7 0,-14', 'M54,28 q6,-7 0,-14',
    'M14,80 h72',
    'M24,80 q26,10 52,0'
  ],
  /* 旅するマグネット：一塊畫著風景的方形磁磚，右上角翹起，旁邊一顆星芒。 */
  magnet: [
    'M26,26 h48 v48 h-48 Z',
    'M32,32 h36 v36 h-36 Z',
    'M35,62 L47,44 L55,54 L61,47 L66,62 Z',
    'M62,39 a5,5 0 1 1 -0.1,0 Z',
    'M74,26 L86,14',
    'M14,18 L16,24 L22,26 L16,28 L14,34 L12,28 L6,26 L12,24 Z',
    'M20,84 h60'
  ],
  /* 鎌倉大佛：露天的阿彌陀如來坐像——肉髻、低垂的眼、定印的手與蓮座。 */
  daibutsu: [
    'M50,14 q4,-5 0,-8',
    'M41,28 a9,11 0 0 1 18,0 v4 a9,12 0 0 1 -18,0 Z',
    'M44,28 q2.5,2 5,0', 'M51,28 q2.5,2 5,0',
    'M46,36 q4,3 8,0',
    'M50,42 C38,42 30,52 28,68 h44 C70,52 62,42 50,42 Z',
    'M50,46 q-6,9 -4,20',
    'M40,68 h20 a4,4 0 0 1 -4,5 h-12 a4,4 0 0 1 -4,-5 Z',
    'M24,78 q26,9 52,0',
    'M16,86 h68'
  ],
  /* ほたて日和：帆立貝殼、昆布水沾麵的麵碗與沾汁杯，冒著熱氣。 */
  tsukemen: [
    'M26,34 a13,10 0 0 1 22,0 Z',
    'M31,34 L34,25', 'M37,34 V24', 'M43,34 L40,25',
    'M10,52 h44 c-2,16 -10,23 -22,23 c-12,0 -20,-7 -22,-23 Z',
    'M16,52 q6,-7 12,-1 q6,6 12,-1 q5,-6 10,0',
    'M24,75 h16',
    'M62,50 h26 c-1,12 -6,18 -13,18 c-7,0 -12,-6 -13,-18 Z',
    'M70,68 h10',
    'M70,42 q5,-5 0,-11', 'M79,42 q5,-5 0,-11'
  ],
  /* たぬき通り商店街：街路灯の腰に鎮座する願かけたぬき——笠・丸い腹。 */
  tanuki: [
    'M80,90 V28',
    'M71,28 h18 l-5,-8 h-8 Z',
    'M76,20 V14',
    'M16,34 q18,-13 36,0 q-18,-5 -36,0',
    'M24,36 a10,11 0 0 1 20,0 v3 a10,12 0 0 1 -20,0 Z',
    'M28,37 h3', 'M37,37 h3',
    'M33,43 q1.5,2 3,0',
    'M34,52 C21,52 15,64 17,76 q17,10 34,0 C53,64 47,52 34,52 Z',
    'M24,64 a10,9 0 1 0 20,0 a10,9 0 1 0 -20,0'
  ],
  /* HARBS：層層奶油與水果的蛋糕切片、盤與叉。 */
  cake: [
    'M14,80 q36,10 72,0',
    'M28,74 V48 h40 v26 Z',
    'M28,66 h40', 'M28,57 h40',
    'M48,48 q-5,-9 0,-13 q5,5 0,13',
    'M84,42 v36', 'M80,42 v11', 'M88,42 v11'
  ],
  /* KITTE ガーデン：屋上的欄杆、花槽與對面東京車站的磚造外牆。 */
  rooftop: [
    'M50,58 V36 h32 v22', 'M58,36 q8,-10 16,0',
    'M56,44 h5', 'M64,44 h5', 'M72,44 h5',
    'M6,58 h88', 'M6,76 h88',
    'M18,76 V58', 'M42,76 V58', 'M66,76 V58', 'M88,76 V58',
    'M20,76 h24 l-3,13 h-18 Z',
    'M31,76 V66 q-8,-2 -10,-9 q10,0 10,9 q0,-9 10,-11 q0,10 -10,11'
  ],
  /* 座布團蒙布朗：方形座布團上的蒙布朗、擠花線與頂上的栗子。 */
  montblanc: [
    'M20,78 h60 v11 h-60 Z',
    'M26,78 v11', 'M74,78 v11',
    'M30,78 q20,-46 40,0',
    'M34,70 q16,-9 32,0', 'M32,62 q18,-9 36,0', 'M37,54 q13,-7 26,0',
    'M50,36 q-6,-7 0,-11 q6,5 0,11'
  ],
  /* 一番街・八重洲地下街：往下的階梯、拱頂與並排的店招。 */
  arcade: [
    'M20,38 q30,-22 60,0',
    'M32,46 h14 v10 h-14 Z', 'M56,46 h16 v10 h-16 Z',
    'M10,58 h12 v8 h12 v8 h12 v8 h14',
    'M8,88 h84',
    'M20,38 V30', 'M80,38 V30'
  ],
  /* 鮪のシマハラ：砧板上的本鮪柵、切片與醬油碟。 */
  tuna: [
    'M12,70 h76 v8 h-76 Z',
    'M24,70 V54 h26 v16 Z',
    'M30,58 v12', 'M38,56 v14', 'M44,58 v12',
    'M58,70 l9,-9', 'M67,70 l9,-9', 'M76,70 l9,-9',
    'M16,84 a9,4 0 1 0 18,0 a9,4 0 1 0 -18,0'
  ],
  /* 日テレ大時計：巨大的機關鐘面、齒輪與蒸氣管。 */
  bigclock: [
    'M32,32 a18,18 0 1 0 36,0 a18,18 0 1 0 -36,0',
    'M50,32 V22', 'M50,32 l10,6',
    'M20,64 a9,9 0 1 0 18,0 a9,9 0 1 0 -18,0',
    'M29,55 V51', 'M29,73 v4', 'M20,64 h-4', 'M38,64 h4',
    'M58,66 h18 v16',
    'M68,58 q5,-7 0,-12',
    'M12,88 h76'
  ],
  /* 鎌倉高校前：平交道的遮斷桿、警報燈與軌道，遠處是海。 */
  crossing: [
    'M18,84 V50',
    'M18,54 h58', 'M18,60 h58',
    'M32,54 v6', 'M46,54 v6', 'M60,54 v6',
    'M13,44 a5,5 0 1 0 10,0 a5,5 0 1 0 -10,0',
    'M6,80 h88', 'M6,88 h88',
    'M26,80 v8', 'M50,80 v8', 'M74,80 v8',
    'M64,70 q7,-5 14,0'
  ],
  /* 七里ヶ浜：海浪、遠方的富士山與低い日輪。 */
  beach: [
    'M18,56 L38,28 L58,56',
    'M31,38 q7,6 14,0',
    'M74,28 a9,9 0 1 0 18,0 a9,9 0 1 0 -18,0',
    'M6,56 h88',
    'M8,68 q8,-6 16,0 q8,6 16,0 q8,-6 16,0 q8,6 16,0 q8,-6 16,0',
    'M8,80 q8,-6 16,0 q8,6 16,0 q8,-6 16,0 q8,6 16,0 q8,-6 16,0'
  ],
  /* 小町通り：入口的鳥居型看板、吊り提灯與石板路。 */
  shotengai: [
    'M18,86 V38', 'M78,86 V38',
    'M10,34 q38,-7 76,0',
    'M14,46 h68',
    'M30,52 a6,7 0 1 0 12,0 a6,7 0 1 0 -12,0',
    'M54,52 a6,7 0 1 0 12,0 a6,7 0 1 0 -12,0',
    'M36,46 v3', 'M60,46 v3',
    'M26,88 h44', 'M34,78 h28'
  ],
  /* 汽車道・萬國橋：桁架橋、橋下的水面與岸邊燈柱。 */
  bridge: [
    'M8,62 h84',
    'M16,62 q34,-32 68,0',
    'M33,62 V50', 'M50,62 V44', 'M67,62 V50',
    'M12,62 V42', 'M8,40 h8',
    'M8,78 q10,-6 20,0 q10,6 20,0 q10,-6 20,0 q10,6 20,0',
    'M8,88 q10,-6 20,0 q10,6 20,0 q10,-6 20,0 q10,6 20,0'
  ],
  /* 赤レンガ倉庫：磚造倉庫的窗列，與啤酒節的大啤酒杯。 */
  brickbeer: [
    'M12,86 V44 h50 v42',
    'M12,44 h50',
    'M20,52 h8 v10 h-8 Z', 'M33,52 h8 v10 h-8 Z', 'M46,52 h8 v10 h-8 Z',
    'M20,70 h8 v10 h-8 Z', 'M33,70 h8 v10 h-8 Z', 'M46,70 h8 v10 h-8 Z',
    'M72,58 h16 v28 h-16 Z',
    'M88,64 q7,6 0,12',
    'M72,58 q4,-7 8,0 q4,-7 8,0'
  ],
  /* 明治神宮：大鳥居與兩側的森。 */
  torii: [
    'M28,86 V40', 'M72,86 V40',
    'M16,34 q34,-9 68,0',
    'M20,42 h60',
    'M24,56 h52',
    'M46,56 V42 h8 v14 Z',
    'M6,80 L13,56 L20,80', 'M80,80 L87,56 L94,80',
    'M13,80 v6', 'M87,80 v6',
    'M6,86 h88'
  ],
  /* 牡蠣ベース：開殼的生牡蠣與檸檬。 */
  oyster: [
    'M18,62 q24,-24 52,-6 q-20,22 -52,6 Z',
    'M28,58 q16,-13 32,-7', 'M26,51 q16,-11 30,-7',
    'M42,54 q10,-7 15,2 q-9,7 -15,-2',
    'M70,78 a10,10 0 1 1 20,0 Z',
    'M80,78 V68', 'M72,76 l8,-7', 'M88,76 l-8,-7',
    'M10,80 q40,13 80,0'
  ]
  /* こち亀記念館：亀有公園前派出所——切妻屋根の交番と屋上の赤ランプ。角色不入畫。 */
  koban: [
    'M20,86 h60',
    'M26,86 V46 h48 v40',
    'M18,46 L50,26 L82,46',
    'M50,26 V16',
    'M44,12 a6,6 0 1 0 12,0 a6,6 0 1 0 -12,0',
    'M44,86 V62 h12 v24',
    'M47,74 h3',
    'M32,52 h12 v10 h-12 Z',
    'M56,52 h12 v10 h-12 Z'
  ]
};
function handDrawnIllustration(key, cls) {
  var ns = 'http://www.w3.org/2000/svg';
  var wrap = el('div', {
    class: (cls ? cls + ' ' : '') + 'noimg illus-spot',
    style: 'background:linear-gradient(160deg,#F3D9C8,#EBC7BE 55%,#CFAFAE)'
  });
  var svg = document.createElementNS(ns, 'svg');
  svg.setAttribute('viewBox', '0 0 100 100');
  svg.setAttribute('aria-hidden', 'true');
  (ILLUSTRATIONS[key] || []).forEach(function (d) {
    var p = document.createElementNS(ns, 'path');
    p.setAttribute('d', d);
    svg.appendChild(p);
  });
  wrap.appendChild(svg);
  return wrap;
}
/** 打卡進度環。載入時由 0 掃到實際比例。 */
function ring(done, total) {
  var ns = 'http://www.w3.org/2000/svg';
  var C = 2 * Math.PI * 20;
  var s = document.createElementNS(ns, 'svg');
  s.setAttribute('viewBox', '0 0 48 48');
  s.setAttribute('class', 'ring');
  ['track', 'fill'].forEach(function (cls) {
    var c = document.createElementNS(ns, 'circle');
    c.setAttribute('cx', '24'); c.setAttribute('cy', '24'); c.setAttribute('r', '20');
    c.setAttribute('class', cls);
    if (cls === 'fill') {
      c.style.setProperty('stroke-dasharray', C.toFixed(1));
      c.style.setProperty('stroke-dashoffset', C.toFixed(1));
      requestAnimationFrame(function () {
        requestAnimationFrame(function () {
          c.style.setProperty('stroke-dashoffset', (C * (1 - (total ? done / total : 0))).toFixed(1));
        });
      });
    }
    s.appendChild(c);
  });
  var t = document.createElementNS(ns, 'text');
  t.setAttribute('x', '24'); t.setAttribute('y', '24');
  t.setAttribute('class', 'rt');
  t.textContent = done + '/' + total;
  s.appendChild(t);
  return el('div', { class: 'ringwrap' }, [s]);
}

function pill(text, bg, fg) {
  return el('span', { class: 'pill', text: text, style: 'background:' + bg + ';color:' + fg });
}
/** 行程重要度。A 已訂或不可重來、B 重點、C 順路可捨——趕時間時從 C 開始砍。 */
var TIERS = {
  A: { label: 'A 必訪', bg: 'color-mix(in srgb, var(--secondary) 18%, transparent)', fg: 'var(--secondary)' },
  B: { label: 'B 重點', bg: 'color-mix(in srgb, var(--primary) 14%, transparent)', fg: 'var(--primary)' },
  C: { label: 'C 可捨', bg: 'var(--surface-variant)', fg: 'var(--on-surface-variant)' }
};
function tierPill(tier) {
  var t = TIERS[tier];
  return t ? pill(t.label, t.bg, t.fg) : null;
}
/** 有色系圖示的提示清單。取代先前用「！」「◆」等字元當項目符號的做法。 */
function noteList(items, kind) {
  var k = KIND[kind] || KIND.note;
  return el('ul', { class: 'notes' }, items.map(function (t) {
    var ic = svgIcon(k.icon, 'ni');
    ic.style.setProperty('fill', k.color);
    // 提醒常是「主詞：內容」，在手機上一條就是一整段。把冒號前的短主詞抽成
    // 粗體第一行，才掃得出哪一條講的是哪一站。只對 alert 做，其餘筆記照舊。
    var cut = kind === 'alert' ? t.indexOf('：') : -1;
    if (cut > 0 && cut <= 20) {
      return el('li', {}, [ic, el('span', {}, [
        el('span', { class: 'note-lead', text: t.slice(0, cut) }),
        el('span', { text: t.slice(cut + 1) })
      ])]);
    }
    return el('li', {}, [ic, el('span', { text: t })]);
  }));
}

/** 圖示＋標籤＋內容的三欄格線，比長句條列好掃。 */
function factGrid(rows) {
  return el('dl', { class: 'facts' }, rows.reduce(function (acc, r) {
    if (!r.value) return acc;
    var ic = svgIcon(r.icon, 'fi');
    if (r.color) ic.style.setProperty('fill', r.color);
    acc.push(el('dt', {}, [ic, el('span', { text: r.label })]));
    acc.push(el('dd', { text: r.value }));
    return acc;
  }, []));
}

function bullets(items, mark) {
  return el('ul', { class: 'bullets' }, items.map(function (t) {
    return el('li', { 'data-b': mark || '・', text: t });
  }));
}
function card(titleText, accent, bodyNodes, opts) {
  var body = el('div', { class: 'card-pad' },
    [titleText ? el('div', { class: 'card-title', style: '--accent:' + accent, text: titleText }) : null]
      .concat(bodyNodes));
  return el('div', Object.assign({ class: 'card' }, opts || {}), [body]);
}
/** 可收合區塊。標題左側以該類別的圖示與色系標示，一眼就能分辨提示種類。 */
function expandable(kind, count, bodyNodes, open) {
  var k = KIND[kind] || KIND.note;
  var head = svgIcon(k.icon, 'eh');
  head.style.setProperty('fill', k.color);
  var d = el('details', { class: 'card exp', open: !!open }, [
    el('summary', {}, [
      head,
      el('span', { class: 'et', text: k.label }),
      pill(String(count), 'color-mix(in srgb, ' + k.color + ' 16%, transparent)', k.color),
      svgIcon(ICON.chev, 'chev')
    ]),
    el('div', { class: 'body' }, bodyNodes)
  ]);
  d.style.setProperty('--accent', k.color);
  return d;
}
function topbar(title, actions) {
  return el('div', { class: 'topbar' }, [
    el('button', { class: 'iconbtn', 'aria-label': '返回', onclick: function () { history.back(); } }, [svgIcon(ICON.back)]),
    el('h2', { text: title })
  ].concat(actions || []));
}

/* ---------- 今日分岔點 ----------
   分岔點是排不下的取捨，要留到當天現場才決定（見 trip-data.json 的 day.forks）。
   所以它不是條列文字，而是一個節點分出數條路徑：點下去就選定，選中的那條亮起、
   其餘淡出，再點一次退回未決定。選擇存在 localStorage，人在現場點完之後
   重新整理、關掉再打開都還在。 */

/** 「A｜內容」拆成代號與內容。沒寫代號就依序補 A、B、C。 */
function parseForkOption(text, i) {
  var m = /^\s*([A-Za-z0-9]{1,2})\s*[｜|]\s*([\s\S]+)$/.exec(String(text));
  if (m) return { key: m[1].toUpperCase(), body: m[2] };
  return { key: String.fromCharCode(65 + i), body: String(text) };
}

function forkNode(day, f) {
  var opts = f.options.map(parseForkOption);
  var keys = opts.map(function (o) { return o.key; });
  // 以「第幾天＋時間」當 key：改寫文案不會讓已經做過的選擇跑掉
  var id = 'fork_d' + day.n + '_' + String(f.at).replace(/[^0-9]/g, '');

  var picked = Store.pick(id);
  if (keys.indexOf(picked) < 0) picked = '';   // 選項改過，舊的選擇就不算數

  var paths = el('div', {
    class: 'fork-paths fork-h', role: 'radiogroup',
    'aria-label': f.at + '　' + f.title
  });
  paths.style.setProperty('--n', String(opts.length));
  // 分成太多條就排不下，退回直向堆疊
  if (opts.length > 4) paths.classList.remove('fork-h');

  var status = el('span', { class: 'fork-status' });
  var reset = el('button', { class: 'fork-reset', type: 'button', text: '重選' });
  var btns = [];

  function paint() {
    btns.forEach(function (b, i) {
      var on = picked === keys[i];
      b.setAttribute('aria-checked', on ? 'true' : 'false');
      // roving tabindex：整組在 Tab 順序裡只佔一格
      b.tabIndex = (on || (!picked && i === 0)) ? 0 : -1;
    });
    paths.setAttribute('data-decided', picked ? 'yes' : 'no');
    status.textContent = picked
      ? '已選 ' + picked + '　其餘路徑先擱著'
      : '尚未決定　到現場再點';
    reset.hidden = !picked;
  }

  function commit(v) { picked = v; Store.setPick(id, v); paint(); }

  opts.forEach(function (o, i) {
    // 手機上每條路徑都是一整段字，很難掃。把冒號前的短標題抽成粗體第一行，
    // 這樣 A／B／C 在不細讀的情況下也分得出來。
    var cut = o.body.indexOf('：');
    var lead = (cut > 0 && cut <= 24) ? o.body.slice(0, cut) : '';
    var b = el('button', { class: 'fork-opt', type: 'button', role: 'radio' }, [
      el('span', { class: 'fork-key', text: o.key }),
      el('span', { class: 'fork-body' }, [
        lead ? el('span', { class: 'fork-lead', text: lead }) : null,
        el('span', { class: 'fork-text', text: lead ? o.body.slice(cut + 1) : o.body })
      ]),
      svgIcon(ICON.check, 'fork-tick')
    ]);
    // 點已選的那條就退回未決定：當天狀況會變，要能反悔
    b.addEventListener('click', function () { commit(picked === keys[i] ? '' : keys[i]); });
    b.addEventListener('keydown', function (e) {
      var step = (e.key === 'ArrowRight' || e.key === 'ArrowDown') ? 1
               : (e.key === 'ArrowLeft' || e.key === 'ArrowUp') ? -1 : 0;
      if (!step) return;
      e.preventDefault();
      var n = (i + step + btns.length) % btns.length;
      commit(keys[n]);
      btns[n].focus();
    });
    btns.push(b);
    paths.appendChild(b);
  });

  reset.addEventListener('click', function () { commit(''); });
  paint();

  return el('div', { class: 'fork' }, [
    el('div', { class: 'fork-head' }, [
      el('span', { class: 'fork-at', text: f.at }),
      el('span', { class: 'fork-title', text: f.title })
    ]),
    el('div', { class: 'fork-stem', 'aria-hidden': 'true' }),
    paths,
    el('div', { class: 'fork-foot' }, [status, reset])
  ]);
}

/* ---------- 畫面：首頁 ---------- */
function viewHome() {
  var ids = [];
  TRIP.days.forEach(function (d) { d.stops.forEach(function (s) { ids.push(s.spot.id); }); });
  var done = Store.count(ids);
  var maxMove = TRIP.days.reduce(function (m, d) { return Math.max(m, d.moveMinutes); }, 1);

  var frag = document.createDocumentFragment();

  frag.appendChild(el('div', { class: 'hero illus' }, [
    el('img', { src: 'hero.svg', alt: '', decoding: 'async' }),
    el('div', { class: 'scrim' }),
    el('div', { class: 'cap' }, [el('h1', { text: TRIP.title }), el('p', { text: TRIP.subtitle })])
  ]));

  var main = el('div', { class: 'split-main' });
  var side = el('div', { class: 'split-side' });
  frag.appendChild(el('div', { class: 'split' }, [main, side]));

  main.appendChild(el('div', { class: 'stats' }, [
    el('div', { class: 'stat tap', onclick: function () { go('#/checklist'); } },
      [ring(done, ids.length), el('span', { text: '打卡' })]),
    el('div', { class: 'stat' },
      [el('b', { class: 'accent-gold', text: String(ids.length) }), el('span', { text: '景點' })]),
    el('div', { class: 'stat' },
      [el('b', { class: 'accent-pink', text: TRIP.hotels.length + ' 家' }), el('span', { text: '住宿' })])
  ]));

  main.appendChild(card('航班', 'var(--secondary)', [
    bullets(['去程　' + TRIP.flightOut, '回程　' + TRIP.flightBack]),
    el('p', { class: 'hint', text: '落地已晚，Day 1 不排景點；Day 8 中午須離開市區。' })
  ]));

  main.appendChild(el('div', { class: 'section-title', text: '每日行程' }));

  var daysGrid = el('div', { class: 'grid grid-days' });
  main.appendChild(daysGrid);

  TRIP.days.forEach(function (d) {
    var dayIds = d.stops.map(function (s) { return s.spot.id; });
    var last = d.stops[d.stops.length - 1].spot;
    daysGrid.appendChild(el('div', { class: 'card tap', onclick: function () { go('#/day/' + d.n); } }, [
      el('div', { class: 'hero small' }, [
        photoEl(last), el('div', { class: 'scrim' }),
        el('div', { class: 'cap' }, [el('h2', { text: 'DAY ' + d.n + '　' + d.date + ' ' + d.weekday })]),
        d.alerts.length ? el('div', { class: 'corner' },
          [pill('⚠ ' + d.alerts.length, 'rgba(0,0,0,.45)', '#fff')]) : null
      ]),
      el('div', { class: 'card-pad' }, [
        el('div', { style: 'font-weight:700', text: d.theme }),
        el('div', { class: 'dsum clamp2', text: d.summary }),
        el('div', { class: 'scroll-x', style: 'margin-top:10px' }, [
          pill(d.stops.length + ' 站', 'var(--surface-variant)', 'var(--on-surface-variant)'),
          pill('移動 ' + d.moveMinutes + ' 分', 'color-mix(in srgb, var(--primary) 12%, transparent)', 'var(--primary)'),
          pill('打卡 ' + Store.count(dayIds), 'color-mix(in srgb, var(--tertiary) 18%, transparent)', 'var(--on-surface)')
        ]),
        el('div', { class: 'scroll-x', style: 'margin-top:10px' }, d.stops.map(function (st) {
          return el('div', {
            style: 'width:96px;flex:none;cursor:pointer',
            onclick: function (e) { e.stopPropagation(); go('#/spot/' + st.spot.id); }
          }, [
            photoEl(st.spot, 'thumb'),
            el('div', { style: 'font-size:.72rem;margin-top:4px', text: st.spot.nameZh })
          ]);
        }))
      ])
    ]));
  });

  side.appendChild(el('div', { class: 'section-title', text: '交通總覽' }));
  side.appendChild(card('每日移動時間', 'var(--primary)', TRIP.days.map(function (d) {
    // 以最長的一天等比縮放，寬度恆落在 (0,100%]
    var pct = Math.max(4, Math.round(d.moveMinutes / maxMove * 100));
    var bar = el('div', { class: 'bar' });
    requestAnimationFrame(function () {
      requestAnimationFrame(function () { bar.style.setProperty('width', pct + '%'); });
    });
    return el('div', { class: 'barrow tap', onclick: function () { go('#/day/' + d.n); } }, [
      el('span', { class: 'blabel', text: 'D' + d.n }),
      el('div', { class: 'bar-track' }, [bar]),
      el('span', { class: 'bval', text: d.moveMinutes + ' 分' })
    ]);
  })));

  side.appendChild(card('住宿', 'var(--tertiary)',
    [bullets(TRIP.hotels.map(function (h) { return h.when + '　' + h.name; }))]));

  side.appendChild(el('p', {
    class: 'foot',
    text: '資料查證於 2026/8，出發前請以各景點官網為準。照片來源 Wikimedia Commons（CC／公有領域）。'
  }));
  return frag;
}

/* ---------- 畫面：單日 ---------- */
function viewDay(n) {
  var d = TRIP.days.filter(function (x) { return x.n === n; })[0] || TRIP.days[0];
  var frag = document.createDocumentFragment();
  frag.appendChild(topbar('Day ' + d.n + '　' + d.date + ' ' + d.weekday));

  frag.appendChild(el('div', { class: 'hero small' }, [
    photoEl(d.stops[d.stops.length - 1].spot), el('div', { class: 'scrim' }),
    el('div', { class: 'cap' }, [el('h2', { text: d.theme }), el('p', { class: 'clamp2', text: d.summary })])
  ]));

  var main = el('div', { class: 'split-main' });
  var side = el('div', { class: 'split-side' });
  frag.appendChild(el('div', { class: 'split' }, [main, side]));

  main.appendChild(el('div', { class: 'scroll-x', style: 'padding:12px 16px 0' }, [
    pill(d.stops.length + ' 站', 'var(--surface-variant)', 'var(--on-surface-variant)'),
    pill('移動 ' + d.moveMinutes + ' 分', 'color-mix(in srgb, var(--primary) 12%, transparent)', 'var(--primary)')
  ]));

  if (d.alerts.length) main.appendChild(expandable('alert', d.alerts.length, [noteList(d.alerts, 'alert')], true));

  // 排不下的東西不預先砍掉，改成當天到了現場才決定的分岔點。
  if (d.forks && d.forks.length) {
    // 預設收起：分岔點是現場才用的東西，攤開會把當天動線推到好幾個畫面之外。
    main.appendChild(expandable('fork', d.forks.length, d.forks.map(function (f) {
      return forkNode(d, f);
    })));
  }

  var tl = el('div', { class: 'timeline' });
  d.stops.forEach(function (st) {
    if (st.leg) {
      tl.appendChild(el('div', { class: 'leg' }, [
        el('div', { class: 'line' }),
        el('div', {}, [
          el('div', { style: 'font-weight:600', text: st.leg.mode + '　' + st.leg.minutes + ' 分　' + st.leg.fare }),
          el('div', { text: st.leg.route })
        ])
      ]));
    }
    tl.appendChild(el('div', {
      class: 'stop', style: 'cursor:pointer',
      onclick: function () { go('#/spot/' + st.spot.id); }
    }, [
      el('div', { class: 'time', text: st.time }),
      el('div', { class: 'body' }, [
        photoEl(st.spot),
        el('div', { class: 'stop-name', text: st.spot.nameZh }),
        el('div', { class: 'stop-sub', text: st.spot.nameJa + '　停留 ' + st.spot.stay }),
        el('div', { class: 'stop-tags' }, [
          tierPill(st.spot.tier),
          st.spot.mustLeaveBy ? el('span', {
            style: 'font-size:.72rem;color:var(--on-surface-variant)',
            text: '最晚離開 ' + st.spot.mustLeaveBy
          }) : null
        ])
      ])
    ]));
  });
  main.appendChild(el('div', { class: 'card' }, [
    el('div', { class: 'card-pad', style: 'padding-bottom:4px' },
      [el('div', { class: 'card-title', text: '行程動線' })]),
    tl
  ]));

  if (d.tips.length) side.appendChild(expandable('tip', d.tips.length, [noteList(d.tips, 'tip')]));
  side.appendChild(card('今晚住宿', 'var(--primary)', [
    el('div', { style: 'font-weight:700', text: d.hotelName }),
    el('div', { style: 'font-size:.85rem;color:var(--on-surface-variant)', text: d.hotelNote })
  ]));
  return frag;
}

/* ---------- 畫面：景點 ---------- */
function viewSpot(id) {
  var spot = null, day = null;
  TRIP.days.forEach(function (d) {
    d.stops.forEach(function (s) { if (s.spot.id === id) { spot = s.spot; day = d; } });
  });
  if (!spot) { go('#/home'); return document.createDocumentFragment(); }

  var frag = document.createDocumentFragment();
  var favBtn = el('button', { class: 'iconbtn', 'aria-label': '收藏' });
  var chkBtn = el('button', { class: 'iconbtn', 'aria-label': '打卡' });
  function paintFav() {
    favBtn.textContent = '';
    var on = Store.isFav(spot.id);
    var i = svgIcon(on ? ICON.heart : ICON.heartOff);
    i.style.fill = on ? 'var(--secondary)' : 'var(--on-surface-variant)';
    favBtn.appendChild(i);
  }
  function paintChk() {
    chkBtn.textContent = '';
    var on = Store.get(spot.id);
    var i = svgIcon(on ? ICON.check : ICON.circle);
    i.style.fill = on ? 'var(--ok)' : 'var(--on-surface-variant)';
    chkBtn.appendChild(i);
  }
  favBtn.addEventListener('click', function () { Store.toggle('fav_' + spot.id); paintFav(); });
  chkBtn.addEventListener('click', function () { Store.toggle(spot.id); paintChk(); });
  paintFav(); paintChk();

  var c0 = TRIP.photoCredits[spot.id];
  frag.appendChild(topbar(spot.nameZh, [favBtn, chkBtn]));

  var main = el('div', { class: 'split-main' });
  var side = el('div', { class: 'split-side' });
  frag.appendChild(el('div', { class: 'split' }, [main, side]));

  main.appendChild(el('div', {
    class: 'hero' + (spot.photo ? ' zoomable' : ''),
    onclick: function () {
      if (spot.photo) lightbox(spot.photo, spot.nameZh, c0 ? '照片：' + c0.author + '／' + c0.license : null);
    }
  }, [
    photoEl(spot), el('div', { class: 'scrim' }),
    el('div', { class: 'cap' }, [el('h2', { text: spot.nameJa }), el('p', { text: spot.kana })]),
    el('div', { class: 'corner' }, [
      day ? pill('Day ' + day.n + '・' + day.date, 'rgba(0,0,0,.45)', '#fff') : null,
      tierPill(spot.tier)
    ])
  ]));

  var acts = [
    el('a', { class: 'btn', href: spot.navUrl, target: '_blank', rel: 'noopener noreferrer' },
      [svgIcon(ICON.nav), document.createTextNode('導航')]),
    el('a', { class: 'btn outline', href: spot.mapUrl, target: '_blank', rel: 'noopener noreferrer' },
      [svgIcon(ICON.map), document.createTextNode('地圖')])
  ];
  if (spot.official) acts.push(el('a', { class: 'btn gold', href: spot.official, target: '_blank', rel: 'noopener noreferrer' },
    [svgIcon(ICON.web), document.createTextNode('官網')]));
  main.appendChild(el('div', { class: 'actions' }, acts));

  main.appendChild(card('重點', 'var(--primary)', [factGrid([
    { icon: ICON.clock, label: '時間', value: spot.hours, color: 'var(--primary)' },
    { icon: ICON.yen, label: '費用', value: spot.price, color: 'var(--eat)' },
    { icon: ICON.hourglass, label: '停留', value: spot.stay, color: 'var(--primary)' },
    { icon: ICON.alert, label: '最晚離開', value: spot.mustLeaveBy, color: 'var(--secondary)' },
    { icon: ICON.closed, label: '公休', value: spot.closed, color: 'var(--secondary)' },
    { icon: ICON.ticket, label: '預約', value: spot.booking, color: 'var(--tertiary)' }
  ])]));

  if (spot.notes.length) side.appendChild(expandable('note', spot.notes.length, [noteList(spot.notes, 'note')], true));
  if (spot.eats.length) side.appendChild(expandable('eat', spot.eats.length, [noteList(spot.eats, 'eat')]));
  if (spot.warns.length) side.appendChild(expandable('warn', spot.warns.length, [noteList(spot.warns, 'warn')]));

  var c = TRIP.photoCredits[spot.id];
  if (c) side.appendChild(el('p', { class: 'credit' }, [
    el('a', { href: c.page, target: '_blank', rel: 'noopener noreferrer',
      text: '照片：' + c.author + '／' + c.license + '（Wikimedia Commons）' })
  ]));
  return frag;
}

/* ---------- 畫面：景點列表 ---------- */
var spotsState = { q: '', cat: 'ALL' };
function viewSpots() {
  var all = [], seen = {};
  TRIP.days.forEach(function (d) {
    d.stops.forEach(function (s) { if (!seen[s.spot.id]) { seen[s.spot.id] = 1; all.push(s.spot); } });
  });
  var cats = [{ k: 'ALL', l: '全部' }, { k: 'FAV', l: '收藏' },
    { k: 'T:A', l: 'A 必訪' }, { k: 'T:B', l: 'B 重點' }, { k: 'T:C', l: 'C 可捨' }];
  var seenCat = {};
  all.forEach(function (s) { if (!seenCat[s.cat]) { seenCat[s.cat] = 1; cats.push({ k: s.cat, l: s.catLabel }); } });

  var frag = document.createDocumentFragment();
  var list = el('div', { class: 'grid grid-spots' });
  var countLbl = el('div', { class: 'foot', style: 'padding:6px 20px 0' });

  function render() {
    var q = spotsState.q.trim().toLowerCase();
    var out = all.filter(function (s) {
      if (spotsState.cat === 'FAV' && !Store.isFav(s.id)) return false;
      if (spotsState.cat.indexOf('T:') === 0 && s.tier !== spotsState.cat.slice(2)) return false;
      if (spotsState.cat !== 'ALL' && spotsState.cat !== 'FAV' && spotsState.cat.indexOf('T:') !== 0
          && s.cat !== spotsState.cat) return false;
      if (!q) return true;
      return (s.nameZh + ' ' + s.nameJa + ' ' + s.kana + ' ' + s.area).toLowerCase().indexOf(q) >= 0;
    });
    countLbl.textContent = out.length + ' 個景點';
    list.textContent = '';
    out.forEach(function (s) {
      list.appendChild(el('div', { class: 'card spot-card' }, [
        el('div', { class: 'spot-row', style: 'cursor:pointer', onclick: function () { go('#/spot/' + s.id); } }, [
          photoEl(s),
          el('div', { class: 'meta' }, [
            el('b', { text: s.nameZh }),
            el('small', { text: s.nameJa + '　' + s.area }),
            el('div', { style: 'margin-top:4px;display:flex;gap:6px;flex-wrap:wrap' }, [
              tierPill(s.tier),
              pill(s.catLabel, 'var(--surface-variant)', 'var(--on-surface-variant)')
            ])
          ]),
          Store.isFav(s.id) ? (function () { var i = svgIcon(ICON.heart); i.style.fill = 'var(--secondary)'; i.style.width = '20px'; return i; })() : null
        ])
      ]));
    });
  }

  frag.appendChild(el('div', { class: 'searchbar' }, [
    el('input', {
      type: 'search', placeholder: '搜尋景點、日文名、地區', 'aria-label': '搜尋',
      value: spotsState.q,
      oninput: function (e) { spotsState.q = e.target.value; render(); }
    })
  ]));
  frag.appendChild(el('div', { class: 'chips' }, cats.map(function (c) {
    return el('button', {
      class: 'chip', 'aria-pressed': spotsState.cat === c.k ? 'true' : 'false', text: c.l,
      onclick: function (e) {
        spotsState.cat = c.k;
        Array.prototype.forEach.call(e.target.parentNode.children, function (b) { b.setAttribute('aria-pressed', 'false'); });
        e.target.setAttribute('aria-pressed', 'true');
        render();
      }
    });
  })));
  frag.appendChild(countLbl);
  frag.appendChild(list);
  render();
  return frag;
}

/* ---------- 畫面：行前準備 ---------- */
function viewChecklist() {
  var frag = document.createDocumentFragment();
  frag.appendChild(el('div', { class: 'section-title', text: '行前準備' }));

  var groups = [], byGroup = {};
  TRIP.checklist.forEach(function (it) {
    if (!byGroup[it.group]) { byGroup[it.group] = []; groups.push(it.group); }
    byGroup[it.group].push(it);
  });

  var progress = el('div', { class: 'foot', style: 'padding:0 20px 8px' });
  function paintProgress() {
    var ids = TRIP.checklist.map(function (i) { return i.id; });
    progress.textContent = '已完成 ' + Store.count(ids) + ' / ' + ids.length + ' 項';
  }
  paintProgress();
  frag.appendChild(progress);

  var checkGrid = el('div', { class: 'grid grid-check' });
  frag.appendChild(checkGrid);

  groups.forEach(function (g) {
    var body = el('div');
    byGroup[g].forEach(function (it) {
      var row = el('div', {
        class: 'check-item', role: 'checkbox', tabindex: '0',
        'aria-checked': Store.get(it.id) ? 'true' : 'false'
      }, [
        el('div', { class: 'box' }, [svgIcon(ICON.tick)]),
        el('div', { class: 'txt', text: it.text })
      ]);
      function toggle() {
        row.setAttribute('aria-checked', Store.toggle(it.id) ? 'true' : 'false');
        paintProgress();
      }
      row.addEventListener('click', toggle);
      row.addEventListener('keydown', function (e) {
        if (e.key === ' ' || e.key === 'Enter') { e.preventDefault(); toggle(); }
      });
      body.appendChild(row);
    });
    checkGrid.appendChild(el('div', { class: 'card' }, [
      el('div', { class: 'card-pad', style: 'padding-bottom:0' }, [el('div', { class: 'card-title', text: g })]),
      body
    ]));
  });

  frag.appendChild(el('div', { class: 'section-title', text: '必訂票券' }));
  var bookGrid = el('div', { class: 'grid grid-books' });
  frag.appendChild(bookGrid);
  TRIP.bookings.forEach(function (b) {
    bookGrid.appendChild(card(b.name, 'var(--tertiary)', [
      el('div', { style: 'font-size:.88rem', text: b.status }),
      el('div', { style: 'font-size:.8rem;color:var(--on-surface-variant)', text: b.note })
    ]));
  });

  frag.appendChild(el('div', { style: 'padding:16px' }, [
    el('button', {
      class: 'btn outline', style: 'width:100%',
      text: '鎖定並清除已解密資料',
      onclick: function () {
        sessionStorage.removeItem(KEY_PREFIX + 'pass');
        localStorage.removeItem(KEY_PREFIX + 'pass');
        location.reload();
      }
    })
  ]));
  return frag;
}

/* ---------- 照片全螢幕檢視 ---------- */
function lightbox(src, alt, credit) {
  var box = el('div', { class: 'lb', role: 'dialog', 'aria-label': alt }, [
    el('button', { class: 'lb-x iconbtn', 'aria-label': '關閉' }, [svgIcon(ICON.close)]),
    el('img', { src: src, alt: alt }),
    credit ? el('p', { class: 'lb-c', text: credit }) : null
  ]);
  function close() {
    box.classList.remove('on');
    document.removeEventListener('keydown', onKey);
    setTimeout(function () { box.remove(); }, 200);
  }
  function onKey(e) { if (e.key === 'Escape') close(); }
  box.addEventListener('click', close);
  document.addEventListener('keydown', onKey);
  document.body.appendChild(box);
  requestAnimationFrame(function () { box.classList.add('on'); });
}

/* ---------- 路由 ---------- */
function go(hash) { location.hash = hash; }
function render() {
  var h = location.hash || '#/home';
  var view = document.getElementById('view');
  view.textContent = '';
  view.classList.remove('in');
  var node, tab = null;
  if (h.indexOf('#/day/') === 0) node = viewDay(parseInt(h.slice(6), 10) || 1);
  else if (h.indexOf('#/spot/') === 0) node = viewSpot(h.slice(7));
  else if (h === '#/spots') { node = viewSpots(); tab = '#/spots'; }
  else if (h === '#/checklist') { node = viewChecklist(); tab = '#/checklist'; }
  else { node = viewHome(); tab = '#/home'; }
  view.appendChild(node);
  Array.prototype.forEach.call(document.querySelectorAll('.tab'), function (b) {
    if (b.dataset.route === tab) b.setAttribute('aria-current', 'page');
    else b.removeAttribute('aria-current');
  });
  window.scrollTo(0, 0);
  requestAnimationFrame(function () { view.classList.add('in'); });
}

/* ---------- 啟動 ---------- */
function start(trip) {
  TRIP = trip;
  document.title = trip.title;
  document.getElementById('lock').remove();
  var app = document.getElementById('app');
  app.hidden = false;
  Array.prototype.forEach.call(document.querySelectorAll('.tab'), function (b) {
    b.addEventListener('click', function () { go(b.dataset.route); });
  });
  window.addEventListener('hashchange', render);
  render();
}

document.addEventListener('DOMContentLoaded', function () {
  var form = document.getElementById('lock-form');
  var input = document.getElementById('pass');
  var errEl = document.getElementById('lock-err');
  var btn = document.getElementById('unlock');
  var remember = document.getElementById('remember');

  async function attempt(pass, persist) {
    btn.disabled = true; errEl.textContent = '解密中…';
    try {
      var trip = await decryptTrip(pass);
      if (persist) localStorage.setItem(KEY_PREFIX + 'pass', pass);
      else sessionStorage.setItem(KEY_PREFIX + 'pass', pass);
      start(trip);
    } catch (e) {
      errEl.textContent = '密碼錯誤';
      input.value = ''; input.focus();
      btn.disabled = false;
    }
  }

  form.addEventListener('submit', function (e) {
    e.preventDefault();
    if (input.value) attempt(input.value, remember.checked);
  });

  /* 入口畫面：一律先出現，即使已記住密碼也要按過「起動」才進入。
   * 記住的密碼在按下之後才拿去解密，行程在此之前不會出現在畫面上。 */
  var gate = document.getElementById('gate');
  var lock = document.getElementById('lock');
  var saved = localStorage.getItem(KEY_PREFIX + 'pass') || sessionStorage.getItem(KEY_PREFIX + 'pass');
  var entered = false;

  function enter() {
    if (entered) return;
    entered = true;
    gate.classList.add('out');
    setTimeout(function () {
      gate.remove();
      lock.hidden = false;
      if (saved) attempt(saved, !!localStorage.getItem(KEY_PREFIX + 'pass'));
      else input.focus();
    }, 380);                      /* 與 #gate 的 transition 時間一致 */
  }

  document.getElementById('gate-enter').addEventListener('click', enter);
});
