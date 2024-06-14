package com.example.masterand.navigation

sealed class Screen(val route: String) {
    object ProfileScreen: Screen(route = "ProfileScreen")
    object GameScreen: Screen(route = "GameScreen/{colorCount}")
    object ResultScreen: Screen(route = "ResultScreen/{score}")
}