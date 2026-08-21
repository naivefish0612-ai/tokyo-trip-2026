package com.tokyo2026.trip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokyo2026.trip.data.Trip
import com.tokyo2026.trip.store.TripStore

@Composable
fun HomeScreen(store: TripStore, onDay: (Int) -> Unit, onSpot: (String) -> Unit, onChecklist: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    val ids = Trip.allSpots.map { it.id }
    val done = store.countChecked(ids)

    LazyColumn(Modifier.fillMaxSize().background(cs.background), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Box(Modifier.fillMaxWidth().height(210.dp)) {
                SpotHero(Trip.days[0].stops[1].spot, Modifier.matchParentSize())
                Column(Modifier.align(Alignment.BottomStart).padding(20.dp)) {
                    Text(Trip.title, color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(Trip.subtitle, color = Color.White.copy(alpha = 0.92f), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        item {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatTile("打卡", "$done/${ids.size}", cs.primary, Modifier.weight(1f)) { onChecklist() }
                StatTile("景點", "${ids.size}", cs.tertiary, Modifier.weight(1f)) { }
                StatTile("住宿", "4 家", cs.secondary, Modifier.weight(1f)) { }
            }
        }

        item {
            Card(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { onChecklist() },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant)
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = cs.primary)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("行前準備與必訂票券", fontWeight = FontWeight.Bold)
                        Text("吉卜力已訂 9/27 14:00", style = MaterialTheme.typography.bodySmall, color = cs.onSurfaceVariant)
                    }
                    Icon(Icons.Default.ChevronRight, null)
                }
            }
        }

        item {
            InfoCard("航班", accent = cs.secondary) {
                Bullets(listOf("去程　${Trip.flightOut}", "回程　${Trip.flightBack}"))
                Spacer(Modifier.height(6.dp))
                Text(
                    "去程 20:50 落地，當天不排景點；回程 17:55 起飛，Day 8 中午就得離開市區。",
                    style = MaterialTheme.typography.bodySmall, color = cs.onSurfaceVariant
                )
            }
        }

        item { SectionTitle("每日行程") }

        items(Trip.days) { day ->
            val dayIds = day.stops.map { it.spot.id }
            val dayDone = store.countChecked(dayIds)
            Card(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { onDay(day.n) },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = cs.surface)
            ) {
                Column {
                    Box(Modifier.fillMaxWidth().height(130.dp)) {
                        SpotHero(day.stops.last().spot, Modifier.matchParentSize())
                        Row(Modifier.align(Alignment.BottomStart).padding(12.dp), verticalAlignment = Alignment.Bottom) {
                            Text("DAY ${day.n}", color = Color.White, style = MaterialTheme.typography.labelLarge)
                            Spacer(Modifier.width(8.dp))
                            Text("${day.date} ${day.weekday}", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        if (day.alerts.isNotEmpty()) {
                            Box(Modifier.align(Alignment.TopEnd).padding(10.dp)) {
                                Pill("⚠ ${day.alerts.size}", Color.Black.copy(alpha = 0.45f), Color.White)
                            }
                        }
                    }
                    Column(Modifier.padding(14.dp)) {
                        Text(day.theme, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(day.summary, style = MaterialTheme.typography.bodySmall, color = cs.onSurfaceVariant, maxLines = 2)
                        Spacer(Modifier.height(10.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            item { Pill("${day.stops.size} 站", cs.surfaceVariant, cs.onSurfaceVariant) }
                            item { Pill("移動 ${day.moveMinutes} 分", cs.primary.copy(alpha = 0.12f), cs.primary) }
                            item { Pill("打卡 $dayDone", cs.tertiary.copy(alpha = 0.18f), cs.onSurface) }
                        }
                        Spacer(Modifier.height(10.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(day.stops) { st ->
                                Column(
                                    Modifier.width(96.dp).clip(RoundedCornerShape(12.dp)).clickable { onSpot(st.spot.id) }
                                ) {
                                    SpotHero(st.spot, Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(12.dp)), scrim = false)
                                    Text(st.spot.nameZh, style = MaterialTheme.typography.labelSmall, maxLines = 2,
                                        modifier = Modifier.padding(top = 4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        item { SectionTitle("交通總覽") }
        item {
            InfoCard("每日移動時間", accent = cs.primary) {
                // 以最長的一天為基準等比縮放；weight 必須恆大於 0，否則 Compose 會丟 IllegalArgumentException。
                val maxMove = Trip.days.maxOf { it.moveMinutes }.coerceAtLeast(1)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Trip.days.forEach { d ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("D${d.n}", Modifier.width(34.dp), style = MaterialTheme.typography.labelLarge, color = cs.primary)
                            val frac = (d.moveMinutes.toFloat() / maxMove).coerceIn(0.04f, 1f)
                            Box(
                                Modifier.height(8.dp).weight(frac)
                                    .clip(RoundedCornerShape(4.dp)).background(cs.primary.copy(alpha = 0.75f))
                            )
                            if (frac < 1f) Spacer(Modifier.weight(1f - frac))
                            Spacer(Modifier.width(8.dp))
                            Text("${d.moveMinutes} 分", style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        item {
            InfoCard("住宿", accent = cs.tertiary) { Bullets(Trip.hotels.map { "${it.first}　${it.second}" }) }
        }

        item {
            Text(
                "資料查證於 2026/8：景點營業時間、票價可能再變動，出發前請點各景點的「官網」按鈕確認。照片來自 Wikimedia Commons（CC／公有領域）。",
                Modifier.padding(20.dp), style = MaterialTheme.typography.bodySmall, color = cs.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatTile(label: String, value: String, accent: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier.clickable { onClick() }, shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = accent)
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(text, Modifier.padding(start = 20.dp, top = 18.dp, bottom = 4.dp),
        style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
}
