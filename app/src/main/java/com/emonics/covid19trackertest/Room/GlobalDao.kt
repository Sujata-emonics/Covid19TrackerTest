package com.emonics.covid19trackertest.Room

import androidx.room.*
import com.emonics.covid19trackertest.dataClass.Global
import retrofit2.http.DELETE
@Dao
interface GlobalDao {
    @Query("SELECT * FROM global_table")
    fun getAllGlobalData():Global
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGlobalData(global:Global)
    @Query("DELETE FROM global_table")
    suspend fun deleteGlobalRecord()
    @Update
    suspend fun UpdateGlobalRecord(record:Global)
}