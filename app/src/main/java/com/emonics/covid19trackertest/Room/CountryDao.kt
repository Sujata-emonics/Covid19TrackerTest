package com.emonics.covid19trackertest.Room

import androidx.room.*
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
@Dao
interface CountryDao {
    @Query("SELECT * FROM country_table")
    fun getAllCountry():List<Country>
    @Query("SELECT * FROM country_table WHERE PK_CountryID LIKE :countryId LIMIT 1")
    suspend fun findByCountryID(countryId: Int): Country
    @Query("SELECT * FROM country_table WHERE country_name LIKE :countryName")
    suspend fun findByCountryName(countryName: String): Country
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: List<Country>?)
    @Delete
    suspend fun delete(country: Country)
    @Query("DELETE FROM country_table")
    suspend fun deleteAllCountries()
    @Query("DELETE FROM country_table WHERE PK_CountryID LIKE :countryId")
    suspend fun deleteByRoll(countryId:Int)
}