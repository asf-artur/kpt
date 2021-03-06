package com.example.ktp.utils

import java.util.*

fun Calendar.toDate2DigitsString(): String {
    return String.format("%1\$te.%1\$tm", this)
}

fun Calendar.toDateString(): String {
    return "${this.get(Calendar.DAY_OF_MONTH)}.${this.get(Calendar.MONTH)+1}.${this.get(Calendar.YEAR)}"
}

fun Calendar.toDateTimeString(): String {
    return "${this.get(Calendar.DAY_OF_MONTH)}.${this.get(Calendar.MONTH)+1}.${this.get(Calendar.YEAR)}" +
            " ${this.get(Calendar.HOUR_OF_DAY)}:${this.get(Calendar.MINUTE)}"
}

fun Date?.toCalendar(): Calendar?{
    if(this == null){
        return null
    }

    return Calendar.getInstance()
            .apply {
                time = this@toCalendar
            }
}