package com.emonics.covid19trackertest.helpers.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.dataClass.Country
import com.emonics.covid19trackertest.databinding.AdapterCountryBinding
import com.emonics.covid19trackertest.helpers.interfaces.RecyclerViewClickListner

class CountryDetailRecyclerAdapter(private val listener: RecyclerViewClickListner, private val recordFor:String) :RecyclerView.Adapter<CountryDetailRecyclerAdapter.CountryViewHolder>() {
    var countries = mutableListOf<Country>()
    var cities = mutableListOf<City>()
    fun setCountryList(countryList: List<Country>) {
        this.countries = countryList.toMutableList()
        notifyDataSetChanged()
    }
    fun setCityList(cityList: List<City>) {
        this.cities = cityList.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterCountryBinding.inflate(inflater, parent, false)//AdapterCityBinding is the adapter_city.xml
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        Log.i("tag_", "item recordFor" + recordFor)
        if(recordFor == "global") {
            val country = countries[position]
            Log.i("tag_", "item country" + country + "==" + position)
            holder.binding.tvCountryName.text = country.country_name.toString()
            holder.binding.tvActiveCases.text = country.active_cases.toString()
            holder.binding.tvRecoveredCases.text = country.confirmed_cases.toString()
            //holder.binding.tvRecoveredCases.text = country.recovered_cases.toString()
            //Glide.with(holder.itemView.context).load(movie.imageUrl).into(holder.binding.imageview)
            holder.binding.tvCountryName.setOnClickListener {
                listener.onRecyclerViewItemClick(holder.binding.tvCountryName,country.country_name.toString())
            }       } else{
            val city = cities[position]
            Log.i("tag_", "item country" + city + "==" + position)
            holder.binding.tvCountryName.text = city.name.toString()
            holder.binding.tvActiveCases.text = city.active_cases.toString()
            holder.binding.tvRecoveredCases.text = city.confirmed_cases.toString()
            //holder.binding.tvRecoveredCases.text = country.recovered_cases.toString()
            //Glide.with(holder.itemView.context).load(movie.imageUrl).into(holder.binding.imageview)
            holder.binding.tvCountryName.setOnClickListener {
                listener.onRecyclerViewItemClick(holder.binding.tvCountryName,city.name.toString())
            }

        }

    }

    override fun getItemCount(): Int {
        Log.i("tag_","item count"+countries.size)
        if(recordFor == "global") {
            return countries.size

        }else{
            return cities.size

        }
    }
    inner class CountryViewHolder(val binding: AdapterCountryBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}
