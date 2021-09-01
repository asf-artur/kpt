package com.example.ktp.model.kpt_filter

import com.example.ktp.model.ThinkingError

class KptFilterThinkingError(
        kptFilterType: KptFilterType,
        val thinkingError: ThinkingError,
)
    : KptFilter(kptFilterType)