package com.example.vaishnavikandoi_comp304_lab03.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vaishnavikandoi_comp304_lab03.ViewModel.FavLocationViewModel
import com.example.vaishnavikandoi_comp304_lab03.ViewModel.WeatherViewModel
import com.example.vaishnavikandoi_comp304_lab03.ui.theme.poppinsFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPage(viewModel: WeatherViewModel, navController: NavController, favLocationViewModel: FavLocationViewModel) {
    // Mutable String to hold city value
    var city by remember { mutableStateOf("") }

    val weatherResult = viewModel.weatherResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column() {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(120.dp),
                title = {
                    Text(
                        "Weather App",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                },
            )
        }

        Row(
            modifier = Modifier
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = city,
                // city value will change everytime the value in the search bar is changed
                onValueChange = { newCity -> city = newCity },
                label = { Text(text = "Search for any location") },
                textStyle = TextStyle(
                    fontSize = 20.sp // Set your desired font size here
                )
            )

            IconButton(onClick = {
                viewModel.getData(city)
                navController.navigate("weatherDetails")
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "SearchIcon"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 25.dp, horizontal = 5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically // Align the icon and text
        ) {
            Icon(
                imageVector = Icons.Default.Star, // Star icon to represent favorite
                contentDescription = "Favorite Icon",
                tint = Color(0xFFFFD700), // Gold color for the star to make it stand out
                modifier = Modifier.size(24.dp) // Adjust the size of the icon
            )

            Spacer(modifier = Modifier.width(8.dp)) // Add some space between icon and text

            Text(
                text = "Favorite Locations",
                fontFamily = poppinsFontFamily,
                fontSize = 20.sp, // Consistent size with the search text
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(favLocationViewModel.listOfFavLocation) { location ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clickable {
                            viewModel.getData(location.City)
                            navController.navigate("weatherDetails")
                        }
                        .padding(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFB0C4DE) // Light blue background for each card
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .background(Color(0xFFB0C4DE)), // Matching the background color
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Display task name and location
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 5.dp)
                                    .weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "locationIcon",
                                    tint = Color.Red,
                                    modifier = Modifier.size(40.dp)
                                )
                                Text(
                                    text = "${location.City}, ${location.Country}",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Normal, // Text should not be bold in favorites
                                    style = TextStyle(
                                        fontSize = 18.sp // Adjusted size for consistency with the list items
                                    ),
                                    color = Color(0xFF000000) // Keep the color consistent with the other text
                                )
                            }

                            Text(
                                text = "${location.temp}°C",
                                fontFamily = poppinsFontFamily,
                                style = TextStyle(
                                    fontSize = 18.sp // Matching the font size with the location text
                                ),
                                color = Color(0xFF000000) // Matching the text color with the other text
                            )
                        }
                    }
                }
            }
        }
    }
}

