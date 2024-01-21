package com.matchmate.di

import android.content.Context
import androidx.room.Room
import com.matchmate.database.USER_DATABASE_NAME
import com.matchmate.database.UserDao
import com.matchmate.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun providesUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(context, UserDatabase::class.java, USER_DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    fun providesUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.getUserDao()
    }
}