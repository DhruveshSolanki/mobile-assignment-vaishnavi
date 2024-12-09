package com.example.vaishnavikandoi_comp304_lab03.View

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vaishnavikandoi_comp304_lab03.Model.DataBase.FavLocationDAO
import com.example.vaishnavikandoi_comp304_lab03.Model.DataBase.FavLocationDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//This class will be called everytime the application is called
class FavLocationApplication : Application()
{


    companion object{
        //Create a DB
        lateinit var favLocationDB: FavLocationDataBase
        lateinit var favLocationDao: FavLocationDAO
    }



    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Ensure the 'temp' column is added as NOT NULL with a default value
            database.execSQL("ALTER TABLE FavLocation ADD COLUMN temp TEXT NOT NULL DEFAULT ''")
        }
    }

    //Things we want to do when the application is created
    override fun onCreate() {
        //super will allow the application to still do things it would usually do
        super.onCreate()
        //Creating the DB
        favLocationDB = Room.databaseBuilder(
            applicationContext,
            FavLocationDataBase::class.java,
            FavLocationDataBase.DBNAME
        )
            .addMigrations(MIGRATION_1_2)
            .build()

        val favLocationDao = favLocationDB.getFavLocationDAO()

        //Delete all records from the database so we're not working will old data
        CoroutineScope(Dispatchers.IO).launch {
            favLocationDao.deleteAllLocations()
        }

    }
}