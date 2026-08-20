package com.tokyo2026.trip.ui

import androidx.compose.foundation.Canvas
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.tokyo2026.trip.data.Art

private data class Palette(val sky: List<Color>, val body: Color, val accent: Color, val ground: Color)

private fun paletteFor(art: Art): Palette = when (art) {
    Art.SKYTREE -> Palette(listOf(Color(0xFF2B3F7A), Color(0xFFE9827A)), Color(0xFFEFF3FF), Color(0xFF7FD4FF), Color(0xFF23305C))
    Art.TOWER -> Palette(listOf(Color(0xFF17203B), Color(0xFF3E2A55)), Color(0xFFFF7A4D), Color(0xFFFFD166), Color(0xFF121829))
    Art.TEMPLE, Art.GATE -> Palette(listOf(Color(0xFFFCE3C7), Color(0xFFF7B29A)), Color(0xFFC0392B), Color(0xFF7A2E22), Color(0xFF8C6239))
    Art.COASTER -> Palette(listOf(Color(0xFFBEE7F5), Color(0xFFF7DDA6)), Color(0xFFE0533C), Color(0xFF2F6D8C), Color(0xFF6F8F4C))
    Art.GACHA -> Palette(listOf(Color(0xFF3A2E5E), Color(0xFF7A4FA0)), Color(0xFFFFE3F2), Color(0xFFFF6FA5), Color(0xFF2A2044))
    Art.ANIME, Art.SHOP -> Palette(listOf(Color(0xFF241C3E), Color(0xFF5B3A8C)), Color(0xFF2FE0C8), Color(0xFFFFD84D), Color(0xFF1A1430))
    Art.FIREWORKS -> Palette(listOf(Color(0xFF08122E), Color(0xFF1D2E63)), Color(0xFFFF5F6D), Color(0xFFFFD86B), Color(0xFF060B1C))
    Art.BAR, Art.NIGHT -> Palette(listOf(Color(0xFF1A0E22), Color(0xFF5A1C46)), Color(0xFFFF4D8D), Color(0xFF57E5FF), Color(0xFF130A1A))
    Art.CAT -> Palette(listOf(Color(0xFFEFE0FA), Color(0xFFC9E8D8)), Color(0xFFFFFFFF), Color(0xFFE87A9B), Color(0xFF6E8F73))
    Art.GHIBLI -> Palette(listOf(Color(0xFF9AD3A6), Color(0xFFE7F3C6)), Color(0xFFB0512F), Color(0xFF3E6B4A), Color(0xFF4E7A48))
    Art.MARKET -> Palette(listOf(Color(0xFF2A2340), Color(0xFF8C4B3A)), Color(0xFFF4E2C4), Color(0xFFE05C3E), Color(0xFF1E1A2E))
    Art.PARK -> Palette(listOf(Color(0xFF7EC8E3), Color(0xFFD9F2C9)), Color(0xFF3E7C59), Color(0xFFFFC94D), Color(0xFF4E8A5F))
    Art.DISNEY -> Palette(listOf(Color(0xFF2A2E8C), Color(0xFFE86FA8)), Color(0xFFFFF6E5), Color(0xFF6FD3FF), Color(0xFF1F2270))
    Art.HOTEL -> Palette(listOf(Color(0xFF223A5E), Color(0xFF6E88AE)), Color(0xFFF5EFE3), Color(0xFFFFC85C), Color(0xFF16263F))
    Art.EXHIBIT -> Palette(listOf(Color(0xFF16324F), Color(0xFF2E7DA8)), Color(0xFFFFD84D), Color(0xFF6FE3C0), Color(0xFF102438))
    Art.MINIATURE -> Palette(listOf(Color(0xFF1B2A4A), Color(0xFF4E6FA8)), Color(0xFFFFE8B8), Color(0xFF7FE3A0), Color(0xFF14203A))
    Art.DREAM -> Palette(listOf(Color(0xFF2B1B4A), Color(0xFF8A4BA8)), Color(0xFFFFE1F0), Color(0xFF6FE0FF), Color(0xFF1E1236))
    Art.HILLS -> Palette(listOf(Color(0xFF1B2440), Color(0xFF556E9E)), Color(0xFFD9E4F2), Color(0xFF9BE8C4), Color(0xFF141B31))
    Art.ISLAND -> Palette(listOf(Color(0xFF6FC7E8), Color(0xFFFFE0A8)), Color(0xFF3F7A4E), Color(0xFFFFFFFF), Color(0xFF2E5F8C))
    Art.AQUARIUM -> Palette(listOf(Color(0xFF06355E), Color(0xFF17A0B8)), Color(0xFF9BE8FF), Color(0xFFFFD86B), Color(0xFF042844))
    Art.FERRIS -> Palette(listOf(Color(0xFF101C3A), Color(0xFF34407C)), Color(0xFF7FD4FF), Color(0xFFFF6FA5), Color(0xFF0B1428))
    Art.VIEW, Art.SKY -> Palette(listOf(Color(0xFF14213D), Color(0xFF9E4A5E)), Color(0xFFFFD9A0), Color(0xFF7FD4FF), Color(0xFF0E1830))
    Art.SHRINE -> Palette(listOf(Color(0xFFE8F0D8), Color(0xFFC5DCC0)), Color(0xFFFFFFFF), Color(0xFFD94F4F), Color(0xFF5E7A55))
    Art.GOV -> Palette(listOf(Color(0xFF141A33), Color(0xFF3A2C63)), Color(0xFFB9C6E0), Color(0xFF7FE3FF), Color(0xFF0E1226))
    Art.CROSSING -> Palette(listOf(Color(0xFF1A1D2E), Color(0xFF4A3A6E)), Color(0xFFF0EFEA), Color(0xFF57E5FF), Color(0xFF2A2A38))
    Art.DOG -> Palette(listOf(Color(0xFFCBD7E8), Color(0xFFE8DCC8)), Color(0xFF8C6A4A), Color(0xFF5A4632), Color(0xFF6E7A8C))
    Art.AIRPORT -> Palette(listOf(Color(0xFF2E5E8C), Color(0xFFBFD9E8)), Color(0xFFFFFFFF), Color(0xFFE05C5C), Color(0xFF44607A))
    Art.MUSEUM -> Palette(listOf(Color(0xFF1E4FA0), Color(0xFF7EC8F0)), Color(0xFFFFFFFF), Color(0xFFE84A4A), Color(0xFF17407F))
}

