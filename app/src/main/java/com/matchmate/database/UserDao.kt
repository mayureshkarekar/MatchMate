package com.matchmate.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matchmate.model.USER_TABLE_NAME
import com.matchmate.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUsers(users: List<User>)

    @Query("SELECT * FROM $USER_TABLE_NAME")
    suspend fun getUsers(): List<User>
}