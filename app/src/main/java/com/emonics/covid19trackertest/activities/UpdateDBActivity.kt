package com.emonics.covid19trackertest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.Room.CovidTrackerDatabase
import com.emonics.covid19trackertest.databinding.ActivityDetailBinding
import com.emonics.covid19trackertest.databinding.ActivityUpdateDbactivityBinding
import com.emonics.covid19trackertest.helpers.dbHandler.DBApplication
import com.emonics.covid19trackertest.helpers.retrofit.RetroInstance
import com.emonics.covid19trackertest.helpers.retrofit.RetroServiceInterFace
import com.emonics.covid19trackertest.viewModel.DatabaseViewModel
import com.emonics.covid19trackertest.viewModel.DatabaseViewModelFactory
import com.emonics.covid19trackertest.viewModel.UserLogInViewModel
import com.google.firebase.auth.FirebaseAuth

class UpdateDBActivity : AppCompatActivity() {
   private lateinit var databaseViewModel : DatabaseViewModel
    lateinit var viewBinding: ActivityUpdateDbactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding  = ActivityUpdateDbactivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        //setContentView(R.layout.activity_update_dbactivity)

        val covidTrackerDatabase: CovidTrackerDatabase

        val repository = (application as DBApplication).covidTrackerRepository
        databaseViewModel = ViewModelProvider(this, DatabaseViewModelFactory(repository)).get(DatabaseViewModel::class.java)
        databaseViewModel.countries.observe(this, Observer {
            //Log.i("tag_db","on  create"+it.toString())
            it.forEach{
                Log.i("tag_db","-country-"+it.country_name)
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

        viewBinding.tvLogOut.setOnClickListener {
            signOut()
        }
       viewBinding.tvPrevious.setOnClickListener {
            finish()
        }
      viewBinding.tvViewGlobalRecord.setOnClickListener {
          viewGlobalDetail()
      }
    }
    fun viewGlobalDetail(){
        val intent = Intent(this, DetailActivity::class.java)
        //var CountryName= dataBinding.spCountrySelectedItem.text
        //Log.i("tag_","country in admin"+CountryName)
        intent.putExtra("RECORDTYPE","")
        startActivity(intent)

    }

    fun signOut(){
        Log.i("tag_","signOut")
        var mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()

        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}