/** 全離線繪製的景點插畫：不使用任何網路圖片。 */
@Composable
fun SpotArt(art: Art, modifier: Modifier = Modifier) {
    val p = paletteFor(art)
    Canvas(modifier = modifier) {
        drawRect(brush = Brush.verticalGradient(p.sky))
        when (art) {
            Art.SKYTREE -> drawSkytree(p)
            Art.TOWER -> drawTower(p)
            Art.TEMPLE, Art.GATE -> drawGate(p)
            Art.COASTER -> drawCoaster(p)
            Art.GACHA -> drawGacha(p)
            Art.ANIME -> drawAnime(p)
            Art.SHOP -> drawShop(p)
            Art.FIREWORKS -> drawFireworks(p)
            Art.BAR, Art.NIGHT -> drawNeon(p)
            Art.CAT -> drawCat(p)
            Art.GHIBLI -> drawGhibli(p)
            Art.MARKET -> drawMarket(p)
            Art.PARK -> drawPark(p)
            Art.DISNEY -> drawCastle(p)
            Art.HOTEL -> drawHotel(p)
            Art.EXHIBIT -> drawExhibit(p)
            Art.MINIATURE -> drawMiniature(p)
            Art.DREAM -> drawDream(p)
            Art.HILLS -> drawHills(p)
            Art.ISLAND -> drawIsland(p)
            Art.AQUARIUM -> drawAquarium(p)
            Art.FERRIS -> drawFerris(p)
            Art.VIEW -> drawView(p)
            Art.SKY -> drawSky(p)
            Art.SHRINE -> drawShrine(p)
            Art.GOV -> drawGov(p)
            Art.CROSSING -> drawCrossing(p)
            Art.DOG -> drawDog(p)
            Art.AIRPORT -> drawAirport(p)
            Art.MUSEUM -> drawMuseum(p)
        }
    }
}

private fun DrawScope.ground(p: Palette, top: Float = 0.82f) {
    drawRect(color = p.ground, topLeft = Offset(0f, size.height * top), size = Size(size.width, size.height * (1 - top)))
}

private fun DrawScope.sun(p: Palette, cx: Float, cy: Float, r: Float) {
    drawCircle(color = p.accent.copy(alpha = 0.35f), radius = r * 1.7f, center = Offset(cx, cy))
    drawCircle(color = p.accent, radius = r, center = Offset(cx, cy))
}

private fun DrawScope.drawSkytree(p: Palette) {
    val w = size.width; val h = size.height
    sun(p, w * 0.78f, h * 0.3f, h * 0.07f)
    ground(p)
    val cx = w * 0.38f
    val path = Path().apply {
        moveTo(cx - w * 0.10f, h * 0.86f)
        lineTo(cx - w * 0.022f, h * 0.16f)
        lineTo(cx + w * 0.022f, h * 0.16f)
        lineTo(cx + w * 0.10f, h * 0.86f)
        close()
    }
    drawPath(path, p.body.copy(alpha = 0.95f))
    drawRect(p.accent, topLeft = Offset(cx - w * 0.055f, h * 0.40f), size = Size(w * 0.11f, h * 0.05f))
    drawRect(p.accent, topLeft = Offset(cx - w * 0.042f, h * 0.27f), size = Size(w * 0.084f, h * 0.04f))
    drawLine(p.body, Offset(cx, h * 0.16f), Offset(cx, h * 0.06f), strokeWidth = 4f)
    for (i in 0..6) {
        val x = w * (0.62f + i * 0.055f)
        drawRect(p.ground.copy(alpha = 0.9f), Offset(x, h * (0.62f + (i % 3) * 0.05f)), Size(w * 0.04f, h))
    }
}

