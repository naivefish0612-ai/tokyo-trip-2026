package com.tokyo2026.trip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokyo2026.trip.data.Spot
import com.tokyo2026.trip.data.Trip
import com.tokyo2026.trip.data.photoCredits
import com.tokyo2026.trip.store.TripStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotScreen(spot: Spot, store: TripStore, onBack: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    val ctx = LocalContext.current
    val checked = store.isChecked(spot.id)
    val day = Trip.dayOf(spot.id)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(spot.nameZh, maxLines = 1, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回") } },
                actions = {
                    IconButton({ store.toggleFav(spot.id) }) {
                        Icon(if (store.isFav(spot.id)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            "收藏", tint = if (store.isFav(spot.id)) cs.secondary else cs.onSurfaceVariant)
                    }
                    IconButton({ store.toggle(spot.id) }) {
                        Icon(if (checked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            "打卡", tint = if (checked) Color(0xFF2FA85F) else cs.onSurfaceVariant)
                    }
                }
            )
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).background(cs.background).verticalScroll(rememberScrollState())) {
            Box(Modifier.fillMaxWidth().height(210.dp)) {
                SpotHero(spot, Modifier.matchParentSize())
                Column(Modifier.align(Alignment.BottomStart).padding(18.dp)) {
                    Text(spot.nameJa, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(spot.kana, color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
                }
                if (day != null) Box(Modifier.align(Alignment.TopEnd).padding(12.dp)) {
                    Pill("Day ${day.n}・${day.date}", Color.Black.copy(alpha = 0.45f), Color.White)
                }
            }

            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button({ openUrl(ctx, spot.navUrl) }, Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) {
                    Icon(Icons.Default.Navigation, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("導航")
                }
                OutlinedButton({ openUrl(ctx, spot.mapUrl) }, Modifier.weight(1f), shape = RoundedCornerShape(14.dp)) {
                    Icon(Icons.Default.Map, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("地圖")
                }
                spot.official?.let { url ->
                    Button({ openUrl(ctx, url) }, Modifier.weight(1f), shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = cs.tertiary, contentColor = Color.Black)) {
                        Icon(Icons.Default.Public, null, Modifier.size(18.dp)); Spacer(Modifier.width(6.dp)); Text("官網")
                    }
                }
            }

            InfoCard("重點") {
                Bullets(
                    listOfNotNull(
                        "時間　${spot.hours}",
                        "費用　${spot.price}",
                        "停留　${spot.stay}　｜　公休　${spot.closed}",
                        spot.booking?.let { "預約　$it" }
                    )
                )
            }

            if (spot.notes.isNotEmpty())
                ExpandableCard("在地達人筆記", spot.notes.size, cs.tertiary, initiallyOpen = true) { Bullets(spot.notes) }
            if (spot.eats.isNotEmpty())
                ExpandableCard("必吃必買", spot.eats.size, Color(0xFFE0A32E)) { Bullets(spot.eats, bullet = "◆") }
            if (spot.warns.isNotEmpty())
                ExpandableCard("注意與避雷", spot.warns.size, cs.secondary) { Bullets(spot.warns, bullet = "！") }

            photoCredits[spot.id]?.let { c ->
                TextButton({ openUrl(ctx, c.page) }, Modifier.padding(start = 20.dp)) {
                    Text("照片：${c.author}／${c.license}（Wikimedia Commons）",
                        style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
