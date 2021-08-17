package com.example.ktp.di

import com.example.ktp.database.KptRecordTypeConverters
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
//    @Provides
//    @Singleton
//    fun providesKptRecordTypeConverters(gson: Gson): KptRecordTypeConverters {
//        return KptRecordTypeConverters(gson)
//    }

    @Provides
    fun provideGson(): Gson{
        return Gson()
    }
}