private fun DrawScope.drawTower(p: Palette) {
    val w = size.width; val h = size.height
    ground(p)
    val cx = w * 0.5f
    val leg = Path().apply {
        moveTo(cx - w * 0.20f, h * 0.86f); lineTo(cx - w * 0.03f, h * 0.20f)
        lineTo(cx + w * 0.03f, h * 0.20f); lineTo(cx + w * 0.20f, h * 0.86f)
        lineTo(cx + w * 0.14f, h * 0.86f); lineTo(cx, h * 0.30f)
        lineTo(cx - w * 0.14f, h * 0.86f); close()
    }
    drawPath(leg, p.body)
    drawRect(p.body, Offset(cx - w * 0.11f, h * 0.52f), Size(w * 0.22f, h * 0.055f))
    drawRect(p.accent, Offset(cx - w * 0.06f, h * 0.30f), Size(w * 0.12f, h * 0.035f))
    drawLine(p.accent, Offset(cx, h * 0.20f), Offset(cx, h * 0.08f), strokeWidth = 5f)
    for (i in 0..4) drawCircle(p.accent.copy(alpha = 0.7f), 3f, Offset(w * (0.12f + i * 0.19f), h * 0.13f))
}

private fun DrawScope.drawGate(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.85f)
    drawRect(p.body, Offset(w * 0.16f, h * 0.30f), Size(w * 0.09f, h * 0.55f))
    drawRect(p.body, Offset(w * 0.75f, h * 0.30f), Size(w * 0.09f, h * 0.55f))
    drawRect(p.body, Offset(w * 0.10f, h * 0.24f), Size(w * 0.80f, h * 0.09f))
    drawRect(p.accent, Offset(w * 0.08f, h * 0.18f), Size(w * 0.84f, h * 0.06f))
    drawCircle(p.accent, h * 0.16f, Offset(w * 0.5f, h * 0.56f))
    drawRect(p.body.copy(alpha = 0.9f), Offset(w * 0.44f, h * 0.40f), Size(w * 0.12f, h * 0.32f))
}

private fun DrawScope.drawCoaster(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.84f)
    val rail = Path().apply {
        moveTo(0f, h * 0.72f)
        cubicTo(w * 0.2f, h * 0.20f, w * 0.42f, h * 0.86f, w * 0.62f, h * 0.44f)
        cubicTo(w * 0.78f, h * 0.14f, w * 0.9f, h * 0.70f, w, h * 0.52f)
    }
    drawPath(rail, p.accent, style = Stroke(width = 7f))
    for (i in 0..5) {
        val x = w * (0.08f + i * 0.17f)
        drawLine(p.accent.copy(alpha = 0.5f), Offset(x, h * 0.86f), Offset(x, h * 0.45f), strokeWidth = 4f)
    }
    drawRect(p.body, Offset(w * 0.24f, h * 0.50f), Size(w * 0.11f, h * 0.07f))
    drawCircle(p.body, h * 0.03f, Offset(w * 0.30f, h * 0.46f))
}

private fun DrawScope.drawGacha(p: Palette) {
    val w = size.width; val h = size.height
    for (i in 0..2) for (j in 0..1) {
        val x = w * (0.16f + i * 0.28f); val y = h * (0.24f + j * 0.36f)
        drawCircle(p.body.copy(alpha = 0.95f), h * 0.13f, Offset(x, y))
        drawArc(p.accent, 180f, 180f, true, Offset(x - h * 0.13f, y - h * 0.13f), Size(h * 0.26f, h * 0.26f))
        drawCircle(p.accent.copy(alpha = 0.8f), h * 0.03f, Offset(x, y + h * 0.06f))
    }
}

private fun DrawScope.drawAnime(p: Palette) {
    val w = size.width; val h = size.height
    for (i in 0..3) {
        val x = w * (0.08f + i * 0.24f)
        drawRect(p.body.copy(alpha = 0.25f + i * 0.15f), Offset(x, h * (0.30f + i % 2 * 0.08f)), Size(w * 0.17f, h * 0.5f))
    }
    drawCircle(p.accent, h * 0.10f, Offset(w * 0.5f, h * 0.28f))
    drawLine(p.accent, Offset(w * 0.1f, h * 0.82f), Offset(w * 0.9f, h * 0.82f), strokeWidth = 5f)
    for (i in 0..8) drawCircle(p.accent.copy(alpha = 0.6f), 3f, Offset(w * (0.08f + i * 0.105f), h * 0.16f))
}

