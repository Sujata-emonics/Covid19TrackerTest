package com.emonics.covid19trackertest.helpers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emonics.covid19trackertest.dataClass.City
import com.emonics.covid19trackertest.databinding.AdapterCityBinding

class CityRecyclerAdapter:RecyclerView.Adapter<CityRecyclerAdapter.CityViewHolder>() {
    var cities = mutableListOf<City>()
    fun setCityList(citiesVar: List<City>) {
        this.cities = citiesVar.toMutableList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =AdapterCityBinding.inflate(inflater, parent, false)//AdapterCityBinding is the adapter_city.xml
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.binding.tvCityName.text = city.name
        holder.binding.tvActiveCases.text = "Active Cases: "+city.active_cases.toString()
        holder.binding.tvConfirmedCases.text = "Confirmed Cases: "+city.confirmed_cases.toString()
        holder.binding.tvRecoveredCases.text = "Recovered Cases: "+city.recovered_cases.toString()
        holder.binding.tvDeathCases.text = "Death Cases: "+city.death_cases.toString()
        //Glide.with(holder.itemView.context).load(movie.imageUrl).into(holder.binding.imageview)
    }

    override fun getItemCount(): Int {
        return cities.size
    }
   inner class CityViewHolder(val binding: AdapterCityBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}