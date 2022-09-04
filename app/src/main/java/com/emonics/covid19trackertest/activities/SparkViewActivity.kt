package com.emonics.covid19trackertest.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.emonics.covid19trackertest.R
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.dataClass.SparkView
import com.emonics.covid19trackertest.databinding.ActivitySparkViewBinding
import com.emonics.covid19trackertest.helpers.adapter.CovidSparkAdapter
import com.emonics.covid19trackertest.helpers.enumData.Metric
import com.emonics.covid19trackertest.helpers.enumData.TimeScale
import com.emonics.covid19trackertest.helpers.retrofit.RetrofitSparkViewService
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.robinhood.ticker.TickerUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val BASE_URL = "https://api.covidtracking.com/v1/"
private const val TAG = "SparkViewActivity"
private const val ALL_STATES: String = "All (Nationwide)"


class SparkViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySparkViewBinding

    private lateinit var currentlyShownData: List<SparkView>
    private lateinit var adapter: CovidSparkAdapter
    private lateinit var perStateDailyData: Map<String, List<SparkView>>
    private lateinit var nationalDailyData: List<SparkView>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySparkViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Covid Tracker"
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val covidService = retrofit.create(RetrofitSparkViewService::class.java)
        var countryName = intent.getStringExtra("Country")
        binding.tvSelectState.text = "Covid-19 Cases of "+countryName
        covidService.getNationalData().enqueue(object : Callback<List<SparkView>> {
            override fun onResponse(
                call: Call<List<SparkView>>,
                response: Response<List<SparkView>>
            ) {
                Log.i(TAG, "onResponse $response")
                val nationalData = response.body()
                Log.i("tag_","nationalData"+nationalData)
                if (nationalData == null) {
                    Log.w(TAG, "Did not receive a valid response body")
                    return
                }
                setupEventListeners()
                nationalDailyData = nationalData.reversed()
                Log.i("tag_","nationalDailyData"+nationalDailyData)
                updateDisplayWithData(nationalDailyData)
            }

            override fun onFailure(call: Call<List<SparkView>>, t: Throwable) {
                Log.e(TAG, "onFailure $t")
            }

        })

        var stateList = ArrayList<String>()
        covidService.getStatesData().enqueue(object : Callback<List<SparkView>> {
            override fun onResponse(
                call: Call<List<SparkView>>,
                response: Response<List<SparkView>>
            ) {
                Log.i(TAG, "onResponse $response")
                val statesData = response.body()
                if (statesData == null) {
                    Log.w(TAG, "Did not receive a valid response body")
                    return
                } else {
                    response.body()!!.forEach {
                        stateList.add(it.state)

                    }
                }
                perStateDailyData = statesData.reversed().groupBy { it.state }
                updateSpinnerWithStateData(perStateDailyData.keys)
            }

            override fun onFailure(call: Call<List<SparkView>>, t: Throwable) {
                Log.e(TAG, "onFailure $t")

            }
        })

        binding.tvSignOut.setOnClickListener {
            signOut()
        }
        binding.btnBack.setOnClickListener {
            Log.i("tag_","inside onclick")
            //adminVieModel.saveOrUpdate()
            finish()
        }

        // access the spinner
        /*var spinner = binding.spinnerSelect
        if (spinner!=null){
            val adapterState = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, stateList)
            spinner.adapter = adapterState


            spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }*/
    }


    private fun updateSpinnerWithStateData(stateNames: Set<String>) {
        val stateAbbreviationList = stateNames.toMutableList()
        stateAbbreviationList.sort()
        stateAbbreviationList.add(0, ALL_STATES)


    }

    private fun setupEventListeners() {
        binding.tickerView.setCharacterLists(TickerUtils.provideNumberList())
        binding.sparkView.isScrubEnabled = true
        binding.sparkView.setScrubListener { itemData ->
            if (itemData is SparkView) {
                updateInfoForDate(itemData)

            }
        }


        binding.radioGroupTimeSelection.setOnCheckedChangeListener { _, checkedId ->
            adapter.daysAgo = when (checkedId) {
                R.id.radioButtonWeek -> TimeScale.WEEK
                R.id.radioButtonMonth -> TimeScale.MONTH
                else -> TimeScale.MAX
            }
            adapter.notifyDataSetChanged()
        }
        binding.radioGroupMetricSelection.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonPositive -> updateDisplayMetric(Metric.POSITIVE)
                R.id.radioButtonNegative -> updateDisplayMetric(Metric.NEGATIVE)
                R.id.radioButtonDeath -> updateDisplayMetric(Metric.DEATH)
            }
        }
    }

    private fun updateDisplayMetric(metric: Metric) {
        val colorRes = when (metric){
            Metric.NEGATIVE -> R.color.colorNegative
            Metric.POSITIVE -> R.color.colorPositive
            Metric.DEATH -> R.color.colorDeath
        }
        @ColorInt val colorInt = ContextCompat.getColor(this, colorRes)
        binding.sparkView.lineColor = colorInt
        binding.tickerView.setTextColor(colorInt)

        adapter.metric = metric
        adapter.notifyDataSetChanged()

        updateInfoForDate(currentlyShownData.last())
    }

    private fun updateDisplayWithData(dailyData: List<SparkView>) {

        currentlyShownData = dailyData

        adapter = CovidSparkAdapter(dailyData)
        binding.sparkView.adapter = adapter

        binding.radioButtonPositive.isChecked = true
        binding.radioButtonMax.isChecked = true

        updateDisplayMetric(Metric.POSITIVE)
    }

    private fun updateInfoForDate(covidData: SparkView) {
        val numCases = when(adapter.metric) {
            Metric.NEGATIVE -> covidData.negativeIncrease
            Metric.POSITIVE -> covidData.negativeIncrease
            Metric.DEATH -> covidData.deathIncrease
        }
        binding.tickerView.text = NumberFormat.getInstance().format(numCases)
        var outputDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        binding.tvDateLabel.text = outputDateFormat.format(covidData.dateChecked)

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