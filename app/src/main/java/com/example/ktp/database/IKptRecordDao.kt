package com.example.ktp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ktp.model.KptRecord
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface IKptRecordDao : IRepositoryDao<KptRecord> {
    @Query("SELECT * from KptRecord")
    fun getAll1() : Flowable<KptRecord>

    @Query("SELECT * from KptRecord")
    override fun getAll() : Observable<List<KptRecord>>

    @Insert
    override fun insert(element: KptRecord)

    @Update
    override fun update(element: KptRecord)

    @Delete
    override fun delete(element: KptRecord)
}