package com.example.ktp.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ktp.model.KptRecord
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface IRepositoryDao<T> {
    fun getAll() : Observable<List<T>>

    fun insert(element: T)

    fun update(element: T)

    fun delete(element: T)
}