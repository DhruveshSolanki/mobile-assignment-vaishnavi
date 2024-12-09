package com.example.vaishnavikandoi_comp304_lab03.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavLocation (
    //ID is primary key
    @PrimaryKey
    var ID: Int,
    var City: String,
    var Country: String,
    var isFav: Boolean,
    var temp: String
)