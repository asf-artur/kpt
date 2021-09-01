package com.example.ktp.di

import com.example.ktp.database.KptRecordTypeConverters
import com.example.ktp.model.repository.KptRecordRepository
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
    @Singleton
    fun prov(): KptRecordRepository{
        return KptRecordRepository()
    }

    @Provides
    fun provideGson(): Gson{
        return Gson()
    }
}