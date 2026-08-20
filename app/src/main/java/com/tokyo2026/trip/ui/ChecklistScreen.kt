package com.tokyo2026.trip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokyo2026.trip.data.Trip
import com.tokyo2026.trip.store.TripStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(store: TripStore, onBack: () -> Unit) {
    val cs = MaterialTheme.colorScheme
    val ids = Trip.checklist.map { it.id }
    val done = store.countChecked(ids)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("行前準備", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回") } }
            )
        }
    ) { pad ->
        LazyColumn(
            Modifier.fillMaxSize().padding(pad).background(cs.background),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Card(
                    Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("$done / ${ids.size} 項已完成", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { if (ids.isEmpty()) 0f else done.toFloat() / ids.size },
                            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }

            item {
                InfoCard("照片授權") {
                    Text(
                        "App 內 ${Trip.photoCount} 張實景照片取自 Wikimedia Commons，皆為 CC 或公有領域授權；" +
                            "各景點頁下方可點開原始出處與作者。少數尚無自由授權照片的景點則以自製插畫呈現。",
                        style = MaterialTheme.typography.bodySmall, color = cs.onSurfaceVariant
                    )
                }
            }

            Trip.checklist.groupBy { it.group }.forEach { (group, items) ->
                item {
                    Text(group, Modifier.padding(start = 20.dp, top = 16.dp, bottom = 4.dp),
                        style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = cs.primary)
                }
                items.forEach { item ->
                    item {
                        Row(
                            Modifier.fillMaxWidth().clickable { store.toggle(item.id) }
                                .padding(horizontal = 12.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = store.isChecked(item.id), onCheckedChange = { store.toggle(item.id) })
                            Text(item.text, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            item {
                ExpandableCard("出發前必訂票券", Trip.bookings.size, cs.secondary, initiallyOpen = true) {
                    Bullets(Trip.bookings.map { "${it.first}\n　${it.second}\n　${it.third}" })
                }
            }
        }
    }
}
