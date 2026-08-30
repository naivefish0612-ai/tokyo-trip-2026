#!/usr/bin/env python3
"""從 trip-data.json 產出可由 GitHub Pages 靜態代管的 docs/。

    python3 web/build.py --passphrase '<密碼>'

行程資料（trip-data.json）與密碼（web/.passphrase）都不在版控內：
repo 是公開的，明文行程一旦進去就等於公開了不在家的日期與每晚住宿。
兩者都必須手動攜帶，不要經由 GitHub 傳遞。

只依賴 Python 3 與 cryptography。照片檔名改為以密碼推導的 salt 加鹽雜湊，
未解密者無法從檔名反推行程結構。
"""
import argparse
import base64
import hashlib
import json
import os
import re
import secrets
import shutil
import sys
import urllib.parse

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
WEB = os.path.join(ROOT, 'web')
DOCS = os.path.join(ROOT, 'docs')
DATA = os.path.join(ROOT, 'trip-data.json')
PHOTOS_SRC = os.path.join(WEB, 'photos')

ITERATIONS = 600_000            # OWASP 對 PBKDF2-SHA256 的建議下限
ALPHABET = 'abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789'  # 去掉易混淆字元


def make_passphrase(n=16):
    return ''.join(secrets.choice(ALPHABET) for _ in range(n))


def check_css_classes():
    """app.js 用到卻沒在 CSS 定義的 class 會讓圖片以原生尺寸撐破版面。

    `.thumb` 就是這樣漏掉的：它以 photoEl(spot, 'thumb') 的參數傳入，
    不是寫成 class: '...'，掃描時很容易忽略。
    """
    js = open(os.path.join(WEB, 'app.js'), encoding='utf-8').read()
    css = open(os.path.join(WEB, 'style.css'), encoding='utf-8').read()
    used = set()
    for m in re.findall(r"class:\s*'([^']+)'", js):
        used.update(m.split())
    for m in re.findall(r"photoEl\([^,)]+,\s*'([^']+)'", js):
        used.update(m.split())
    defined = set(re.findall(r'\.([A-Za-z][\w-]*)', css))
    missing = sorted(c for c in used if c not in defined)
    if missing:
        sys.exit('CSS 缺少這些 class（會導致版面跑掉）: ' + ', '.join(missing))
    return len(used)


def spots(trip):
    for day in trip['days']:
        for stop in day['stops']:
            yield day, stop, stop['spot']


def hhmm(t):
    h, m = t.split(':')
    return int(h) * 60 + int(m)


