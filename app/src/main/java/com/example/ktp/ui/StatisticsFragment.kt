package com.example.ktp.ui

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ktp.R
import com.example.ktp.constants.EmotionalReactionsConstants
import com.example.ktp.model.KptRecord
import com.example.ktp.model.ThinkingError
import com.example.ktp.model.toRusString
import com.example.ktp.ui.adapters.StatisticsAdapter
import com.example.ktp.viewmodels.StatisticsFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    val statisticsFragmentViewModel: StatisticsFragmentViewModel by viewModels()
    lateinit var disposable: Disposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kptRecordsObservable = statisticsFragmentViewModel.data
        disposable = kptRecordsObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { kptRecords ->
                val emotionalReactionsCount = getEmotionalReactionsCount(kptRecords)
                val thinkingErrorsCount = getThinkingErrorsCount(kptRecords)

                val emotionalReactionsView = view.findViewById<LinearLayout>(R.id.emotionalReactions)
                emotionalReactionsView.findViewById<LinearLayout>(R.id.content)
                    .apply {
                        visibility = View.VISIBLE
                    }
                val option_add_minimized = emotionalReactionsView
                    .findViewById<ConstraintLayout>(R.id.option_add_minimized)

                option_add_minimized
                    .findViewById<FrameLayout>(R.id.frameLayout)
                    .apply {
                        visibility = View.GONE
                    }

                option_add_minimized
                    .findViewById<TextView>(R.id.text)
                    .apply {
                        text = "Статистика эмоциональных реакций"
                    }

                val emotionalReactionsRecyclerView = emotionalReactionsView.findViewById<LinearLayout>(R.id.content)
                    .findViewById<RecyclerView>(R.id.recyclerView)
                emotionalReactionsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                emotionalReactionsRecyclerView.adapter = StatisticsAdapter(emotionalReactionsCount, R.layout.el2)

                val thinkingErrorsView = view.findViewById<LinearLayout>(R.id.thinkingErrors)
                thinkingErrorsView
                    .findViewById<LinearLayout>(R.id.content)
                    .apply {
                        visibility = View.VISIBLE
                    }

                val option_add_minimized1 = thinkingErrorsView
                    .findViewById<ConstraintLayout>(R.id.option_add_minimized)

                option_add_minimized1
                    .findViewById<FrameLayout>(R.id.frameLayout)
                    .apply {
                        visibility = View.GONE
                    }

                option_add_minimized1
                    .findViewById<TextView>(R.id.text)
                    .apply {
                        text = "Статистика ошибок мышления"
                    }

                val thinkingErrorsRecyclerView = thinkingErrorsView.findViewById<LinearLayout>(R.id.content)
                    .findViewById<RecyclerView>(R.id.recyclerView)
                thinkingErrorsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                thinkingErrorsRecyclerView.adapter = StatisticsAdapter(thinkingErrorsCount, R.layout.el2_1)
            }
    }

    private fun getEmotionalReactionsCount(kptRecords: List<KptRecord>): List<String> {
        val emotionalReactions = kptRecords
            .flatMap{
                it.emotionalReactions ?: emptyList()
            }

        val result = EmotionalReactionsConstants.Value
            .map { text ->
//                text to emotionalReactions.count { it == text }
                "$text (${emotionalReactions.count { it == text }})"
            }

        return result
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.dispose()
    }

    private fun getThinkingErrorsCount(kptRecords: List<KptRecord>): List<String> {
        val emotionalReactions = kptRecords
            .flatMap{
                it.thinkingErrors ?: emptyList()
            }

        val result = ThinkingError.values()
            .map { thinkingError ->
//                thinkingError.toRusString() to emotionalReactions.count { it == thinkingError }
                "${thinkingError.toRusString()} (${emotionalReactions.count { it == thinkingError }})"
            }

        return result
    }
}