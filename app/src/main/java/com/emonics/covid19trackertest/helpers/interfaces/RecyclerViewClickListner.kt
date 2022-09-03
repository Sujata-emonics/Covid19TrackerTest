package com.emonics.covid19trackertest.helpers.interfaces

import android.view.View

interface RecyclerViewClickListner {
    fun onRecyclerViewItemClick(view: View, country:String)
}