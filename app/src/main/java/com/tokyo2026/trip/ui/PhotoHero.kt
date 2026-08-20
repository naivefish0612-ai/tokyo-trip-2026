package com.tokyo2026.trip.ui

import android.graphics.BitmapFactory
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

private val cache = HashMap<String, ImageBitmap?>()

/** 讀取打包在 assets 的實景照片；沒有照片的景點自動退回向量插畫。 */
@Composable
fun SpotHero(spot: Spot, modifier: Modifier = Modifier, scrim: Boolean = true) {
    val ctx = LocalContext.current
    val path = spot.photo
    val bmp = remember(path) {
        if (path == null) null else cache.getOrPut(path) {
            runCatching {
                ctx.assets.open(path).use { BitmapFactory.decodeStream(it) }?.asImageBitmap()
            }.getOrNull()
        }
    }
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
