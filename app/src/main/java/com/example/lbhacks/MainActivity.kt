package com.example.lbhacks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.lbhacks.ui.home.HomeScreen
import com.example.lbhacks.ui.theme.LBHacksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LBHacksTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    App()
                }
            }
        }
    }
}

sealed class Screen(val icon: Int, val route: String, @StringRes resourceId: Int) {
    object Home: Screen(R.drawable.ic_baseline_home_24, "Home", R.string.home_route)
    object Camera: Screen(R.drawable.ic_baseline_camera_alt_24, "Camera", R.string.camera_route)
}

@Composable
fun App() {
    val navController: NavHostController = rememberNavController()

    val navItems = listOf(
        Screen.Home,
        Screen.Camera
    )

    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

                navItems.forEach(action = { screen ->
                    BottomNavigationItem(
                        icon = { Icon(painter = painterResource(screen.icon), contentDescription = "Nav Icon:") },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        })
                })
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "Home") {
            composable("Home") { HomeScreen(navController)}
            composable("Camera") { HomeScreen(navController)}
        }
    }
}