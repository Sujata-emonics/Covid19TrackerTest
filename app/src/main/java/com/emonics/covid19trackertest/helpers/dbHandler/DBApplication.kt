package com.emonics.covid19trackertest.helpers.dbHandler

import android.app.Application
import com.emonics.covid19trackertest.Room.CovidTrackerDatabase
import com.emonics.covid19trackertest.helpers.retrofit.RetroInstance
import com.emonics.covid19trackertest.helpers.retrofit.RetroServiceDbUpdateInterface
import com.emonics.covid19trackertest.repository.CovidTrackerRepository
import com.emonics.covid19trackertest.repository.DetailViewRepository

class DBApplication:Application() {
    lateinit var covidTrackerRepository: CovidTrackerRepository
    lateinit var detailViewRepository: DetailViewRepository
    override fun onCreate() {
        super.onCreate()
        var retroService =  RetroInstance.getRetroInstance().create(RetroServiceDbUpdateInterface::class.java)
        var covidTrackerDatabase = CovidTrackerDatabase.getDatabase(applicationContext)
        covidTrackerRepository = CovidTrackerRepository(retroService,covidTrackerDatabase,applicationContext)
        detailViewRepository = DetailViewRepository(retroService,covidTrackerDatabase,applicationContext)

    }

}