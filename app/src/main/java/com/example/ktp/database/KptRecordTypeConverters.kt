package com.example.ktp.database

import androidx.room.TypeConverter
import com.example.ktp.App
import com.example.ktp.di.DaggerAppComponent
import com.example.ktp.model.ThinkingError
import com.google.gson.Gson
import java.util.*
import javax.inject.Inject

class KptRecordTypeConverters{
    init{
        App.daggerAppComponent.inject(this)
    }
    @Inject lateinit var gson: Gson

    @TypeConverter
    fun fromCalendar(calendar: Calendar?): Long? {
        return calendar?.time?.time
    }

    @TypeConverter
    fun toCalendar(timeInMillis: Long?): Calendar?{
        if(timeInMillis == null){
            return null
        }

        return Calendar.getInstance()
            .apply {
                this.timeInMillis = timeInMillis
            }
    }

    @TypeConverter
    fun fromListString(listOfString: List<String>?): String?{
        return gson.toJson(listOfString)
    }

    @TypeConverter
    fun toListString(string: String?): List<String>?{
        if(string == null){
            return null
        }

        return gson.fromJson(string, JsonListString::class.java)?.list
    }

    @TypeConverter
    fun fromThinkingErrorList(thinkingErrorList: List<ThinkingError>?): String?{
        return gson.toJson(thinkingErrorList)
    }

    @TypeConverter
    fun toThinkingErrorList(string: String?): List<ThinkingError>?{
        if(string == null){
            return null
        }

        return gson.fromJson(string, JsonListThinkingError::class.java)?.list
    }
}