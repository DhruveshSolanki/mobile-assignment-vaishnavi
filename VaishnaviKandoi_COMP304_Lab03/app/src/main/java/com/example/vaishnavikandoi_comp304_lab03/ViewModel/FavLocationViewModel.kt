package com.example.vaishnavikandoi_comp304_lab03.ViewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaishnavikandoi_comp304_lab03.Model.FavLocation
import com.example.vaishnavikandoi_comp304_lab03.View.FavLocationApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavLocationViewModel : ViewModel()
{
    //Create an instance of the DAO
    val favLocationDao = FavLocationApplication.favLocationDB.getFavLocationDAO()


    val FavLocationList : LiveData<List<FavLocation>> = favLocationDao.getAllLocations()

    val listOfFavLocation: SnapshotStateList<FavLocation> = mutableStateListOf()

    var id = 0;

    fun addFavLocation(city : String, country : String, temp: String)
    {
        //will run this task on different thread then main so it wont crash
        viewModelScope.launch(Dispatchers.IO) {
            //add to DB
            favLocationDao.addLocation(FavLocation(id, city, country, true, temp))
            //add to list
            listOfFavLocation.add(FavLocation(id, city, country, true, temp))
            id++
        }
    }

    fun deleteFavLocation(id: Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            //remove form DB
            favLocationDao.deleteLocation(id)
            //remove from list
            val locationToRemove = listOfFavLocation.find { it.ID == id }
            listOfFavLocation.remove(locationToRemove)
        }

    }
}