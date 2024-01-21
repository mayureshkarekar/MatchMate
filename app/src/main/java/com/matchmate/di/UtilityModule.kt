package com.matchmate.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UtilityModule {
    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()
}