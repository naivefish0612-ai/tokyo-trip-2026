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
  alert: { icon: ICON.alert, color: 'var(--secondary)', label: '今日提醒' }
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
  var hue = 0; for (var i = 0; i < spot.id.length; i++) hue = (hue * 31 + spot.id.charCodeAt(i)) % 360;
  return el('div', {
    class: (cls ? cls + ' ' : '') + 'noimg',
    style: 'background:linear-gradient(150deg,hsl(' + hue + ',42%,38%),hsl(' + ((hue + 45) % 360) + ',48%,58%))'
  });
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
/** 有色系圖示的提示清單。取代先前用「！」「◆」等字元當項目符號的做法。 */
function noteList(items, kind) {
  var k = KIND[kind] || KIND.note;
  return el('ul', { class: 'notes' }, items.map(function (t) {
    var ic = svgIcon(k.icon, 'ni');
    ic.style.setProperty('fill', k.color);
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

  frag.appendChild(el('div', { class: 'stats' }, [
    el('div', { class: 'stat tap', onclick: function () { go('#/checklist'); } },
      [ring(done, ids.length), el('span', { text: '打卡' })]),
    el('div', { class: 'stat' },
      [el('b', { class: 'accent-gold', text: String(ids.length) }), el('span', { text: '景點' })]),
    el('div', { class: 'stat' },
      [el('b', { class: 'accent-pink', text: TRIP.hotels.length + ' 家' }), el('span', { text: '住宿' })])
  ]));

  frag.appendChild(card('航班', 'var(--secondary)', [
    bullets(['去程　' + TRIP.flightOut, '回程　' + TRIP.flightBack]),
    el('p', { class: 'hint', text: '落地已晚，Day 1 不排景點；Day 8 中午須離開市區。' })
  ]));

  frag.appendChild(el('div', { class: 'section-title', text: '每日行程' }));

  TRIP.days.forEach(function (d) {
    var dayIds = d.stops.map(function (s) { return s.spot.id; });
    var last = d.stops[d.stops.length - 1].spot;
    frag.appendChild(el('div', { class: 'card tap', onclick: function () { go('#/day/' + d.n); } }, [
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

  frag.appendChild(el('div', { class: 'section-title', text: '交通總覽' }));
  frag.appendChild(card('每日移動時間', 'var(--primary)', TRIP.days.map(function (d) {
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

  frag.appendChild(card('住宿', 'var(--tertiary)',
    [bullets(TRIP.hotels.map(function (h) { return h.when + '　' + h.name; }))]));

  frag.appendChild(el('p', {
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

  frag.appendChild(el('div', { class: 'scroll-x', style: 'padding:12px 16px 0' }, [
    pill(d.stops.length + ' 站', 'var(--surface-variant)', 'var(--on-surface-variant)'),
    pill('移動 ' + d.moveMinutes + ' 分', 'color-mix(in srgb, var(--primary) 12%, transparent)', 'var(--primary)')
  ]));

  if (d.alerts.length) frag.appendChild(expandable('alert', d.alerts.length, [noteList(d.alerts, 'alert')], true));

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
        el('div', { style: 'font-weight:700;margin-top:6px', text: st.spot.nameZh }),
        el('div', { style: 'font-size:.78rem;color:var(--on-surface-variant)', text: st.spot.nameJa + '　停留 ' + st.spot.stay })
      ])
    ]));
  });
  frag.appendChild(el('div', { class: 'card' }, [
    el('div', { class: 'card-pad', style: 'padding-bottom:4px' },
      [el('div', { class: 'card-title', text: '行程動線' })]),
    tl
  ]));

  if (d.tips.length) frag.appendChild(expandable('tip', d.tips.length, [noteList(d.tips, 'tip')]));
  frag.appendChild(card('今晚住宿', 'var(--primary)', [
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
  frag.appendChild(el('div', {
    class: 'hero' + (spot.photo ? ' zoomable' : ''),
    onclick: function () {
      if (spot.photo) lightbox(spot.photo, spot.nameZh, c0 ? '照片：' + c0.author + '／' + c0.license : null);
    }
  }, [
    photoEl(spot), el('div', { class: 'scrim' }),
    el('div', { class: 'cap' }, [el('h2', { text: spot.nameJa }), el('p', { text: spot.kana })]),
    day ? el('div', { class: 'corner' }, [pill('Day ' + day.n + '・' + day.date, 'rgba(0,0,0,.45)', '#fff')]) : null
  ]));

  var acts = [
    el('a', { class: 'btn', href: spot.navUrl, target: '_blank', rel: 'noopener noreferrer' },
      [svgIcon(ICON.nav), document.createTextNode('導航')]),
    el('a', { class: 'btn outline', href: spot.mapUrl, target: '_blank', rel: 'noopener noreferrer' },
      [svgIcon(ICON.map), document.createTextNode('地圖')])
  ];
  if (spot.official) acts.push(el('a', { class: 'btn gold', href: spot.official, target: '_blank', rel: 'noopener noreferrer' },
    [svgIcon(ICON.web), document.createTextNode('官網')]));
  frag.appendChild(el('div', { class: 'actions' }, acts));

  frag.appendChild(card('重點', 'var(--primary)', [factGrid([
    { icon: ICON.clock, label: '時間', value: spot.hours, color: 'var(--primary)' },
    { icon: ICON.yen, label: '費用', value: spot.price, color: 'var(--eat)' },
    { icon: ICON.hourglass, label: '停留', value: spot.stay, color: 'var(--primary)' },
    { icon: ICON.closed, label: '公休', value: spot.closed, color: 'var(--secondary)' },
    { icon: ICON.ticket, label: '預約', value: spot.booking, color: 'var(--tertiary)' }
  ])]));

  if (spot.notes.length) frag.appendChild(expandable('note', spot.notes.length, [noteList(spot.notes, 'note')], true));
  if (spot.eats.length) frag.appendChild(expandable('eat', spot.eats.length, [noteList(spot.eats, 'eat')]));
  if (spot.warns.length) frag.appendChild(expandable('warn', spot.warns.length, [noteList(spot.warns, 'warn')]));

  var c = TRIP.photoCredits[spot.id];
  if (c) frag.appendChild(el('p', { class: 'credit' }, [
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
  var cats = [{ k: 'ALL', l: '全部' }, { k: 'FAV', l: '收藏' }];
  var seenCat = {};
  all.forEach(function (s) { if (!seenCat[s.cat]) { seenCat[s.cat] = 1; cats.push({ k: s.cat, l: s.catLabel }); } });

  var frag = document.createDocumentFragment();
  var list = el('div');
  var countLbl = el('div', { class: 'foot', style: 'padding:6px 20px 0' });

  function render() {
    var q = spotsState.q.trim().toLowerCase();
    var out = all.filter(function (s) {
      if (spotsState.cat === 'FAV' && !Store.isFav(s.id)) return false;
      if (spotsState.cat !== 'ALL' && spotsState.cat !== 'FAV' && s.cat !== spotsState.cat) return false;
      if (!q) return true;
      return (s.nameZh + ' ' + s.nameJa + ' ' + s.kana + ' ' + s.area).toLowerCase().indexOf(q) >= 0;
    });
    countLbl.textContent = out.length + ' 個景點';
    list.textContent = '';
    out.forEach(function (s) {
      list.appendChild(el('div', { class: 'card', style: 'margin:8px 16px' }, [
        el('div', { class: 'spot-row', style: 'cursor:pointer', onclick: function () { go('#/spot/' + s.id); } }, [
          photoEl(s),
          el('div', { class: 'meta' }, [
            el('b', { text: s.nameZh }),
            el('small', { text: s.nameJa + '　' + s.area }),
            el('div', { style: 'margin-top:4px' }, [
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
    frag.appendChild(el('div', { class: 'card' }, [
      el('div', { class: 'card-pad', style: 'padding-bottom:0' }, [el('div', { class: 'card-title', text: g })]),
      body
    ]));
  });

  frag.appendChild(el('div', { class: 'section-title', text: '必訂票券' }));
  TRIP.bookings.forEach(function (b) {
    frag.appendChild(card(b.name, 'var(--tertiary)', [
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

  var saved = localStorage.getItem(KEY_PREFIX + 'pass') || sessionStorage.getItem(KEY_PREFIX + 'pass');
  if (saved) attempt(saved, !!localStorage.getItem(KEY_PREFIX + 'pass'));
  else input.focus();
});
