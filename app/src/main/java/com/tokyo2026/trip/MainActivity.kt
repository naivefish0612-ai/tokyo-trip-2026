package com.tokyo2026.trip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tokyo2026.trip.data.Trip
import com.tokyo2026.trip.store.TripStore
import com.tokyo2026.trip.ui.*

private data class Tab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val tabs = listOf(
    Tab("home", "行程", Icons.Default.Today),
    Tab("spots", "景點", Icons.Default.Explore),
    Tab("checklist", "準備", Icons.Default.CheckBox)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val store = remember { TripStore(applicationContext) }
            Tokyo2026Theme {
                val nav = rememberNavController()
                val entry by nav.currentBackStackEntryAsState()
                val route = entry?.destination?.route
                val showBar = route in tabs.map { it.route }

                Scaffold(
                    bottomBar = {
                        if (showBar) NavigationBar {
                            tabs.forEach { t ->
                                NavigationBarItem(
                                    selected = route == t.route,
                                    onClick = {
                                        if (route != t.route) nav.navigate(t.route) {
                                            popUpTo("home"); launchSingleTop = true
                                        }
                                    },
                                    icon = { Icon(t.icon, contentDescription = t.label) },
                                    label = { Text(t.label) }
                                )
                            }
                        }
                    }
                ) { pad ->
                    NavHost(nav, startDestination = "home", modifier = Modifier.padding(pad)) {
                        composable("home") {
                            HomeScreen(store,
                                onDay = { nav.navigate("day/$it") },
                                onSpot = { nav.navigate("spot/$it") },
                                onChecklist = { nav.navigate("checklist") { popUpTo("home"); launchSingleTop = true } })
                        }
                        composable("spots") { SpotsScreen(store) { id -> nav.navigate("spot/$id") } }
                        composable("checklist") { ChecklistScreen(store, onBack = { nav.popBackStack() }) }
                        composable("day/{n}") { e ->
                            val n = e.arguments?.getString("n")?.toIntOrNull() ?: 1
                            val day = Trip.days.firstOrNull { it.n == n } ?: Trip.days.first()
                            DayScreen(day, store, onBack = { nav.popBackStack() }, onSpot = { nav.navigate("spot/$it") })
                        }
                        composable("spot/{id}") { e ->
                            val spot = Trip.allSpots.firstOrNull { it.id == e.arguments?.getString("id") }
                            if (spot == null) nav.popBackStack() else SpotScreen(spot, store, onBack = { nav.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
