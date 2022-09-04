package com.emonics.covid19trackertest.dataClass

import java.util.*

data class SparkView(
    val dateChecked: Date,
    val positiveIncrease: Int,
    val negativeIncrease: Int,
    val deathIncrease: Int,
    val state: String
)
