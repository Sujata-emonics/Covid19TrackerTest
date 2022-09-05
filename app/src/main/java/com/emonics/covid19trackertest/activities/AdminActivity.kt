package com.emonics.covid19trackertest.activities

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection.setSelection
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.databinding.ActivityAdminBinding
import com.emonics.covid19trackertest.helpers.dbHandler.DBApplication
import com.emonics.covid19trackertest.util.Covid19TrackerUtility
import com.emonics.covid19trackertest.viewModel.AdminViewModel
import com.emonics.covid19trackertest.viewModel.AdminViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity() {
    //lateinit var viewBinding:ActivityAdminBinding
    lateinit var dataBinding:ActivityAdminBinding
    lateinit var countryList1:ArrayList<String>
    lateinit var cityList:ArrayList<String>
    lateinit var adminVieModel:AdminViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_admin)
        var repository = (application as DBApplication).covidTrackerRepository
        adminVieModel = ViewModelProvider(this,AdminViewModelFactory(repository)).get(AdminViewModel::class.java)
        dataBinding.myViewModel =adminVieModel
       // dataBinding.lifecycleOwner = this
       // var cityList = adminVieModel.getCityForselectedCountry("India")

        //Log.i("tag_","city5 "+cityList)
        adminVieModel.countryLiveData.observe(this, Observer {
            Log.i("tag_","observe "+it)
            //countryList1 = it
            var arrayAdapter_country = ArrayAdapter(applicationContext,R.layout.spinner_country,it)
            dataBinding.spCountry.adapter=arrayAdapter_country
            dataBinding.spCountry.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    Log.i("tag_","country seslected "+it[p2].toString())
                    dataBinding.spCountrySelectedItem.text = it[p2].toString()
                    Log.i("tag_"," count "+it[p2].toString())
                    adminVieModel.getCityForselectedCountry(it[p2].toString())
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        })
      adminVieModel.cityLiveData.observe(this, Observer {
            var arrayAdapter_city = ArrayAdapter(applicationContext,R.layout.spinner_city,it)
            dataBinding.spCity.adapter=arrayAdapter_city
            //dataBinding.spCitySelectedItem.text=it[p2].toString()


            dataBinding.spCity.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    Log.i("tag_","country seslected "+it[p2].toString())
                    dataBinding.spCitySelectedItem.text = it[p2].toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }


        })

        dataBinding.btnSaveOrUpdate.setOnClickListener {
            Log.i("tag_","inside onclick")
            adminVieModel.saveOrUpdate()
        }

        dataBinding.btnBack.setOnClickListener {
            Log.i("tag_","inside onclick")
            //adminVieModel.saveOrUpdate()
            finish()
        }


        adminVieModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
        dataBinding.tvSignOut.setOnClickListener {
            signOut()
        }
       /*dataBinding.tvViewDetail.setOnClickListener {
           viewGlobalDetail()
        }*/
      dataBinding.tvGobal.setOnClickListener {
          viewGlobalDetail()
        }
      dataBinding.tvUpdateDb.setOnClickListener {
           updateDB()
        }
    }

    fun signOut(){
        Log.i("tag_","signOut")
        var mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()

          val intent= Intent(this, MainActivity::class.java)
          startActivity(intent)
          finish()
    }

    fun viewDetail(){
        val intent = Intent(this, DetailActivity::class.java)
        //var CountryName= dataBinding.spCountrySelectedItem.text
        //Log.i("tag_","country in admin"+CountryName)
       // intent.putExtra("Country",CountryName.toString())
        intent.putExtra("RECORDTYPE",dataBinding.spCountrySelectedItem.text)

        startActivity(intent)

    }

    fun viewGlobalDetail(){
        val intent = Intent(this, DetailActivity::class.java)
        //var CountryName= dataBinding.spCountrySelectedItem.text
        //Log.i("tag_","country in admin"+CountryName)
        intent.putExtra("RECORDTYPE","")
        startActivity(intent)

    }

   fun updateDB(){
       val intent = Intent(this, UpdateDBActivity::class.java)
        startActivity(intent)

    }


}