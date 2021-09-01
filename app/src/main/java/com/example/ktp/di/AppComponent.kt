package com.example.ktp.di

import com.example.ktp.MainActivity
import com.example.ktp.database.KptRecordTypeConverters
import com.example.ktp.model.repository.KptRecordRepository
import com.example.ktp.ui.AddKptThoughtFragment
import com.example.ktp.ui.FullInfoKptRecordFragment
import com.example.ktp.ui.MainFragment
import com.example.ktp.viewmodels.FullInfoKptRecordFragmentViewModel
import com.example.ktp.viewmodels.MainFragmentViewModel
import com.example.ktp.viewmodels.StatisticsFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RoomModule::class, AppModule::class, KptServicesModule::class])
@Singleton
interface AppComponent {
    fun inject(kptRecordTypeConverters: KptRecordTypeConverters)
    fun inject(kptRecordRepository: KptRecordRepository)
    fun inject(mainFragment: MainFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(addKptThoughtFragment: AddKptThoughtFragment)

    fun inject(mainFragmentViewModel: MainFragmentViewModel)
    fun inject(fullInfoKptRecordFragmentViewModel: FullInfoKptRecordFragmentViewModel)
    fun inject(fullInfoKptRecordFragment: FullInfoKptRecordFragment)
    fun inject(statisticsFragmentViewModel: StatisticsFragmentViewModel)
}