package com.example.ktp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.ktp.model.services.KptFilterService
import com.example.ktp.model.KptRecord
import com.example.ktp.model.services.KptSortService
import com.example.ktp.model.ThinkingError
import com.example.ktp.model.kpt_filter.KptFilterText
import com.example.ktp.model.kpt_filter.KptFilterType
import com.example.ktp.model.kpt_sort.KptSort
import com.example.ktp.model.kpt_sort.KptSortType
import com.example.ktp.model.repository.KptRecordRepository
import com.example.ktp.ui.IFragmentTransactions
import com.example.ktp.ui.fragments.MainFragment
import com.example.ktp.ui.fragments.StatisticsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IFragmentTransactions {
    @Inject lateinit var kptRecordRepository: KptRecordRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.daggerAppComponent.inject(this)
//        fillKptDb()
//        kptRecordRepository.deleteAll()

        if(savedInstanceState == null){
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container_view, MainFragment())
//                    .add(R.id.fragment_container_view, AddKptThoughtFragment())
                    .commit()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item1 ->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, MainFragment())
                        .commit()
                    true
                }
                R.id.item2 ->{
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, StatisticsFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        val q = 0
    }

    override fun goToFragment(fragment: Fragment){
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .commit()
    }

    private fun do1(){
        val kptRepository = KptRecordRepository()
        val data = kptRepository.Data
//        val obs = kptRepository.getAll()
//                .flatMap {
//                    return@flatMap Observable.fromIterable(it)
//                }
//                .subscribe({
//                    log(it.situation)
//                },
//                {e ->
//                    log("error ${e.message}")},
//                {log("success")})

        val obs2 = kptRepository.getAll()
                .subscribe{
                    val kptFilterService = KptFilterService()
                    val kptSortService = KptSortService()
                    val filter = KptFilterText(KptFilterType.bodilyReactions, "a")
                    val filtered = kptFilterService.filter(it, filter)
                    val sorted = kptSortService.sort(filtered, KptSort(KptSortType.changeDate, true))
                    val qq = 0
                }
    }

    private fun log(text: String){
        Log.d("MYTAG", text)
    }

    private fun go(): KptRecord {
        return KptRecord(
            "sidfgdgfdgdfgt",
            "dfhjkdhjkdfjd",
            listOf("a","b"),
            null,
            null,
            null,
            null,
            Calendar.getInstance(),
            Calendar.getInstance()
        )
    }

    private fun fillKptDb(){
        val a = KptRecord(
                "Ситуация 111111 Что-то произошло и теперь я сильно переживаю",
                "Автоматическая мысль 1111111111111",
                listOf("Эмоциональная реакция 1","Эмоциональная реакция 2"),
                "Телесная реакция 1, Телесная реакция 2",
                "Поведение 1",
                10.0,
                listOf(ThinkingError.DichotomousThinking),
                Calendar.getInstance(),
                Calendar.getInstance()
        )

        val b = KptRecord(
                "Ситуация 222222  Что-то произошло и теперь я сильно переживаю  Что-то произошло и теперь я сильно переживаю",
                "Автоматическая мысль 22222222222222",
                listOf("Эмоциональная реакция 2","Эмоциональная реакция 3"),
                "Телесная реакция 2, Телесная реакция 3",
                "Поведение 2",
                40.0,
                listOf(ThinkingError.DichotomousThinking, ThinkingError.Catastrophization),
                Calendar.getInstance(),
                Calendar.getInstance()
        )

        val c = KptRecord(
                "Ситуация 333333333  Что-то произошло и теперь я сильно переживаю  Что-то произошло и теперь я сильно переживаю  Что-то произошло и теперь я сильно переживаю",
                "Автоматическая мысль 333333333333",
                listOf("Эмоциональная реакция 1","Эмоциональная реакция 3"),
                "Телесная реакция 1, Телесная реакция 3",
                "Поведение 3",
                90.0,
                listOf(ThinkingError.DichotomousThinking, ThinkingError.Catastrophization, ThinkingError.DevaluationOfThePositive),
                Calendar.getInstance(),
                Calendar.getInstance()
        )

        kptRecordRepository.add(a)
        kptRecordRepository.add(b)
        kptRecordRepository.add(c)
    }
}