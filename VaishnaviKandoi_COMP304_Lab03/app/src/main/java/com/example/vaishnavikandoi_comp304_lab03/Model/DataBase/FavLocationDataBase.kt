package com.example.vaishnavikandoi_comp304_lab03.Model.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vaishnavikandoi_comp304_lab03.Model.FavLocation

@Database(entities = [FavLocation::class], version = 2)
abstract class FavLocationDataBase : RoomDatabase() {


    companion object{
        const val DBNAME = "FavLocations_DB"
    }

    //returns the dao
    abstract  fun getFavLocationDAO() : FavLocationDAO
}