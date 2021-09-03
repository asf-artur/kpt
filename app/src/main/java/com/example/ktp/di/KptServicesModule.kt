package com.example.ktp.di

import androidx.fragment.app.FragmentTransaction
import com.example.ktp.excel.ExcelService
import com.example.ktp.model.KptFilterService
import com.example.ktp.model.repository.KptRecordRepository
import com.example.ktp.ui.IFragmentTransactions
import com.example.ktp.utils.UiKptRecordHelper
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

    @Provides
    fun provideUiKptRecordHelper(kptRecordRepository: KptRecordRepository): UiKptRecordHelper{
        return  UiKptRecordHelper(kptRecordRepository)
    }
}