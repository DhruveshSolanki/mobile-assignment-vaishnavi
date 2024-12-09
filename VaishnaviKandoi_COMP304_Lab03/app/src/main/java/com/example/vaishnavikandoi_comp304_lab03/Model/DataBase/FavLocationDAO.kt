package com.example.vaishnavikandoi_comp304_lab03.Model.DataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vaishnavikandoi_comp304_lab03.Model.FavLocation

@Dao
interface FavLocationDAO {

    @Query("SELECT * FROM FavLocation")
    fun getAllLocations() : LiveData<List<FavLocation>>

    @Insert
    fun addLocation(favLocation: FavLocation)

    @Query("DELETE FROM FavLocation WHERE ID = :id")
    fun deleteLocation(id : Int)

    @Query("DELETE FROM favlocation")
    suspend fun deleteAllLocations()
}