def clock(x):
    return '%02d:%02d' % (x // 60, x % 60)


def stay_minutes(s):
    """把「1.5 小時」「40 分」轉成分鐘；「全日」「住宿」這類回 None 表示不參與檢查。"""
    if not s:
        return None
    m = re.search(r'([\d.]+)\s*小時', s)
    if m:
        return int(float(m.group(1)) * 60)
    m = re.search(r'(\d+)\s*分', s)
    if m:
        return int(m.group(1))
    return None


def validate(trip):
    """取代原本 Robolectric 測試中的資料一致性檢查。"""
    problems, ids = [], []
    for day, stop, sp in spots(trip):
        ids.append(sp['id'])
        for field in ('id', 'nameZh', 'nameJa', 'hours', 'stay', 'area'):
            if not sp.get(field):
                problems.append(f"{sp.get('id', '?')} 缺少 {field}")
        if sp.get('tier') not in ('A', 'B', 'C'):
            problems.append(f"{sp.get('id', '?')} 的 tier 必須是 A／B／C，目前是 {sp.get('tier')!r}")
        if not (20 < sp.get('lat', 0) and 120 < sp.get('lng', 0)):
            problems.append(f"{sp['id']} 座標不合理")
        if not sp.get('notes'):
            problems.append(f"{sp['id']} 沒有在地筆記")
        if sp.get('photo') and not os.path.exists(os.path.join(PHOTOS_SRC, sp['photo'])):
            problems.append(f"{sp['id']} 找不到照片 {sp['photo']}")
    dupes = {i for i in ids if ids.count(i) > 1}
    if dupes:
        problems.append('景點 id 重複: ' + ', '.join(sorted(dupes)))
    check_ids = [c['id'] for c in trip['checklist']]
    if set(check_ids) & set(ids):
        problems.append('清單 id 與景點 id 衝突')

    # 排班可行性：上一站的停留＋車程不能超過下一站的抵達時間。
    # 這類錯誤靠肉眼看不出來——Day 8 曾把機場抵達寫成 12:45，實際最早 13:40。
    for day in trip['days']:
        st = day['stops']
        for i in range(len(st) - 1):
            a, b = st[i], st[i + 1]
            hold = stay_minutes(a['spot'].get('stay'))
            ride = (b.get('leg') or {}).get('minutes')
            if hold is None or ride is None:
                continue
            earliest = hhmm(a['time']) + hold + ride
            if earliest > hhmm(b['time']):
                problems.append(
                    'Day %d %s 停留 %d 分＋車程 %d 分，最早 %s 才到 %s，但排 %s'
                    % (day['n'], a['spot']['nameZh'], hold, ride,
                       clock(earliest), b['spot']['nameZh'], b['time']))
        declared = day.get('moveMinutes')
        actual = sum((s.get('leg') or {}).get('minutes', 0) for s in st)
        if declared is not None and declared != actual:
            problems.append('Day %d moveMinutes 宣告 %d，實際 %d' % (day['n'], declared, actual))

    if problems:
        sys.exit('資料檢查未通過:\n  ' + '\n  '.join(problems))
    return len(ids)


def derive_leave_times(trip):
    """反推每站的最晚離開時間 = 下一站抵達時間 − 該段車程。

    景點若自帶 mustLeaveBy（閉園、末班車這類硬限制）就以它為準。
    """
    n = 0
    for day in trip['days']:
        st = day['stops']
        for i, stop in enumerate(st):
            sp = stop['spot']
            if sp.get('mustLeaveBy'):
                n += 1
                continue
            if i + 1 >= len(st):
                continue
            ride = (st[i + 1].get('leg') or {}).get('minutes')
            if ride is None:
                continue
            sp['mustLeaveBy'] = clock(hhmm(st[i + 1]['time']) - ride)
            n += 1
    return n


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--passphrase')
    args = ap.parse_args()

    n_cls = check_css_classes()
    if not os.path.exists(DATA):
        sys.exit('找不到 %s。這個檔案不在版控內，需從原機器複製過來。' % DATA)

    from cryptography.hazmat.primitives.ciphers.aead import AESGCM

    passphrase = args.passphrase or make_passphrase()
    trip = json.load(open(DATA, encoding='utf-8'))
    n_spots = validate(trip)
    n_leave = derive_leave_times(trip)

    # ---- 分類標籤與地圖連結 ----
    # catLabel 存成一份對照表而非每個景點複製一次；地圖用日文地標名而非座標，
    # Google Maps 才會顯示店名與營業資訊。
    labels = trip.get('catLabels', {})
    for _, _, sp in spots(trip):
        sp['catLabel'] = labels.get(sp['cat'], sp['cat'])
        q = urllib.parse.quote_plus(sp.get('mapQuery') or sp['nameJa'])
        sp['mapUrl'] = 'https://www.google.com/maps/search/?api=1&query=' + q
        sp['navUrl'] = ('https://www.google.com/maps/dir/?api=1&destination=' + q
                        + '&travelmode=transit')

    # ---- 照片改名：sha256(salt + spotId) 前 16 hex，對應關係只存在於密文內 ----
    # salt 由密碼推導而非隨機產生，同一組密碼重建時檔名才不會變動。
    photo_salt = hashlib.sha256(b'photo-salt|' + passphrase.encode('utf-8')).digest()[:16]
    out_photos = os.path.join(DOCS, 'p')
    shutil.rmtree(out_photos, ignore_errors=True)
    os.makedirs(out_photos, exist_ok=True)

    copied = 0
    for _, _, sp in spots(trip):
        if not sp.get('photo'):
            continue
        h = hashlib.sha256(photo_salt + sp['id'].encode()).hexdigest()[:16]
        new = 'p/%s.jpg' % h
        dst = os.path.join(DOCS, new)
        if not os.path.exists(dst):
            shutil.copy2(os.path.join(PHOTOS_SRC, sp['photo']), dst)
            copied += 1
        sp['photo'] = new

    # ---- 加密 ----
    plaintext = json.dumps(trip, ensure_ascii=False, separators=(',', ':')).encode('utf-8')
    salt = secrets.token_bytes(16)
    key = hashlib.pbkdf2_hmac('sha256', passphrase.encode('utf-8'), salt, ITERATIONS, dklen=32)
    iv = secrets.token_bytes(12)
    ct = AESGCM(key).encrypt(iv, plaintext, None)   # GCM tag 已附加在密文尾端

    os.makedirs(DOCS, exist_ok=True)
    with open(os.path.join(DOCS, 'data.enc.json'), 'w') as f:
        json.dump({
            'v': 1, 'kdf': 'PBKDF2-SHA256', 'iterations': ITERATIONS, 'cipher': 'AES-256-GCM',
            'salt': base64.b64encode(salt).decode(),
            'iv': base64.b64encode(iv).decode(),
            'ct': base64.b64encode(ct).decode(),
        }, f)

    for name in ('index.html', 'style.css', 'app.js', 'hero.svg'):
        shutil.copy2(os.path.join(WEB, name), os.path.join(DOCS, name))
    open(os.path.join(DOCS, '.nojekyll'), 'w').close()
    with open(os.path.join(DOCS, 'robots.txt'), 'w') as f:
        f.write('User-agent: *\nDisallow: /\n')

    if not args.passphrase:
        with open(os.path.join(WEB, '.passphrase'), 'w') as f:
            f.write(passphrase + '\n')

    print('資料檢查 : %d 個景點、%d 個 CSS class 全部通過' % (n_spots, n_cls))
    print('最晚離開 : %d 站已標定' % n_leave)
    print('明文     : %d bytes' % len(plaintext))
    print('密文     : %d bytes' % len(ct))
    print('照片     : %d 張 -> docs/p/' % copied)
    print('PASSPHRASE=%s' % passphrase)


if __name__ == '__main__':
    main()
