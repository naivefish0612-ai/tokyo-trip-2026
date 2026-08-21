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
  check: 'M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm-2 15-5-5 1.4-1.4L10 14.2l7.6-7.6L19 8l-9 9z',
  circle: 'M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm0 2a8 8 0 1 1 0 16 8 8 0 0 1 0-16z',
  nav: 'M12 2 3 21l9-4 9 4L12 2z',
  map: 'M20 4l-6 2-4-2-6 2v14l6-2 4 2 6-2V4zm-6 14.5-4-2V5.5l4 2v11z',
  web: 'M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm7.9 9h-3a15 15 0 0 0-1.2-5.2A8 8 0 0 1 19.9 11zM12 4.2c.8 1.1 1.5 3 1.7 6.8h-3.4c.2-3.8.9-5.7 1.7-6.8zM4.1 13h3a15 15 0 0 0 1.2 5.2A8 8 0 0 1 4.1 13zm3-2h-3a8 8 0 0 1 4.2-5.2A15 15 0 0 0 7.1 11zm4.9 8.8c-.8-1.1-1.5-3-1.7-6.8h3.4c-.2 3.8-.9 5.7-1.7 6.8zm3.7-.6a15 15 0 0 0 1.2-5.2h3a8 8 0 0 1-4.2 5.2z',
  chev: 'M12 15.4 6.6 10 8 8.6l4 4 4-4L17.4 10z',
  tick: 'M9 16.2 4.8 12l-1.4 1.4L9 19 21 7l-1.4-1.4z'
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
function pill(text, bg, fg) {
  return el('span', { class: 'pill', text: text, style: 'background:' + bg + ';color:' + fg });
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
function expandable(titleText, count, accent, bodyNodes, open) {
  return el('details', { class: 'card exp', style: '--accent:' + accent, open: !!open }, [
    el('summary', {}, [
      document.createTextNode(titleText),
      pill(String(count), 'color-mix(in srgb, ' + accent + ' 16%, transparent)', accent),
      svgIcon(ICON.chev, 'chev')
    ]),
    el('div', { class: 'body' }, bodyNodes)
  ]);
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

  var heroSpot = TRIP.days[0].stops[1] ? TRIP.days[0].stops[1].spot : TRIP.days[0].stops[0].spot;
  frag.appendChild(el('div', { class: 'hero' }, [
    photoEl(heroSpot), el('div', { class: 'scrim' }),
    el('div', { class: 'cap' }, [el('h1', { text: TRIP.title }), el('p', { text: TRIP.subtitle })])
  ]));

  frag.appendChild(el('div', { class: 'stats' }, [
    el('div', { class: 'stat tap', onclick: function () { go('#/checklist'); } },
      [el('b', { text: done + '/' + ids.length, style: 'color:var(--primary)' }), el('span', { text: '打卡' })]),
    el('div', { class: 'stat' },
      [el('b', { text: String(ids.length), style: 'color:var(--tertiary)' }), el('span', { text: '景點' })]),
    el('div', { class: 'stat' },
      [el('b', { text: TRIP.hotels.length + ' 家', style: 'color:var(--secondary)' }), el('span', { text: '住宿' })])
  ]));

  frag.appendChild(card('航班', 'var(--secondary)', [
    bullets(['去程　' + TRIP.flightOut, '回程　' + TRIP.flightBack]),
    el('p', { class: 'foot', style: 'padding:10px 0 0', text: '去程 20:50 落地，當天不排景點；回程 17:55 起飛，Day 8 中午就得離開市區。' })
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
        el('div', { style: 'font-size:.84rem;color:var(--on-surface-variant)', text: d.summary }),
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
    return el('div', { class: 'row', style: 'margin-bottom:8px' }, [
      el('span', { style: 'width:34px;font-weight:700;color:var(--primary);font-size:.8rem', text: 'D' + d.n }),
      el('div', { class: 'bar-track' }, [el('div', { class: 'bar', style: 'width:' + pct + '%' })]),
      el('span', { style: 'font-size:.78rem;color:var(--on-surface-variant);width:52px;text-align:right', text: d.moveMinutes + ' 分' })
    ]);
  })));

  frag.appendChild(card('住宿', 'var(--tertiary)',
    [bullets(TRIP.hotels.map(function (h) { return h.when + '　' + h.name; }))]));

  frag.appendChild(el('p', {
    class: 'foot',
    text: '資料查證於 2026/8：景點營業時間、票價可能再變動，出發前請點各景點的「官網」按鈕確認。照片來自 Wikimedia Commons（CC／公有領域）。'
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
    el('div', { class: 'cap' }, [el('h2', { text: d.theme }), el('p', { text: d.summary })])
  ]));

  frag.appendChild(el('div', { class: 'scroll-x', style: 'padding:12px 16px 0' }, [
    pill(d.stops.length + ' 站', 'var(--surface-variant)', 'var(--on-surface-variant)'),
    pill('移動 ' + d.moveMinutes + ' 分', 'color-mix(in srgb, var(--primary) 12%, transparent)', 'var(--primary)')
  ]));

  if (d.alerts.length) frag.appendChild(card('今日提醒', 'var(--secondary)', [bullets(d.alerts, '！')]));

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

  if (d.tips.length) frag.appendChild(expandable('今日小訣竅', d.tips.length, 'var(--tertiary)', [bullets(d.tips)], true));
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

  frag.appendChild(topbar(spot.nameZh, [favBtn, chkBtn]));
  frag.appendChild(el('div', { class: 'hero' }, [
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

  var key = ['時間　' + spot.hours, '費用　' + spot.price, '停留　' + spot.stay + '　｜　公休　' + spot.closed];
  if (spot.booking) key.push('預約　' + spot.booking);
  frag.appendChild(card('重點', 'var(--primary)', [bullets(key)]));

  if (spot.notes.length) frag.appendChild(expandable('在地達人筆記', spot.notes.length, 'var(--tertiary)', [bullets(spot.notes)], true));
  if (spot.eats.length) frag.appendChild(expandable('必吃必買', spot.eats.length, '#E0A32E', [bullets(spot.eats, '◆')]));
  if (spot.warns.length) frag.appendChild(expandable('注意與避雷', spot.warns.length, 'var(--secondary)', [bullets(spot.warns, '！')]));

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

/* ---------- 路由 ---------- */
function go(hash) { location.hash = hash; }
function render() {
  var h = location.hash || '#/home';
  var view = document.getElementById('view');
  view.textContent = '';
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
