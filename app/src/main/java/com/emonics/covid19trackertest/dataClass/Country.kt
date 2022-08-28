package com.emonics.covid19trackertest.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(tableName="country_table", indices = [Index(value = ["country_name"], unique = true)])
data class Country(
    @PrimaryKey(autoGenerate = true) var PK_CountryID:Int,
    @ColumnInfo(name="country_name") var  country_name:String?,
    @ColumnInfo(name="confirmed_cases") var confirmed_cases:Int?,
    @ColumnInfo(name="active_cases") var active_cases:Int?,
    @ColumnInfo(name="recovered_cases") var recovered_cases:Int?,
    @ColumnInfo(name="death_cases") var death_cases:Int?,
    @ColumnInfo(name="test_cases") var test_cases:Int?
)
