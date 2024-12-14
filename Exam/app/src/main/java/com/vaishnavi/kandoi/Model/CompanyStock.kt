package com.vaishnavi.kandoi.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_stock")
data class CompanyStock(
    @PrimaryKey val companyName: String,
    val openingPrice: Double,
    val closingPrice: Double
)
