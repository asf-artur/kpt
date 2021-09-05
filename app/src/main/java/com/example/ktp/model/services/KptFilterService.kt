package com.example.ktp.model.services

import com.example.ktp.model.KptRecord
import com.example.ktp.model.kpt_filter.*
import java.lang.Exception

class KptFilterService {
    fun filter(kptRecords: List<KptRecord>, kptFilter: KptFilter): List<KptRecord>{
        return when(kptFilter.kptFilterType){
            KptFilterType.emotionalReactions -> filterByEmotionalReactions(kptRecords, kptFilter as KptFilterText)
            KptFilterType.bodilyReactions -> filterByBodilyReactions(kptRecords, kptFilter as KptFilterText)
            KptFilterType.truthOfThoughtBigger -> filterByTruthOfThought(kptRecords, kptFilter as KptFilterDouble)
            KptFilterType.thinkingErrors -> filterByThinkingErrors(kptRecords, kptFilter as KptFilterThinkingError)
            else -> throw Exception("${kptFilter.kptFilterType} error")
        }
    }

    private fun filterByEmotionalReactions(kptRecords: List<KptRecord>, kptFilterText: KptFilterText): List<KptRecord>{
        return kptRecords.filter {
            it.emotionalReactions?.contains(kptFilterText.filterText) ?: false
        }
    }

    private fun filterByBodilyReactions(kptRecords: List<KptRecord>, kptFilterText: KptFilterText): List<KptRecord>{
        return kptRecords.filter {
            it.bodilyReactions?.contains(kptFilterText.filterText) ?: false
        }
    }

    private fun filterByTruthOfThought(kptRecords: List<KptRecord>, kptFilterDouble: KptFilterDouble): List<KptRecord>{
        return kptRecords.filter {
            if(kptFilterDouble.bigger){
                return@filter (it.truthOfThought ?: 0.0) < kptFilterDouble.double
            }

            (it.truthOfThought ?: 0.0) > kptFilterDouble.double
        }
    }

    private fun filterByThinkingErrors(kptRecords: List<KptRecord>, kptFilterThinkingError: KptFilterThinkingError): List<KptRecord>{
        return kptRecords.filter {
            it.thinkingErrors?.contains(kptFilterThinkingError.thinkingError) ?: false
        }
    }
}