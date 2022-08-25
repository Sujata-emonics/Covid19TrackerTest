package com.emonics.covid19trackertest.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.Room.CovidTrackerDatabase
import com.emonics.covid19trackertest.helpers.dbHandler.DBApplication
import com.emonics.covid19trackertest.helpers.retrofit.RetroInstance
import com.emonics.covid19trackertest.helpers.retrofit.RetroServiceInterFace
import com.emonics.covid19trackertest.viewModel.DatabaseViewModel
import com.emonics.covid19trackertest.viewModel.DatabaseViewModelFactory
import com.emonics.covid19trackertest.viewModel.UserLogInViewModel

class UpdateDBActivity : AppCompatActivity() {
   private lateinit var databaseViewModel : DatabaseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_dbactivity)

        val covidTrackerDatabase: CovidTrackerDatabase

        val repository = (application as DBApplication).covidTrackerRepository
        databaseViewModel = ViewModelProvider(this, DatabaseViewModelFactory(repository)).get(DatabaseViewModel::class.java)
        databaseViewModel.countries.observe(this, Observer {
            //Log.i("tag_db","on  create"+it.toString())
            it.forEach{
                Log.i("tag_db","-country-"+it.name)
                //covidTrackerDatabase.Inse
            }
        }
        )

        databaseViewModel.cities.observe(this, Observer {
           // Log.i("tag_db", "-cities-"+it.toString())
            it.forEach{
                Log.i("tag_db", "-city-"+it.name)
            }
        })

        databaseViewModel.globalData.observe(this, Observer {
           // Log.i("tag_db", "-cities-"+it.toString())

                Log.i("tag_db", "-global-"+it.global_confirmed_cases)

        })

        databaseViewModel.userData.observe(this, Observer {
            it.forEach{
                Log.i("tag_db","user "+it.name)

            }
        })
    }
}