private fun DrawScope.drawShop(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.86f)
    drawRect(p.body.copy(alpha = 0.9f), Offset(w * 0.14f, h * 0.28f), Size(w * 0.72f, h * 0.58f))
    for (i in 0..2) for (j in 0..1)
        drawRect(p.accent.copy(alpha = 0.8f), Offset(w * (0.20f + i * 0.22f), h * (0.36f + j * 0.20f)), Size(w * 0.14f, h * 0.13f))
    drawRect(p.accent, Offset(w * 0.10f, h * 0.22f), Size(w * 0.80f, h * 0.07f))
}

private fun DrawScope.drawFireworks(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.80f)
    fun burst(cx: Float, cy: Float, r: Float, c: Color) {
        for (i in 0 until 20) {
            val a = (Math.PI * 2 * i / 20).toFloat()
            val sx = cx + kotlin.math.cos(a) * r * 0.25f
            val sy = cy + kotlin.math.sin(a) * r * 0.25f
            drawLine(c, Offset(sx, sy), Offset(cx + kotlin.math.cos(a) * r, cy + kotlin.math.sin(a) * r), strokeWidth = 3f)
            drawCircle(c, 3f, Offset(cx + kotlin.math.cos(a) * r, cy + kotlin.math.sin(a) * r))
        }
    }
    burst(w * 0.32f, h * 0.34f, h * 0.20f, p.body)
    burst(w * 0.68f, h * 0.26f, h * 0.15f, p.accent)
    burst(w * 0.55f, h * 0.52f, h * 0.10f, p.body.copy(alpha = 0.7f))
    drawRect(p.accent.copy(alpha = 0.25f), Offset(0f, h * 0.80f), Size(w, h * 0.2f))
}

private fun DrawScope.drawNeon(p: Palette) {
    val w = size.width; val h = size.height
    for (i in 0..4) {
        val x = w * (0.06f + i * 0.19f)
        drawRect(p.accent.copy(alpha = 0.25f), Offset(x, h * (0.2f + (i % 3) * 0.1f)), Size(w * 0.13f, h * 0.8f))
        drawRect(p.body.copy(alpha = 0.85f), Offset(x + w * 0.02f, h * (0.3f + (i % 3) * 0.1f)), Size(w * 0.03f, h * 0.4f))
    }
    drawCircle(p.body.copy(alpha = 0.35f), h * 0.18f, Offset(w * 0.5f, h * 0.35f))
}

private fun DrawScope.drawCat(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.86f)
    val cx = w * 0.5f; val cy = h * 0.56f; val r = h * 0.20f
    drawCircle(p.body, r, Offset(cx, cy))
    val ear = Path().apply {
        moveTo(cx - r * 0.85f, cy - r * 0.55f); lineTo(cx - r * 0.55f, cy - r * 1.25f); lineTo(cx - r * 0.20f, cy - r * 0.85f); close()
        moveTo(cx + r * 0.85f, cy - r * 0.55f); lineTo(cx + r * 0.55f, cy - r * 1.25f); lineTo(cx + r * 0.20f, cy - r * 0.85f); close()
    }
    drawPath(ear, p.body)
    drawCircle(p.ground, r * 0.12f, Offset(cx - r * 0.35f, cy - r * 0.1f))
    drawCircle(p.ground, r * 0.12f, Offset(cx + r * 0.35f, cy - r * 0.1f))
    drawCircle(p.accent, r * 0.10f, Offset(cx, cy + r * 0.18f))
    drawArc(p.accent, 0f, 180f, false, Offset(cx - r * 0.9f, cy - r * 0.3f), Size(r * 1.8f, r * 1.5f), style = Stroke(3f))
}

private fun DrawScope.drawGhibli(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.80f)
    drawRect(p.body, Offset(w * 0.22f, h * 0.38f), Size(w * 0.56f, h * 0.44f))
    val roof = Path().apply { moveTo(w * 0.16f, h * 0.40f); lineTo(w * 0.5f, h * 0.16f); lineTo(w * 0.84f, h * 0.40f); close() }
    drawPath(roof, p.accent)
    drawCircle(p.accent.copy(alpha = 0.9f), h * 0.07f, Offset(w * 0.5f, h * 0.55f))
    for (i in 0..3) drawCircle(p.ground.copy(alpha = 0.55f), h * (0.05f + i * 0.01f), Offset(w * (0.08f + i * 0.28f), h * 0.86f))
    drawLine(p.ground, Offset(w * 0.72f, h * 0.30f), Offset(w * 0.72f, h * 0.12f), strokeWidth = 5f)
    drawCircle(p.body, h * 0.045f, Offset(w * 0.72f, h * 0.10f))
}

