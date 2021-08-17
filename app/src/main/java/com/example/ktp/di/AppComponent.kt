package com.example.ktp.di

import com.example.ktp.database.KptRecordTypeConverters
import com.example.ktp.model.repository.KptRecordRepository
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RoomModule::class, AppModule::class])
@Singleton
interface AppComponent {
    fun inject(kptRecordTypeConverters: KptRecordTypeConverters)
    fun inject(kptRecordRepository: KptRecordRepository)
}