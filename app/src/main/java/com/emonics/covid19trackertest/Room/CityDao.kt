package com.emonics.covid19trackertest.Room

import androidx.room.*
import com.emonics.covid19trackertest.dataClass.City
@Dao
interface CityDao {
    @Query("SELECT * FROM city_table")
    fun getAllCities():List<City>
    @Query("SELECT * FROM city_table WHERE PK_CityID LIKE :cityId LIMIT 1")
    suspend fun findByCityId(cityId: Int):City
    @Query("SELECT * FROM city_table WHERE country_name LIKE :countryName")
    suspend fun findByCountryName(countryName: String):List<City>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city:List<City>?)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertsingleRecord(city: City) : Long
    @Delete
    suspend fun delete(city: City)
    @Query("DELETE FROM city_table")
    suspend fun deleteAllCities()
    @Query("DELETE FROM city_table WHERE PK_CityID LIKE :cityId")
    suspend fun deleteByCityId(cityId:Int)
}