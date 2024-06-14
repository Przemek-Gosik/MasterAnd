package com.example.masterand.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.masterand.navigation.screens.GameScreen
import com.example.masterand.navigation.screens.ProfileScreen
import com.example.masterand.navigation.screens.ResultScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.ProfileScreen.route) {
        composable(
            route = Screen.ProfileScreen.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300))
            }
        ) {
            ProfileScreen(navController = navController)
        }

        composable(
            route = Screen.GameScreen.route,
            arguments = listOf(navArgument("colorCount") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { navBackStackEntry ->
            val colorCount = navBackStackEntry.arguments?.getString("colorCount") ?: ""
            GameScreen(navController = navController, colorCount = colorCount, onLogoutClick = {
                navController.popBackStack()
            })
        }

        composable(
            route = Screen.ResultScreen.route,
            arguments = listOf(navArgument("score") { type = NavType.StringType }),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
                ) + fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { navBackStackEntry ->
            val score = navBackStackEntry.arguments?.getString("score") ?: ""
            ResultScreen(
                score = score,
                onRestartGame = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.popBackStack(
                        route = Screen.ProfileScreen.route,
                        inclusive = false
                    )
                })
        }
    }
}