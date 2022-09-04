package com.emonics.covid19trackertest.activities

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.databinding.ActivityDetailBinding
import com.emonics.covid19trackertest.helpers.adapter.CityRecyclerAdapter
import com.emonics.covid19trackertest.helpers.adapter.CountryDetailRecyclerAdapter
import com.emonics.covid19trackertest.helpers.dbHandler.DBApplication
import com.emonics.covid19trackertest.helpers.interfaces.RecyclerViewClickListner
import com.emonics.covid19trackertest.repository.DetailViewRepository
import com.emonics.covid19trackertest.viewModel.DetailViewFactory
import com.emonics.covid19trackertest.viewModel.DetailViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_detail.*
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class DetailActivity : AppCompatActivity(), RecyclerViewClickListner {
    lateinit var mpiechart: PieChart
    lateinit var spinner: Spinner
    lateinit var viewBinding:ActivityDetailBinding
    lateinit var detailViewModel: DetailViewModel
    lateinit var repository: DetailViewRepository
    lateinit var countryCodePicker: CountryCodePicker
    //val adapter = CityRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        mpiechart = viewBinding.pieChart
        //spinner = viewBinding.spinner
        var countryName = intent.getStringExtra("RECORDTYPE")

        countryCodePicker = viewBinding.ccp
        viewBinding.tvGlobalHeader.setOnClickListener {
            detailViewModel.getGlobalRecord()
        }
        viewBinding.tvCountryHeader.setOnClickListener {
            var countrySelected = countryCodePicker.getSelectedCountryName()
            fetchdata(countrySelected)
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
            viewBinding.tvCountryHeader.background = resources.getDrawable(R.drawable.switch_track,null)
            //signUp.setTextColor(resources.getColor(R.color.textColor,null))
            viewBinding.tvCountryHeader?.setTextColor(resources.getColor(R.color.textColor))
            viewBinding.tvGlobalHeader?.background = null
            viewBinding.tvGlobalHeader.setTextColor(resources.getColor(R.color.toggle_blue))
            viewBinding.tvGlobalHeader.text ="Global"

            updateGraph( it.global_active_cases!!.toInt(),it.global_confirmed_cases!!.toInt(),it.global_recovered_cases!!.toInt(),it.global_death_cases!!.toInt())
            viewBinding.tvActiveCases.text = it.global_active_cases.toString()
            viewBinding.tvConfirmedCases.text = it.global_confirmed_cases.toString()
            viewBinding.tvRecoveredCases.text = it.global_recovered_cases.toString()
            viewBinding.tvDeathCases.text = it.global_death_cases.toString()
        })

        detailViewModel.countryRecord.observe(this, Observer {
            if(it!=null) {
              //  viewBinding.tvCountryHeader.setTextColor(resources.getColor(R.color.toggle_blue))
                viewBinding.tvGlobalHeader.background = resources.getDrawable(R.drawable.switch_track,null)
                //signUp.setTextColor(resources.getColor(R.color.textColor,null))
                viewBinding.tvGlobalHeader?.setTextColor(resources.getColor(R.color.textColor))
                viewBinding.tvCountryHeader?.background = null
                viewBinding.tvCountryHeader.setTextColor(resources.getColor(R.color.toggle_blue))
                viewBinding.tvCountryHeader.text = country
                updateGraph(
                    it.active_cases!!.toInt(),
                    it.confirmed_cases!!.toInt(),
                    it.recovered_cases!!.toInt(),
                    it.death_cases!!.toInt()
                )
                viewBinding.tvActiveCases.text = it.active_cases.toString()
                viewBinding.tvConfirmedCases.text = it.confirmed_cases.toString()
                viewBinding.tvRecoveredCases.text = it.recovered_cases.toString()
                viewBinding.tvDeathCases.text = it.death_cases.toString()
            } else {
                Log.d("tag_", "The data does not available: $it")
            }
        })


        detailViewModel.errormsg.observe(this, Observer {
            Toast.makeText(this, "The data does not available for the selected country",Toast.LENGTH_SHORT).show()
            updateGraph(0,0,0,0)
            viewBinding.tvActiveCases.text = ""
            viewBinding.tvConfirmedCases.text = ""
            viewBinding.tvRecoveredCases.text = ""
            viewBinding.tvDeathCases.text = ""
        })

        detailViewModel.allCountryRecord.observe(this, Observer {
            Log.d("tag_", "onCreate: $it")
            it.forEach {

            }
            val adapter = CountryDetailRecyclerAdapter(this, recordFor = "global")

            viewBinding.recyclerview.adapter = adapter
            adapter.setCountryList(it)
        })

        detailViewModel.cityRecord.observe(this, Observer {
            Log.d("tag_", "onCreate: $it")
            it.forEach {

            }
            val adapter = CountryDetailRecyclerAdapter(this, recordFor = "country")

            viewBinding.recyclerview.adapter = adapter
            adapter.setCityList(it)
        })



        viewBinding.tvSignOut.setOnClickListener {
            signOut()
        }
        viewBinding.btnBack.setOnClickListener {
            Log.i("tag_","inside onclick")
            //adminVieModel.saveOrUpdate()
            finish()
        }

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

        //  viewBinding.tvCountryHeader.setTextColor(resources.getColor(R.color.toggle_blue))
        viewBinding.tvGlobalHeader.background = resources.getDrawable(R.drawable.switch_track,null)
        //signUp.setTextColor(resources.getColor(R.color.textColor,null))
        viewBinding.tvGlobalHeader?.setTextColor(resources.getColor(R.color.textColor))
        viewBinding.tvCountryHeader?.background = null
        viewBinding.tvCountryHeader.setTextColor(resources.getColor(R.color.toggle_blue))
        viewBinding.tvCountryHeader.text = country
        detailViewModel.getCountryDetail(country)
    }

    override fun onRecyclerViewItemClick(view: View, country: String) {
            var intent = Intent(this,SparkViewActivity::class.java)
            intent.putExtra("Country",country)
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