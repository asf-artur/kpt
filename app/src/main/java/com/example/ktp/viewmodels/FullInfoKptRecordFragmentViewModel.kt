package com.example.ktp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ktp.App
import com.example.ktp.model.KptRecord
import com.example.ktp.model.repository.KptRecordRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FullInfoKptRecordFragmentViewModel : ViewModel() {
    @Inject
    lateinit var kptRecordRepository: KptRecordRepository

    init {
        App.daggerAppComponent.inject(this)
    }

    fun getItem(id: Int): Observable<KptRecord> {
        return kptRecordRepository.get(id)
    }

    fun deleteItem(kptRecord: KptRecord){
        kptRecordRepository.remove(kptRecord)
    }

    fun update(kptRecord: KptRecord){
        kptRecordRepository.update(kptRecord)
    }
}