private fun DrawScope.drawMarket(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.84f)
    for (i in 0..2) {
        val x = w * (0.06f + i * 0.31f)
        drawRect(p.body.copy(alpha = 0.92f), Offset(x, h * 0.42f), Size(w * 0.26f, h * 0.42f))
        val roof = Path().apply { moveTo(x - w * 0.02f, h * 0.44f); lineTo(x + w * 0.13f, h * 0.26f); lineTo(x + w * 0.28f, h * 0.44f); close() }
        drawPath(roof, p.accent)
        drawCircle(p.accent, h * 0.05f, Offset(x + w * 0.13f, h * 0.36f))
    }
    drawLine(p.accent.copy(alpha = 0.6f), Offset(0f, h * 0.22f), Offset(w, h * 0.22f), strokeWidth = 3f)
}

private fun DrawScope.drawPark(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.78f)
    drawRect(p.body.copy(alpha = 0.85f), Offset(w * 0.10f, h * 0.52f), Size(w * 0.80f, h * 0.26f))
    drawRect(p.accent, Offset(w * 0.10f, h * 0.46f), Size(w * 0.80f, h * 0.07f))
    for (i in 0..4) {
        val x = w * (0.16f + i * 0.17f)
        drawCircle(p.body, h * 0.07f, Offset(x, h * 0.36f))
        drawLine(p.ground, Offset(x, h * 0.46f), Offset(x, h * 0.38f), strokeWidth = 4f)
    }
}

private fun DrawScope.drawCastle(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.85f)
    fun spire(cx: Float, base: Float, wd: Float, top: Float) {
        drawRect(p.body, Offset(cx - wd / 2, base), Size(wd, h * 0.85f - base))
        val roof = Path().apply { moveTo(cx - wd * 0.75f, base); lineTo(cx, top); lineTo(cx + wd * 0.75f, base); close() }
        drawPath(roof, p.accent)
    }
    spire(w * 0.28f, h * 0.52f, w * 0.12f, h * 0.34f)
    spire(w * 0.72f, h * 0.52f, w * 0.12f, h * 0.34f)
    spire(w * 0.50f, h * 0.40f, w * 0.20f, h * 0.12f)
    drawRect(p.body, Offset(w * 0.34f, h * 0.62f), Size(w * 0.32f, h * 0.23f))
    drawArc(p.accent, 180f, 180f, true, Offset(w * 0.44f, h * 0.68f), Size(w * 0.12f, h * 0.2f))
    for (i in 0..5) drawCircle(Color.White.copy(alpha = 0.8f), 2.5f, Offset(w * (0.1f + i * 0.16f), h * 0.2f))
}

private fun DrawScope.drawHotel(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.86f)
    drawRect(p.body.copy(alpha = 0.95f), Offset(w * 0.20f, h * 0.24f), Size(w * 0.60f, h * 0.62f))
    for (r in 0..3) for (c in 0..3)
        drawRect(p.accent.copy(alpha = if ((r + c) % 2 == 0) 0.9f else 0.35f), Offset(w * (0.26f + c * 0.13f), h * (0.32f + r * 0.13f)), Size(w * 0.08f, h * 0.08f))
    drawRect(p.ground, Offset(w * 0.44f, h * 0.72f), Size(w * 0.12f, h * 0.14f))
}

private fun DrawScope.drawExhibit(p: Palette) {
    val w = size.width; val h = size.height
    drawRect(p.ground.copy(alpha = 0.6f), Offset(0f, h * 0.72f), Size(w, h * 0.28f))
    for (i in 0..2) {
        val x = w * (0.10f + i * 0.30f)
        drawRect(p.body.copy(alpha = 0.9f), Offset(x, h * (0.24f + i % 2 * 0.08f)), Size(w * 0.22f, h * 0.34f))
        drawCircle(p.accent, h * 0.05f, Offset(x + w * 0.11f, h * (0.41f + i % 2 * 0.08f)))
    }
    for (i in 0..3) drawLine(p.accent.copy(alpha = 0.5f), Offset(w * (0.05f + i * 0.3f), 0f), Offset(w * (0.15f + i * 0.3f), h * 0.2f), strokeWidth = 3f)
}

private fun DrawScope.drawMiniature(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.80f)
    for (i in 0..7) {
        val x = w * (0.06f + i * 0.115f)
        val bh = h * (0.14f + (i % 4) * 0.10f)
        drawRect(p.body.copy(alpha = 0.9f), Offset(x, h * 0.80f - bh), Size(w * 0.075f, bh))
    }
    drawCircle(p.accent, h * 0.06f, Offset(w * 0.82f, h * 0.24f))
    drawLine(p.accent, Offset(w * 0.0f, h * 0.88f), Offset(w, h * 0.88f), strokeWidth = 4f)
}

