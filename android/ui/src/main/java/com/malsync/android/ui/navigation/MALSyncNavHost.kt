package com.malsync.android.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.malsync.android.ui.theme.glassBackground
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.malsync.android.ui.screens.discover.DiscoverScreen
import com.malsync.android.ui.screens.library.LibraryScreen
import com.malsync.android.ui.screens.detail.AnimeDetailScreen
import com.malsync.android.ui.screens.browser.BrowserScreen
import com.malsync.android.ui.screens.search.SearchScreen
import com.malsync.android.ui.screens.profile.ProfileScreen
import com.malsync.android.ui.screens.settings.SettingsScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Discover : Screen("discover", "Discover", Icons.Filled.Explore) // Was Home
    object Library : Screen("library", "Library", Icons.Filled.VideoLibrary)
    object Browser : Screen("browser", "Browser", Icons.Filled.Language)
    object Search : Screen("search", "Search", Icons.Filled.Search)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
    object Detail : Screen("detail/{animeId}", "Detail", Icons.Filled.Info) {
        fun createRoute(animeId: String) = "detail/$animeId"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MALSyncNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        Screen.Library,
        Screen.Discover,
        Screen.Browser,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            // Hide bottom bar on detail screen or settings if desired, keeping simple for now
            val currentRoute = currentDestination?.route
            if (currentRoute != Screen.Settings.route && currentRoute?.startsWith("detail") != true) {
                NavigationBar(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent, // Let modifier handle background
                    modifier = Modifier.padding(16.dp).glassBackground(
                         shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                    ) // Floating glass bar effect
                ) {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Library.route, // Default to Library often, or Discover? Let's stick to Library as per user probable pref or Discover. Prompt didn't specify default, but Library is usually main.
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Discover.route) {
                DiscoverScreen(
                    onNavigateToDetail = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    }
                )
            }

            composable(Screen.Library.route) {
                LibraryScreen(
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onNavigateToDetail = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    }
                )
            }
            
            composable(Screen.Browser.route) {
                BrowserScreen()
            }
            
            composable(Screen.Search.route) {
                SearchScreen()
            }
            
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("animeId") { type = NavType.StringType })
            ) {
                AnimeDetailScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
