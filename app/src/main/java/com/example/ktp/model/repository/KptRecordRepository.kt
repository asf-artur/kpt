package com.example.ktp.model.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ktp.App
import com.example.ktp.database.IRepositoryDao
import com.example.ktp.database.KptRecordDatabase
import com.example.ktp.model.KptRecord
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

private val DATABASE_NAME = "kptRecord-database"
class KptRecordRepository()
    : ADataListRepository<KptRecord>(){
    @Inject lateinit var applicationContext: Context

    init {
        App.daggerAppComponent.inject(this)
    }

    override val database = Room.databaseBuilder(
        applicationContext,
        KptRecordDatabase::class.java,
        DATABASE_NAME
    ).build()
    override val dao = database.kptDao()

    fun getAll1(): Flowable<KptRecord> {
        return dao.getAll1()
    }
}