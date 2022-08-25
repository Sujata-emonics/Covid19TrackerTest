package com.emonics.covid19trackertest.Room

import androidx.room.*
import com.emonics.covid19trackertest.dataClass.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user_table")
    fun getAllUsers():List<User>
    @Query("SELECT * FROM user_table WHERE PK_UserID LIKE :userId LIMIT 1")
    suspend fun findByUserID(userId: Int): User
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: List<User>)
    @Delete
    suspend fun delete(user: User)
    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()
    @Query("DELETE FROM user_table WHERE PK_UserID LIKE :userId")
    suspend fun deleteByUserId(userId:Int)
}