package com.example.ktp.di

import com.example.ktp.excel.ExcelService
import com.example.ktp.model.KptFilterService
import dagger.Module
import dagger.Provides

@Module
class KptServicesModule {
    @Provides
    fun provideKptFilterService(): KptFilterService {
        return KptFilterService()
    }

    @Provides
    fun provideExcelService(): ExcelService{
        return ExcelService()
    }
}