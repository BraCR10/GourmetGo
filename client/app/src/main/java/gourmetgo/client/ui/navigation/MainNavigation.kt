package gourmetgo.client.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import gourmetgo.client.ui.screens.LoginScreen
import gourmetgo.client.ui.screens.ExperiencesScreen
import gourmetgo.client.ui.screens.ProfileScreen

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("experiences") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("experiences") {
            ExperiencesScreen(
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}