package com.emonics.covid19trackertest.dataClass

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName="user_table", indices = [Index(value = ["user_name","user_email"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true) val PK_UserID:Int,
    @ColumnInfo(name="user_name") val name:String?,
    @ColumnInfo(name="user_email") val email:String?,
    @ColumnInfo(name="user_password") val password:String,
    @ColumnInfo(name="is_admin", defaultValue = "0") val is_admin:Int,
    @ColumnInfo(name="is_active") val is_active:Int

)
