package com.emonics.covid19trackertest.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="global_table")
data class Global(
    @PrimaryKey(autoGenerate = true) var PK_GlobalID:Int,
    @ColumnInfo(name="global_confirmed_cases") var global_confirmed_cases:Long?,
    @ColumnInfo(name="global_active_cases") var global_active_cases:Long?,
    @ColumnInfo(name="global_recovered_cases") var global_recovered_cases:Long?,
    @ColumnInfo(name="global_death_cases(") var global_death_cases:Long?,
    @ColumnInfo(name="global_test_cases") var global_test_cases:Long?
)