private fun DrawScope.drawDream(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.84f)
    drawRect(p.body.copy(alpha = 0.9f), Offset(w * 0.16f, h * 0.30f), Size(w * 0.68f, h * 0.54f))
    drawArc(p.accent, 180f, 180f, true, Offset(w * 0.16f, h * 0.16f), Size(w * 0.68f, h * 0.28f))
    for (i in 0..4) drawCircle(p.accent.copy(alpha = 0.75f), h * 0.035f, Offset(w * (0.24f + i * 0.13f), h * 0.52f))
    drawRect(p.accent.copy(alpha = 0.5f), Offset(w * 0.30f, h * 0.62f), Size(w * 0.40f, h * 0.22f))
}

private fun DrawScope.drawHills(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.86f)
    drawRect(p.body.copy(alpha = 0.95f), Offset(w * 0.36f, h * 0.10f), Size(w * 0.26f, h * 0.76f))
    drawRect(p.body.copy(alpha = 0.7f), Offset(w * 0.10f, h * 0.42f), Size(w * 0.22f, h * 0.44f))
    drawRect(p.body.copy(alpha = 0.7f), Offset(w * 0.66f, h * 0.34f), Size(w * 0.24f, h * 0.52f))
    drawRect(p.accent.copy(alpha = 0.9f), Offset(w * 0.36f, h * 0.30f), Size(w * 0.26f, h * 0.03f))
    for (i in 0..3) drawCircle(p.accent.copy(alpha = 0.6f), h * 0.04f, Offset(w * (0.14f + i * 0.24f), h * 0.86f))
}

private fun DrawScope.drawIsland(p: Palette) {
    val w = size.width; val h = size.height
    drawRect(p.ground, Offset(0f, h * 0.66f), Size(w, h * 0.34f))
    val isl = Path().apply {
        moveTo(w * 0.18f, h * 0.70f)
        cubicTo(w * 0.34f, h * 0.34f, w * 0.62f, h * 0.36f, w * 0.82f, h * 0.70f)
        close()
    }
    drawPath(isl, p.body)
    drawRect(p.accent, Offset(w * 0.47f, h * 0.24f), Size(w * 0.06f, h * 0.26f))
    drawCircle(p.accent, h * 0.045f, Offset(w * 0.5f, h * 0.22f))
    for (i in 0..3) drawLine(p.accent.copy(alpha = 0.5f), Offset(w * (0.05f + i * 0.28f), h * (0.80f + i % 2 * 0.05f)), Offset(w * (0.20f + i * 0.28f), h * (0.80f + i % 2 * 0.05f)), strokeWidth = 3f)
}

private fun DrawScope.drawAquarium(p: Palette) {
    val w = size.width; val h = size.height
    for (i in 0..3) drawArc(p.accent.copy(alpha = 0.25f), 200f, 140f, false, Offset(-w * 0.2f + i * w * 0.2f, h * 0.1f), Size(w * 0.9f, h * 0.9f), style = Stroke(4f))
    fun fish(cx: Float, cy: Float, s: Float, c: Color) {
        val body = Path().apply {
            moveTo(cx - s, cy); cubicTo(cx - s * 0.3f, cy - s * 0.7f, cx + s * 0.3f, cy - s * 0.7f, cx + s, cy)
            cubicTo(cx + s * 0.3f, cy + s * 0.7f, cx - s * 0.3f, cy + s * 0.7f, cx - s, cy); close()
        }
        drawPath(body, c)
        val tail = Path().apply { moveTo(cx - s, cy); lineTo(cx - s * 1.6f, cy - s * 0.5f); lineTo(cx - s * 1.6f, cy + s * 0.5f); close() }
        drawPath(tail, c)
        drawCircle(p.ground, s * 0.14f, Offset(cx + s * 0.45f, cy - s * 0.12f))
    }
    fish(w * 0.34f, h * 0.42f, h * 0.13f, p.body)
    fish(w * 0.68f, h * 0.66f, h * 0.09f, p.accent)
    fish(w * 0.76f, h * 0.28f, h * 0.06f, p.body.copy(alpha = 0.8f))
}

