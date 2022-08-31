package com.emonics.covid19trackertest.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "country_city_table", indices = [Index(value=["country","city"],unique=true)])
data class CountryCity(
    @PrimaryKey(autoGenerate = true) var PK_ID:Int,
    @ColumnInfo(name="country") var country:Int?,
    @ColumnInfo(name="city") var city:String?
)
