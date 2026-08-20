package com.tokyo2026.trip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokyo2026.trip.data.Day
import com.tokyo2026.trip.store.TripStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayScreen(day: Day, store: TripStore, onBack: () -> Unit, onSpot: (String) -> Unit) {
    val cs = MaterialTheme.colorScheme
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Day ${day.n}　${day.date}（${day.weekday}）・移動 ${day.moveMinutes} 分", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(day.theme, style = MaterialTheme.typography.bodySmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回") }
                }
            )
        }
    ) { pad ->
        LazyColumn(
            Modifier.fillMaxSize().padding(pad).background(cs.background),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            if (day.alerts.isNotEmpty()) item {
                Card(
                    Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.secondary.copy(alpha = 0.12f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("⚠ 本日重要提醒", fontWeight = FontWeight.Bold, color = cs.secondary)
                        Spacer(Modifier.height(8.dp))
                        Bullets(day.alerts, color = cs.onSurface)
                    }
                }
            }

            item {
                InfoCard("今晚住宿", accent = cs.tertiary) {
                    Text(day.hotelName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(6.dp))
                    Text(day.hotelNote, style = MaterialTheme.typography.bodyMedium, color = cs.onSurfaceVariant)
                }
            }

            if (day.tips.isNotEmpty()) item {
                ExpandableCard("在地達人的排法", day.tips.size, cs.tertiary, initiallyOpen = true) { Bullets(day.tips) }
            }

            item {
                Text("行程動線", Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            day.stops.forEach { stop ->
                item {
                    stop.leg?.let { leg ->
                        Row(
                            Modifier.fillMaxWidth().padding(start = 32.dp, end = 20.dp, top = 4.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.DirectionsTransit, null, tint = cs.primary, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text("${leg.mode}　約 ${leg.minutes} 分　${leg.fare}",
                                    style = MaterialTheme.typography.labelMedium, color = cs.primary, fontWeight = FontWeight.Bold)
                                Text(leg.route, style = MaterialTheme.typography.bodySmall, color = cs.onSurfaceVariant)
                            }
                        }
                    }
                    val checked = store.isChecked(stop.spot.id)
                    Card(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { onSpot(stop.spot.id) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = cs.surface)
                    ) {
                        Column {
                            Box(Modifier.fillMaxWidth().height(120.dp)) {
                                SpotHero(stop.spot, Modifier.matchParentSize())
                                Box(
                                    Modifier.align(Alignment.TopStart).padding(10.dp)
                                        .clip(CircleShape).background(Color.Black.copy(alpha = 0.45f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) { Text(stop.time, color = Color.White, style = MaterialTheme.typography.labelMedium) }
                                IconButton(
                                    onClick = { store.toggle(stop.spot.id) },
                                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
                                ) {
                                    Icon(
                                        if (checked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = "打卡",
                                        tint = if (checked) Color(0xFF4CD07D) else Color.White
                                    )
                                }
                            }
                            Column(Modifier.padding(14.dp)) {
                                Text(stop.spot.nameZh, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(stop.spot.nameJa, style = MaterialTheme.typography.bodySmall, color = cs.onSurfaceVariant)
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Pill(stop.spot.cat.label, cs.surfaceVariant, cs.onSurfaceVariant)
                                    Pill("停留 ${stop.spot.stay}", cs.primary.copy(alpha = 0.12f), cs.primary)
                                    if (stop.spot.booking != null) Pill("需預約", cs.secondary.copy(alpha = 0.15f), cs.secondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
