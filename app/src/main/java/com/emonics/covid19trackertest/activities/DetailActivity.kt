package com.emonics.covid19trackertest.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.databinding.ActivityDetailBinding
import com.emonics.covid19trackertest.helpers.dbHandler.DBApplication
import com.emonics.covid19trackertest.repository.DetailViewRepository
import com.emonics.covid19trackertest.viewModel.DetailViewFactory
import com.emonics.covid19trackertest.viewModel.DetailViewModel
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_detail.*
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class DetailActivity : AppCompatActivity() {
    lateinit var mpiechart: PieChart
    lateinit var spinner: Spinner
    lateinit var viewBinding:ActivityDetailBinding
    lateinit var detailViewModel: DetailViewModel
    lateinit var repository: DetailViewRepository
    lateinit var countryCodePicker: CountryCodePicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        mpiechart = viewBinding.pieChart
        spinner = viewBinding.spinner
        countryCodePicker = viewBinding.ccp
        viewBinding.tvGlobalRecord.setOnClickListener {
            detailViewModel.getGlobalRecord()
        }
        //countryCodePicker.setDefaultCountryUsingNameCode("US")
        //var defaultCountryCode = countryCodePicker.defaultCountryName
        //Log.i("tag_","defaultCountryCode"+defaultCountryCode)
        countryCodePicker.setAutoDetectedCountry(true)
        var country = countryCodePicker.getSelectedCountryName()
        countryCodePicker.setOnCountryChangeListener(CountryCodePicker.OnCountryChangeListener {
            country = countryCodePicker.getSelectedCountryName()
            fetchdata(country)
        })
        repository = (application as DBApplication).detailViewRepository
        detailViewModel = ViewModelProvider(this, DetailViewFactory(repository,countryCodePicker.getSelectedCountryName().toString())).get(DetailViewModel::class.java)


        detailViewModel.globalRecord.observe(this, Observer {
            updateGraph( it.global_active_cases!!.toInt(),it.global_confirmed_cases!!.toInt(),it.global_recovered_cases!!.toInt(),it.global_death_cases!!.toInt())
            viewBinding.tvActiveCases.text = it.global_active_cases.toString()
            viewBinding.tvConfirmedCases.text = it.global_confirmed_cases.toString()
            viewBinding.tvRecoveredCases.text = it.global_recovered_cases.toString()
            viewBinding.tvDeathCases.text = it.global_death_cases.toString()
        })

        detailViewModel.countryRecord.observe(this, Observer {
            updateGraph( it.active_cases!!.toInt(),it.confirmed_cases!!.toInt(),it.recovered_cases!!.toInt(),it.death_cases!!.toInt())
            viewBinding.tvActiveCases.text = it.active_cases.toString()
            viewBinding.tvConfirmedCases.text = it.confirmed_cases.toString()
            viewBinding.tvRecoveredCases.text = it.recovered_cases.toString()
            viewBinding.tvDeathCases.text = it.death_cases.toString()

        })

    }

    private fun updateGraph(active: Int, confirmed: Int, recovered: Int, deaths: Int) {

        mpiechart.clearChart()
        mpiechart.addPieSlice(PieModel("Confirm", confirmed.toFloat(), Color.parseColor("#FFB701")))
        mpiechart.addPieSlice(PieModel("Active", active.toFloat(), Color.parseColor("#FF4CAF50")))
        mpiechart.addPieSlice(
            PieModel(
                "Recovered",
                recovered.toFloat(),
                Color.parseColor("#FF2196F3")
            )
        )
        mpiechart.addPieSlice(PieModel("Deaths", deaths.toFloat(), Color.parseColor("#F55C47")))
        mpiechart.startAnimation()
    }

    fun fetchdata(country:String="USA"){
        Log.i("tag_","country "+country)
        detailViewModel.getCountryDetail(country)
    }

}