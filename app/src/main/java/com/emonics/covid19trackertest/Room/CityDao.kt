package com.emonics.covid19trackertest.Room

import androidx.room.*
import com.emonics.covid19trackertest.dataClass.City
@Dao
interface CityDao {
    @Query("SELECT * FROM city_table")
    fun getAllCities():List<City>
    @Query("SELECT * FROM city_table WHERE PK_CityID LIKE :cityId LIMIT 1")
    suspend fun findByCityId(cityId: Int):City
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city:List<City>?)
    @Delete
    suspend fun delete(city: City)
    @Query("DELETE FROM city_table")
    suspend fun deleteAllCities()
    @Query("DELETE FROM city_table WHERE PK_CityID LIKE :cityId")
    suspend fun deleteByCityId(cityId:Int)
}