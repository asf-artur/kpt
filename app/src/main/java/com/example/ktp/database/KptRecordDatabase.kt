package com.example.ktp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.ktp.model.KptRecord
import com.example.ktp.model.ThinkingError
import com.google.gson.Gson
import java.util.*

@Database(entities = [KptRecord::class], version = 1)
@TypeConverters(KptRecordTypeConverters::class)
abstract class KptRecordDatabase() : RoomDatabase() {
    abstract fun kptDao(): IKptRecordDao
}