package com.example.vaishnavikandoi_comp304_lab03.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vaishnavikandoi_comp304_lab03.Model.NetworkResponse
import com.example.vaishnavikandoi_comp304_lab03.Model.WeatherModel
import com.example.vaishnavikandoi_comp304_lab03.ViewModel.FavLocationViewModel
import com.example.vaishnavikandoi_comp304_lab03.ViewModel.WeatherViewModel
import com.example.vaishnavikandoi_comp304_lab03.ui.theme.poppinsFontFamily

@Composable
fun WeatherScaffold(weatherViewModel: WeatherViewModel, navController: NavController, favLocationViewModel: FavLocationViewModel) {
    Scaffold(
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController, favLocationViewModel, weatherViewModel) },
        content = { paddingValues ->
            DisplayWeatherInfo(
                modifier = Modifier.padding(paddingValues),
                viewModel = weatherViewModel
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column() {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(100.dp), // Adjusted the height to avoid overlap
                title = {
                    Text(
                        "Weather Information",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun BottomNavBar(
    navController: NavController,
    favLocationViewModel: FavLocationViewModel,
    weatherViewModel: WeatherViewModel
) {
    val weatherResult = weatherViewModel.weatherResult.observeAsState()

    Column() {
        NavigationBar() {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                label = { Text("Home", fontFamily = poppinsFontFamily) },
                selected = false,
                onClick = { navController.navigate("home") }
            )
            NavigationBarItem(
                icon = {
                    when (val result = weatherResult.value) {
                        is NetworkResponse.Success -> {
                            val city = result.data.location.name
                            val isCityInList = favLocationViewModel.listOfFavLocation.any { it.City == city }
                            if (isCityInList) {
                                Icon(Icons.Filled.Favorite, contentDescription = "Add to Fav")
                            } else {
                                Icon(Icons.Filled.FavoriteBorder, contentDescription = "Add to Fav")
                            }
                        }
                        else -> {}
                    }
                },
                label = { Text("Favourite", fontFamily = poppinsFontFamily) },
                selected = false,
                onClick = {
                    weatherResult.value?.let { result ->
                        if (result is NetworkResponse.Success) {
                            val city = result.data.location.name
                            val country = result.data.location.country
                            val temp = result.data.current.temp_c
                            val isCityInList = favLocationViewModel.listOfFavLocation.any { it.City == city }
                            if (isCityInList) {
                                favLocationViewModel.listOfFavLocation.find { it.City == city }?.let {
                                    favLocationViewModel.deleteFavLocation(it.ID)
                                }
                            } else {
                                favLocationViewModel.addFavLocation(city, country, temp)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DisplayWeatherInfo(modifier: Modifier, viewModel: WeatherViewModel) {
    val weatherResult = viewModel.weatherResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }
            null -> {}
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(BorderStroke(2.dp, Color.Gray), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = " ${data.location.name}, ${data.location.country}",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black), // Changed to black color
                modifier = Modifier.padding(bottom = 16.dp)
            )

            AsyncImage(
                model = "https:${data.current.condition.icon}",
                contentDescription = data.current.condition.text,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = data.current.condition.text,
                fontSize = 20.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Display temperature
            WeatherKeyVal(key = "Temperature (°C)", value = "${data.current.temp_c}°C")

            // Display humidity
            WeatherKeyVal(key = "Humidity (%)", value = "${data.current.humidity}%")

            // Display wind speed
            WeatherKeyVal(key = "Wind Speed (km/h)", value = "${data.current.wind_kph} km/h")

            // Additional weather information
            WeatherKeyVal(key = "Feels Like (°C)", value = "${data.current.feelslike_c}°C")
            WeatherKeyVal(key = "Precipitation (mm)", value = "${data.current.precip_mm} mm")
        }
    }
}

@Composable
fun WeatherKeyVal(key: String, value: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontSize = 20.sp, color = Color.Gray)
    }
}
