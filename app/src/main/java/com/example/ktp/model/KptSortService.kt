package com.example.ktp.model

import com.example.ktp.model.kpt_sort.KptSort
import com.example.ktp.model.kpt_sort.KptSortType
import java.lang.Exception

class KptSortService {
    fun sort(kptRecords: List<KptRecord>, kptSort: KptSort): List<KptRecord>{
        return when(kptSort.kptSortType){
            KptSortType.creationDate -> sortByCreationDate(kptRecords, kptSort)
            KptSortType.changeDate -> sortByChangeDate(kptRecords, kptSort)
            KptSortType.truthOfThought -> sortByTruthOfThought(kptRecords, kptSort)
            else -> throw Exception("enum ${kptSort.kptSortType} error")
        }
    }

    fun sortByCreationDate(kptRecords: List<KptRecord>, kptSort: KptSort): List<KptRecord>{
        return if (kptSort.bigger){
            kptRecords.sortedBy {
                it.creationDate
            }
        }
        else{
            kptRecords.sortedByDescending {
                it.creationDate
            }
        }
    }

    fun sortByChangeDate(kptRecords: List<KptRecord>, kptSort: KptSort): List<KptRecord>{
        return if (kptSort.bigger){
            kptRecords.sortedBy {
                it.changeDate
            }
        }
        else{
            kptRecords.sortedByDescending {
                it.changeDate
            }
        }
    }

    fun sortByTruthOfThought(kptRecords: List<KptRecord>, kptSort: KptSort): List<KptRecord>{
        return if (kptSort.bigger){
            kptRecords.sortedBy {
                it.truthOfThought
            }
        }
        else{
            kptRecords.sortedByDescending {
                it.truthOfThought
            }
        }
    }
}