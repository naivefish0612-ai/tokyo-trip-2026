package com.tokyo2026.trip.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.util.LruCache
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.tokyo2026.trip.data.Spot

/**
 * 照片快取上限約為可用堆積的 1/8。先前用不會淘汰的 HashMap，
 * 37 張照片全部解碼後常駐約 49MB，在小 heap 的裝置上會 OutOfMemoryError。
 */
private val cache = object : LruCache<String, ImageBitmap>(
    (Runtime.getRuntime().maxMemory() / 8)
        .coerceIn(4L * 1024 * 1024, 32L * 1024 * 1024).toInt()
) {
    override fun sizeOf(key: String, value: ImageBitmap): Int = value.width * value.height * 4
}

private fun loadPhoto(ctx: Context, path: String): ImageBitmap? {
    cache.get(path)?.let { return it }
    val bmp = runCatching {
        ctx.assets.open(path).use { BitmapFactory.decodeStream(it) }?.asImageBitmap()
    }.getOrNull() ?: return null
    cache.put(path, bmp)
    return bmp
}

/** 讀取打包在 assets 的實景照片；沒有照片的景點自動退回向量插畫。 */
@Composable
fun SpotHero(spot: Spot, modifier: Modifier = Modifier, scrim: Boolean = true) {
    val ctx = LocalContext.current
    val path = spot.photo
    val bmp = remember(path) { if (path == null) null else loadPhoto(ctx, path) }
    Box(modifier) {
        if (bmp != null) {
            Image(bmp, contentDescription = spot.nameZh, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            if (scrim) {
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.45f to Color.Black.copy(alpha = 0.15f),
                            1f to Color.Black.copy(alpha = 0.62f)
                        )
                    )
                )
            }
        } else {
            SpotArt(spot.art, Modifier.fillMaxSize())
        }
    }
}