private fun DrawScope.drawFerris(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.84f)
    val cx = w * 0.5f; val cy = h * 0.44f; val r = h * 0.32f
    drawCircle(p.body.copy(alpha = 0.9f), r, Offset(cx, cy), style = Stroke(4f))
    drawCircle(p.body, r * 0.6f, Offset(cx, cy), style = Stroke(2f))
    for (i in 0 until 12) {
        val a = (Math.PI * 2 * i / 12).toFloat()
        val x = cx + kotlin.math.cos(a) * r; val y = cy + kotlin.math.sin(a) * r
        drawLine(p.body.copy(alpha = 0.5f), Offset(cx, cy), Offset(x, y), strokeWidth = 2f)
        drawCircle(if (i % 2 == 0) p.accent else p.body, h * 0.028f, Offset(x, y))
    }
    drawLine(p.body, Offset(cx - w * 0.10f, h * 0.84f), Offset(cx, cy), strokeWidth = 5f)
    drawLine(p.body, Offset(cx + w * 0.10f, h * 0.84f), Offset(cx, cy), strokeWidth = 5f)
}

private fun DrawScope.drawView(p: Palette) {
    val w = size.width; val h = size.height
    sun(p, w * 0.74f, h * 0.34f, h * 0.06f)
    drawRect(p.ground, Offset(0f, h * 0.62f), Size(w, h * 0.38f))
    for (i in 0..6) {
        val x = w * (0.04f + i * 0.14f); val bh = h * (0.16f + (i % 3) * 0.14f)
        drawRect(p.body.copy(alpha = 0.85f), Offset(x, h * 0.62f - bh), Size(w * 0.10f, bh))
        for (j in 0..2) drawRect(p.accent.copy(alpha = 0.6f), Offset(x + w * 0.02f, h * 0.62f - bh + h * (0.03f + j * 0.05f)), Size(w * 0.06f, h * 0.02f))
    }
}

private fun DrawScope.drawSky(p: Palette) {
    val w = size.width; val h = size.height
    sun(p, w * 0.2f, h * 0.24f, h * 0.07f)
    drawRect(p.ground, Offset(0f, h * 0.70f), Size(w, h * 0.30f))
    drawRect(p.body.copy(alpha = 0.95f), Offset(w * 0.30f, h * 0.30f), Size(w * 0.40f, h * 0.56f))
    drawRect(p.accent.copy(alpha = 0.9f), Offset(w * 0.28f, h * 0.26f), Size(w * 0.44f, h * 0.045f))
    for (r in 0..3) drawLine(p.ground.copy(alpha = 0.4f), Offset(w * 0.30f, h * (0.40f + r * 0.11f)), Offset(w * 0.70f, h * (0.40f + r * 0.11f)), strokeWidth = 3f)
    drawCircle(p.accent, h * 0.03f, Offset(w * 0.5f, h * 0.22f))
}

private fun DrawScope.drawShrine(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.84f)
    val cx = w * 0.5f; val cy = h * 0.55f; val r = h * 0.22f
    drawCircle(p.body, r, Offset(cx, cy))
    val ear = Path().apply {
        moveTo(cx - r * 0.8f, cy - r * 0.6f); lineTo(cx - r * 0.5f, cy - r * 1.25f); lineTo(cx - r * 0.15f, cy - r * 0.9f); close()
        moveTo(cx + r * 0.8f, cy - r * 0.6f); lineTo(cx + r * 0.5f, cy - r * 1.25f); lineTo(cx + r * 0.15f, cy - r * 0.9f); close()
    }
    drawPath(ear, p.body)
    drawCircle(p.accent, r * 0.10f, Offset(cx - r * 0.32f, cy - r * 0.12f))
    drawCircle(p.accent, r * 0.10f, Offset(cx + r * 0.32f, cy - r * 0.12f))
    drawRect(p.accent, Offset(cx - r * 0.45f, cy + r * 0.30f), Size(r * 0.9f, r * 0.22f))
    drawCircle(p.body, r * 0.30f, Offset(cx + r * 0.95f, cy - r * 0.55f))
}

private fun DrawScope.drawGov(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.86f)
    drawRect(p.body.copy(alpha = 0.9f), Offset(w * 0.24f, h * 0.20f), Size(w * 0.14f, h * 0.66f))
    drawRect(p.body.copy(alpha = 0.9f), Offset(w * 0.62f, h * 0.20f), Size(w * 0.14f, h * 0.66f))
    drawRect(p.body.copy(alpha = 0.75f), Offset(w * 0.38f, h * 0.44f), Size(w * 0.24f, h * 0.42f))
    for (i in 0..5) drawLine(p.accent.copy(alpha = 0.7f), Offset(w * 0.24f, h * (0.26f + i * 0.10f)), Offset(w * 0.76f, h * (0.26f + i * 0.10f)), strokeWidth = 2f)
    drawRect(p.accent.copy(alpha = 0.35f), Offset(w * 0.38f, h * 0.44f), Size(w * 0.24f, h * 0.42f))
}

