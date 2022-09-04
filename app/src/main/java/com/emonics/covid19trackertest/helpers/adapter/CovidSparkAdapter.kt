package com.emonics.covid19trackertest.helpers.adapter

import android.graphics.RectF
import com.emonics.covid19trackertest.dataClass.SparkView
import com.emonics.covid19trackertest.helpers.enumData.Metric
import com.emonics.covid19trackertest.helpers.enumData.TimeScale
import com.robinhood.spark.SparkAdapter

class CovidSparkAdapter(private val dailyData: List<SparkView>) : SparkAdapter() {

    var metric = Metric.POSITIVE
    var daysAgo = TimeScale.MAX

    override fun getCount() = dailyData.size

    override fun getItem(index: Int) = dailyData[index]

    override fun getY(index: Int): Float {
        val chosenDayData = dailyData[index]
        return when (metric) {
            Metric.NEGATIVE -> chosenDayData.negativeIncrease.toFloat()
            Metric.POSITIVE -> chosenDayData.positiveIncrease.toFloat()
            Metric.DEATH -> chosenDayData.deathIncrease.toFloat()
        }

    }

    override fun getDataBounds(): RectF {
        val bounds = super.getDataBounds()
        if (daysAgo != TimeScale.MAX) {
            bounds.left = count - daysAgo.numDays.toFloat()
        }
        return bounds
    }

}