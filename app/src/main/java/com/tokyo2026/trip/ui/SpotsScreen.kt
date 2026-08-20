package com.tokyo2026.trip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tokyo2026.trip.data.Cat
import com.tokyo2026.trip.data.Trip
import com.tokyo2026.trip.store.TripStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotsScreen(store: TripStore, onSpot: (String) -> Unit) {
    val cs = MaterialTheme.colorScheme
    var query by rememberSaveable { mutableStateOf("") }
    var cat by rememberSaveable { mutableStateOf<String?>(null) }
    var favOnly by rememberSaveable { mutableStateOf(false) }

    val list = Trip.allSpots.filter { s ->
        (query.isBlank() || listOf(s.nameZh, s.nameJa, s.kana, s.area).any { it.contains(query, true) }) &&
            (cat == null || s.cat.name == cat) &&
            (!favOnly || store.isFav(s.id))
    }

    Column(Modifier.fillMaxSize().background(cs.background)) {
        OutlinedTextField(
            value = query, onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            placeholder = { Text("搜尋景點、日文名、地區") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = {
                if (query.isNotEmpty()) IconButton({ query = "" }) { Icon(Icons.Default.Clear, "清除") }
            },
            singleLine = true, shape = RoundedCornerShape(14.dp)
        )
        LazyRow(
            Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(selected = favOnly, onClick = { favOnly = !favOnly },
                    label = { Text("收藏") },
                    leadingIcon = { Icon(if (favOnly) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, Modifier.size(16.dp)) })
            }
            item {
                FilterChip(selected = cat == null, onClick = { cat = null }, label = { Text("全部") })
            }
            items(Cat.entries.toList()) { c ->
                FilterChip(selected = cat == c.name, onClick = { cat = if (cat == c.name) null else c.name }, label = { Text(c.label) })
            }
        }
        Text("${list.size} 個景點", Modifier.padding(start = 20.dp, top = 10.dp),
            style = MaterialTheme.typography.labelMedium, color = cs.onSurfaceVariant)

        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)) {
            items(list) { s ->
                val day = Trip.dayOf(s.id)
                Card(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp).clickable { onSpot(s.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.surface)
                ) {
                    Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        SpotHero(s, Modifier.size(74.dp).clip(RoundedCornerShape(12.dp)), scrim = false)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(s.nameZh, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text(s.nameJa, style = MaterialTheme.typography.bodySmall, color = cs.onSurfaceVariant, maxLines = 1)
                            Spacer(Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (day != null) Pill("D${day.n}", cs.primary.copy(alpha = 0.12f), cs.primary)
                                Pill(s.cat.label, cs.surfaceVariant, cs.onSurfaceVariant)
                                if (s.booking != null) Pill("需預約", cs.secondary.copy(alpha = 0.15f), cs.secondary)
                            }
                        }
                        IconButton({ store.toggleFav(s.id) }) {
                            Icon(
                                if (store.isFav(s.id)) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                "收藏", tint = if (store.isFav(s.id)) cs.secondary else cs.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
