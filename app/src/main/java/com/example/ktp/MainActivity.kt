package com.example.ktp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ktp.model.KptRecord
import com.example.ktp.model.repository.KptRecordRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.reactivestreams.Subscription
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val kptRepository = KptRecordRepository()
//        kptRepository.add(go())
//        kptRepository.remove(go())
        val data = kptRepository.Data
        val obs = kptRepository.getAll()
                .flatMap {
                    return@flatMap Observable.fromIterable(it)
                }
                .subscribe{
                    log(it.situation)
                }


        val q = 0
    }

    private fun log(text: String){
        Log.d("MYTAG", text)
    }

    private fun go(): KptRecord {
        return KptRecord(
            0,
            "sidfgdgfdgdfgt",
            "dfhjkdhjkdfjd",
            null,
            null,
            null,
            null,
            null,
            Calendar.getInstance(),
            Calendar.getInstance()
        )
    }
}