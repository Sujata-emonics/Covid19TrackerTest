package com.emonics.covid19trackertest.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName="city_table", indices = [Index(value=["city_name","country_name"],unique=true)])
data class City(
    @PrimaryKey(autoGenerate = true) var PK_CityID:Int,
    @ColumnInfo(name="city_name") var name:String?,
    @ColumnInfo(name="FK_country") var FK_country:Int?,
    @ColumnInfo(name="country_name") var country:String?,
    @ColumnInfo(name="confirmed_cases") var confirmed_cases:String?,
    @ColumnInfo(name="active_cases") var active_cases:String?,
    @ColumnInfo(name="recovered_cases") var recovered_cases:String?,
    @ColumnInfo(name="death_cases") var death_cases:String?,
    @ColumnInfo(name="test_cases") var test_cases:String?
)
