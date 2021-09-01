package com.example.ktp.model.repository

import android.content.Context
import androidx.room.RoomDatabase
import com.example.ktp.database.IRepositoryDao
import com.example.ktp.model.KptRecord
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.Executors

abstract class ADataListRepository<T>() {
    protected abstract val database: RoomDatabase
    protected abstract val dao: IRepositoryDao<T>
    private var _data: MutableList<T> = mutableListOf()

    protected val executor = Executors.newSingleThreadExecutor()

    fun get(id: Int): Observable<T> {
        return dao.get(id)
    }

    fun getAll(): Observable<List<T>> {
        return dao.getAll()
    }

    val Data: List<T>
        get() { return _data }

    fun update(element: T){
        executor.execute {
            dao.update(element)
        }
    }

    open fun add(element: T){
        _data.add(element)
        executor.execute {
            dao.insert(element)
        }
    }

    open fun remove(element: T){
        try {
            executor.execute {
                _data.remove(element)
                dao.delete(element)
            }
        }
        catch (e: Exception) {
        }
    }
}