package com.emonics.covid19trackertest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.databinding.ActivityCityResultBinding
import com.emonics.covid19trackertest.helpers.adapter.CityRecyclerAdapter
import com.emonics.covid19trackertest.helpers.dbHandler.DBApplication
import com.emonics.covid19trackertest.viewModel.CityRecyclerViewModel
import com.emonics.covid19trackertest.viewModel.CityRecyclerViewModelFactory
import com.emonics.covid19trackertest.viewModel.ForgotPassWordViewModel
import com.emonics.covid19trackertest.viewModel.ForgotPasswordViewModelFactory
import kotlinx.coroutines.launch

class CityResultActivity : AppCompatActivity() {
    lateinit var cityRecyclerViewModel: CityRecyclerViewModel
    lateinit var binding:ActivityCityResultBinding
    val adapter = CityRecyclerAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //var intent = Intent()
        var countryName = intent.getStringExtra("Country")
        Log.i("tag","countryname selected" +countryName)
        binding = ActivityCityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.adapter = adapter
        val repository = (application as DBApplication).covidTrackerRepository
        cityRecyclerViewModel = ViewModelProvider(this, CityRecyclerViewModelFactory(repository,countryName.toString())).get(CityRecyclerViewModel::class.java)
        cityRecyclerViewModel.citiRecordsLiveData.observe(this, Observer {
            Log.d("tag_", "onCreate: $it")
            adapter.setCityList(it)
        })

        binding.tvGlobalRecord.setOnClickListener {
            //
        }
        cityRecyclerViewModel.globalRecord.observe(this, Observer {
            binding.tvGlobalActiveCases.text = "Active Cases: "+it.global_active_cases.toString()
            binding.tvGlobalRecoveredCases.text = "Recovered Cases: "+it.global_recovered_cases.toString()
            binding.tvGlobalDeathCases.text = "Death Cases: "+it.global_death_cases.toString()
        })

        cityRecyclerViewModel.countryList.observe(this, Observer {
            Log.i("tag_","inside city result "+it)

                binding.tvCountryName.text = it.country_name.toString()
                binding.tvCountryName.setOnClickListener {
                    //
                }
                binding.tvCountryActiveCases.text = "Active Cases: "+it.active_cases.toString()
                binding.tvCountryRecoveredCases.text = "Recovered Cases: "+it.recovered_cases.toString()
                binding.tvCountryDeathCases.text = "Death Cases: "+it.death_cases.toString()

        })

        cityRecyclerViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
        })
       cityRecyclerViewModel.errorMessageCountry.observe(this, Observer {
           Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
       })
       cityRecyclerViewModel.globalErrorMessage.observe(this, Observer {
           Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
       })

    }
}