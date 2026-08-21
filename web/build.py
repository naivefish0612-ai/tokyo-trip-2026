#!/usr/bin/env python3
"""把匯出的行程 JSON 加密、照片改名，產出可直接由 GitHub Pages 靜態代管的 docs/。

    python3 web/build.py [--passphrase XXX]

沒有指定密碼時會產生一組強密碼並印出（同時寫入 gitignored 的 web/.passphrase）。
照片檔名改為以隨機 salt 加鹽的雜湊值，避免未解密者從檔名推回行程結構。
"""
import argparse
import base64
import hashlib
import json
import os
import secrets
import shutil
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
WEB = os.path.join(ROOT, 'web')
DOCS = os.path.join(ROOT, 'docs')
DATA = os.path.join(ROOT, 'web-build', 'data.json')
PHOTOS_SRC = os.path.join(ROOT, 'app', 'src', 'main', 'assets', 'photos')

ITERATIONS = 600_000            # OWASP 對 PBKDF2-SHA256 的建議下限
ALPHABET = 'abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789'  # 去掉易混淆字元


def make_passphrase(n=16):
    return ''.join(secrets.choice(ALPHABET) for _ in range(n))


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument('--passphrase')
    args = ap.parse_args()

    if not os.path.exists(DATA):
        sys.exit('缺少 %s，請先執行：./gradlew testDebugUnitTest --tests "*ExportWebData*"' % DATA)

    from cryptography.hazmat.primitives.ciphers.aead import AESGCM

    passphrase = args.passphrase or make_passphrase()
    trip = json.load(open(DATA, encoding='utf-8'))

    # ---- 照片改名：sha256(salt + spotId) 前 16 hex，對應關係只存在於密文內 ----
    photo_salt = secrets.token_bytes(16)
    out_photos = os.path.join(DOCS, 'p')
    shutil.rmtree(out_photos, ignore_errors=True)
    os.makedirs(out_photos, exist_ok=True)

    renamed = 0
    for day in trip['days']:
        for stop in day['stops']:
            spot = stop['spot']
            if not spot.get('photo'):
                continue
            src = os.path.join(PHOTOS_SRC, os.path.basename(spot['photo']))
            if not os.path.exists(src):
                spot['photo'] = None
                continue
            h = hashlib.sha256(photo_salt + spot['id'].encode()).hexdigest()[:16]
            new = 'p/%s.jpg' % h
            dst = os.path.join(DOCS, new)
            if not os.path.exists(dst):
                shutil.copy2(src, dst)
                renamed += 1
            spot['photo'] = new

    # ---- 加密 ----
    plaintext = json.dumps(trip, ensure_ascii=False, separators=(',', ':')).encode('utf-8')
    salt = secrets.token_bytes(16)
    key = hashlib.pbkdf2_hmac('sha256', passphrase.encode('utf-8'), salt, ITERATIONS, dklen=32)
    iv = secrets.token_bytes(12)
    ct = AESGCM(key).encrypt(iv, plaintext, None)   # GCM tag 已附加在密文尾端

    blob = {
        'v': 1,
        'kdf': 'PBKDF2-SHA256',
        'iterations': ITERATIONS,
        'cipher': 'AES-256-GCM',
        'salt': base64.b64encode(salt).decode(),
        'iv': base64.b64encode(iv).decode(),
        'ct': base64.b64encode(ct).decode(),
    }

    os.makedirs(DOCS, exist_ok=True)
    with open(os.path.join(DOCS, 'data.enc.json'), 'w') as f:
        json.dump(blob, f)

    # ---- 靜態檔 ----
    for name in ('index.html', 'style.css', 'app.js'):
        shutil.copy2(os.path.join(WEB, name), os.path.join(DOCS, name))

    # GitHub Pages 不要跑 Jekyll
    open(os.path.join(DOCS, '.nojekyll'), 'w').close()
    with open(os.path.join(DOCS, 'robots.txt'), 'w') as f:
        f.write('User-agent: *\nDisallow: /\n')

    if not args.passphrase:
        with open(os.path.join(WEB, '.passphrase'), 'w') as f:
            f.write(passphrase + '\n')

    print('明文     : %d bytes' % len(plaintext))
    print('密文     : %d bytes' % len(ct))
    print('照片     : %d 張 -> docs/p/' % renamed)
    print('迭代次數 : %d' % ITERATIONS)
    print('PASSPHRASE=%s' % passphrase)


if __name__ == '__main__':
    main()
