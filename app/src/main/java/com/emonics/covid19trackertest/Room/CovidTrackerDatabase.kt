package com.emonics.covid19trackertest.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.Global
import com.emonics.covid19trackertest.dataClass.User

@Database(entities = [Global::class, Country::class, City::class, User::class],version=1)
abstract class CovidTrackerDatabase: RoomDatabase() {
    abstract fun GlobalDao():GlobalDao
    abstract fun CountryDao():CountryDao
    abstract fun CityDao():CityDao
    abstract fun UserDao():UserDao
    companion object{
         @Volatile
        private var INSTANCE: CovidTrackerDatabase? = null
            fun getDatabase(context:Context): CovidTrackerDatabase{
                val tempInstance = INSTANCE
                if(tempInstance!=null){
                    return tempInstance
                }
                kotlin.synchronized(this){

                    val instance = Room.databaseBuilder(context.applicationContext,CovidTrackerDatabase::class.java,
                    "db_covid_tracker").build()
                    INSTANCE = instance
                    return  instance

                }
            }
    }
}