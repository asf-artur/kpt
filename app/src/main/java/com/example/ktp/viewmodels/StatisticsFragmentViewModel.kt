package com.example.ktp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ktp.App
import com.example.ktp.model.KptRecord
import com.example.ktp.model.repository.KptRecordRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class StatisticsFragmentViewModel : ViewModel() {
    @Inject lateinit var kptRecordRepository: KptRecordRepository

    val data: Observable<List<KptRecord>>

    init {
        App.daggerAppComponent.inject(this)

        data = kptRecordRepository.getAll()
    }
}