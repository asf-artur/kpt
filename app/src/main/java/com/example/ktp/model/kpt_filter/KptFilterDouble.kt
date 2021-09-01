package com.example.ktp.model.kpt_filter

class KptFilterDouble(
        kptFilterType: KptFilterType,
        val double: Double,
        val bigger: Boolean
)
    : KptFilter(kptFilterType)