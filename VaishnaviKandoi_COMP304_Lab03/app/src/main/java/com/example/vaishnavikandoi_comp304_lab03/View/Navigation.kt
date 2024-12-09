package com.example.vaishnavikandoi_comp304_lab03.View

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vaishnavikandoi_comp304_lab03.ViewModel.FavLocationViewModel
import com.example.vaishnavikandoi_comp304_lab03.ViewModel.WeatherViewModel

@Composable
fun Navigation()
{
    val navController = rememberNavController()
    val weatherViewModel: WeatherViewModel = viewModel()
    val favLocationViewModel: FavLocationViewModel = viewModel()

    //This controls the navigation and where each path will lead to
    NavHost(navController = navController, startDestination = "home")
    {
        //goes to home screen to search
        composable("home"){WeatherPage(weatherViewModel, navController, favLocationViewModel)}
        //goes to another activity to show weather info
        composable("weatherDetails"){ WeatherScaffold(weatherViewModel, navController, favLocationViewModel) }
    }
}