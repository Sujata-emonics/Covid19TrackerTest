package com.emonics.covid19trackertest.Room

import androidx.room.Dao
import androidx.room.Query
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.CountryCity

@Dao
interface CountryCityDao {
    @Query("SELECT * FROM country_city_table")
    fun getAllCountryCity():List<CountryCity>
    @Query("SELECT city FROM country_city_table WHERE country LIKE :countryName")
    suspend fun findByCountry(countryName: String): List<String?>
    @Query("SELECT * FROM country_city_table")
    suspend fun findAllCountry(): List<CountryCity?>
}