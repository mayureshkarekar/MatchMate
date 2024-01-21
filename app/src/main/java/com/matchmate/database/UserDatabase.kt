package com.matchmate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.matchmate.model.User
import com.matchmate.model.UserTypeConverters

@Database(entities = [User::class], version = USER_DATABASE_VERSION)
@TypeConverters(UserTypeConverters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}

// region constants
const val USER_DATABASE_NAME = "user_database"
private const val USER_DATABASE_VERSION = 1
// endregion