private fun DrawScope.drawCrossing(p: Palette) {
    val w = size.width; val h = size.height
    drawRect(p.ground, Offset(0f, h * 0.45f), Size(w, h * 0.55f))
    for (i in 0..7) drawRect(p.body.copy(alpha = 0.85f), Offset(w * (0.02f + i * 0.125f), h * 0.52f), Size(w * 0.075f, h * 0.42f))
    for (i in 0..5) {
        val x = w * (0.12f + i * 0.15f); val y = h * (0.60f + (i % 3) * 0.10f)
        drawCircle(p.ground.copy(alpha = 0.9f), h * 0.035f, Offset(x, y - h * 0.05f))
        drawRect(p.ground.copy(alpha = 0.9f), Offset(x - w * 0.018f, y - h * 0.02f), Size(w * 0.036f, h * 0.10f))
    }
    for (i in 0..3) drawRect(p.accent.copy(alpha = 0.5f), Offset(w * (0.05f + i * 0.26f), h * (0.10f + i % 2 * 0.08f)), Size(w * 0.16f, h * 0.10f))
}

private fun DrawScope.drawDog(p: Palette) {
    val w = size.width; val h = size.height
    drawRect(p.ground, Offset(0f, h * 0.78f), Size(w, h * 0.22f))
    val cx = w * 0.5f; val cy = h * 0.50f; val r = h * 0.17f
    drawCircle(p.body, r, Offset(cx, cy))
    val ear = Path().apply {
        moveTo(cx - r * 0.85f, cy - r * 0.5f); lineTo(cx - r * 0.6f, cy - r * 1.3f); lineTo(cx - r * 0.15f, cy - r * 0.85f); close()
        moveTo(cx + r * 0.85f, cy - r * 0.5f); lineTo(cx + r * 0.6f, cy - r * 1.3f); lineTo(cx + r * 0.15f, cy - r * 0.85f); close()
    }
    drawPath(ear, p.body)
    drawCircle(p.accent, r * 0.12f, Offset(cx - r * 0.35f, cy - r * 0.1f))
    drawCircle(p.accent, r * 0.12f, Offset(cx + r * 0.35f, cy - r * 0.1f))
    drawCircle(p.accent, r * 0.14f, Offset(cx, cy + r * 0.25f))
    drawRect(p.body.copy(alpha = 0.85f), Offset(cx - r * 0.9f, cy + r * 0.9f), Size(r * 1.8f, r * 1.0f))
    drawRect(p.accent.copy(alpha = 0.4f), Offset(w * 0.30f, h * 0.78f), Size(w * 0.40f, h * 0.06f))
}

private fun DrawScope.drawAirport(p: Palette) {
    val w = size.width; val h = size.height
    drawRect(p.ground, Offset(0f, h * 0.72f), Size(w, h * 0.28f))
    val plane = Path().apply {
        moveTo(w * 0.20f, h * 0.52f); lineTo(w * 0.78f, h * 0.36f); lineTo(w * 0.86f, h * 0.42f); lineTo(w * 0.36f, h * 0.62f); close()
    }
    drawPath(plane, p.body)
    val wing = Path().apply { moveTo(w * 0.44f, h * 0.46f); lineTo(w * 0.56f, h * 0.22f); lineTo(w * 0.64f, h * 0.26f); lineTo(w * 0.56f, h * 0.50f); close() }
    drawPath(wing, p.body.copy(alpha = 0.85f))
    drawCircle(p.accent, h * 0.03f, Offset(w * 0.74f, h * 0.40f))
    drawLine(Color.White.copy(alpha = 0.6f), Offset(0f, h * 0.84f), Offset(w, h * 0.84f), strokeWidth = 5f)
    for (i in 0..5) drawRect(p.accent.copy(alpha = 0.7f), Offset(w * (0.06f + i * 0.18f), h * 0.90f), Size(w * 0.08f, h * 0.02f))
}

private fun DrawScope.drawMuseum(p: Palette) {
    val w = size.width; val h = size.height
    ground(p, 0.84f)
    drawCircle(p.body, h * 0.22f, Offset(w * 0.5f, h * 0.46f))
    drawCircle(p.accent, h * 0.055f, Offset(w * 0.5f, h * 0.60f))
    drawRect(p.accent, Offset(w * 0.5f - h * 0.14f, h * 0.36f), Size(h * 0.28f, h * 0.05f))
    drawCircle(p.ground, h * 0.035f, Offset(w * 0.5f - h * 0.07f, h * 0.44f))
    drawCircle(p.ground, h * 0.035f, Offset(w * 0.5f + h * 0.07f, h * 0.44f))
    val bell = Path().apply {
        moveTo(w * 0.18f, h * 0.84f); cubicTo(w * 0.18f, h * 0.62f, w * 0.34f, h * 0.62f, w * 0.34f, h * 0.84f); close()
    }
    drawPath(bell, p.accent.copy(alpha = 0.85f))
    drawRect(p.accent.copy(alpha = 0.85f), Offset(w * 0.68f, h * 0.60f), Size(w * 0.14f, h * 0.24f))
}
