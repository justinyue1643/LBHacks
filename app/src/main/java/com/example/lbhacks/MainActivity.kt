package com.example.lbhacks

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.lbhacks.ui.camera.CameraScreen
import com.example.lbhacks.ui.home.HomeScreen
import com.example.lbhacks.ui.theme.LBHacksTheme
import java.io.File

class MainActivity : ComponentActivity() {
    private lateinit var outputDirectory: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        outputDirectory = getOutputDirectory()
        if (allPermissionsGranted()) {
            setContent {
                LBHacksTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        AppThatNests(outputDirectory)
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setContent {
                    LBHacksTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(color = MaterialTheme.colors.background) {
                            AppThatNests(outputDirectory)
                        }
                    }
                }
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    }
}

sealed class Screen(val icon: Int, val route: String, @StringRes resourceId: Int) {
    object Home: Screen(R.drawable.ic_baseline_home_24, "Home", R.string.home_route)
    object Camera: Screen(R.drawable.ic_baseline_camera_alt_24, "Camera", R.string.camera_route)
}

@Composable
fun AppThatNests(outputDirectory: File) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "nested") {
        navigation(startDestination = "home screens", route = "nested") {
            composable("home screens") { App(outputDirectory, navController)}
        }
        composable("solution success camera") { CameraScreen(1.0f, navHostController = navController, outputDirectory = outputDirectory,true, addingProblem = false)}
        composable("solution failure camera") { CameraScreen(1.0f, navHostController = navController, outputDirectory = outputDirectory,false, addingProblem = false)}
    }
}

@Composable
fun App(outputDirectory: File, navHostController: NavHostController) {
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
            composable("Home") { HomeScreen(navController, navHostController)}
            composable("Camera") { CameraScreen(0.9f, navController, outputDirectory, true, addingProblem = true)}
        }
    }
}