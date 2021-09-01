package com.example.ktp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.ktp.App
import com.example.ktp.excel.ExcelService
import com.example.ktp.model.KptRecord
import com.example.ktp.model.repository.KptRecordRepository
import io.reactivex.rxjava3.core.Observable
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.inject.Inject

class MainFragmentViewModel : ViewModel() {
    @Inject
    lateinit var kptRecordRepository: KptRecordRepository
    @Inject lateinit var excelService: ExcelService

    val data: Observable<List<KptRecord>>

    init {
        App.daggerAppComponent.inject(this)

        data = kptRecordRepository.getAll()
    }

    fun createExcelWorkBook(items: List<KptRecord>): XSSFWorkbook {
        return excelService.createExcelWorkBook(items)
    }
}