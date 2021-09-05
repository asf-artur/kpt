package com.example.ktp.excel

import com.example.ktp.constants.ExcelHeadersConstants
import com.example.ktp.model.KptRecord
import com.example.ktp.model.toRusString
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ExcelService {
    fun writeFile(){

    }

    fun createExcelWorkBook(kptRecords: List<KptRecord>): XSSFWorkbook {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("КПТ")

        createHeaderRow(sheet)
        kptRecords.forEach {
            createRecordRow(it, sheet)
        }

        return workbook
    }

    private fun createRecordRow(kptRecord: KptRecord, sheet: XSSFSheet){
        val nextRowNum = sheet.lastRowNum + 1
        val row = sheet.createRow(nextRowNum)
        val setCell = { value: String? -> row.createCell(row.physicalNumberOfCells).apply { setCellValue(value) } }
        val setCellList = { valueList: List<String>? -> row.createCell(row.physicalNumberOfCells).apply {
            if(valueList?.isNotEmpty() == true){
                val value = valueList.reduce{ acc, s -> acc + ", " + s } ?: ""
                setCellValue(value)
            }
        } }

        setCell(kptRecord.situation)
        setCell(kptRecord.automaticThought ?: "")
        setCellList(kptRecord.emotionalReactions)
        setCell(kptRecord.bodilyReactions)
        setCell(kptRecord.behavior)
        setCell(kptRecord.truthOfThought?.toString() ?: "0.0")
        setCellList(kptRecord.thinkingErrors?.map { it.toRusString() })
    }

    private fun createHeaderRow(sheet: XSSFSheet){
        val row = sheet.createRow(0)
        val setCell = { value: String? -> row.createCell(row.physicalNumberOfCells).apply { setCellValue(value) } }

        ExcelHeadersConstants.Headers.forEach {
            setCell(it)
        }
    }
}