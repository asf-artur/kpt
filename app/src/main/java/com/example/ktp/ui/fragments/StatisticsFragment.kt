package com.example.ktp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ktp.R
import com.example.ktp.constants.EmotionalReactionsConstants
import com.example.ktp.databinding.FragmentStatisticsBinding
import com.example.ktp.databinding.OptionAddWithWordsBinding
import com.example.ktp.model.KptRecord
import com.example.ktp.model.ThinkingError
import com.example.ktp.model.toRusString
import com.example.ktp.ui.BaseFragmentWithBinding
import com.example.ktp.ui.adapters.StatisticsAdapter
import com.example.ktp.viewmodels.StatisticsFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable

class StatisticsFragment : BaseFragmentWithBinding<FragmentStatisticsBinding>(R.layout.fragment_statistics) {
    val statisticsFragmentViewModel: StatisticsFragmentViewModel by viewModels()
    lateinit var disposable: Disposable

    override var inflateFunc: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> FragmentStatisticsBinding
        = FragmentStatisticsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kptRecordsObservable = statisticsFragmentViewModel.data
        disposable = kptRecordsObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { kptRecords ->
                val emotionalReactionsCount = getEmotionalReactionsCount(kptRecords)
                val thinkingErrorsCount = getThinkingErrorsCount(kptRecords)

                setWords(
                    binding.emotionalReactions,
                    "Статистика эмоциональных реакций",
                    emotionalReactionsCount,
                    GridLayoutManager(requireContext(), 2),
                    R.layout.el2
                )
                setWords(
                    binding.thinkingErrors,
                    "Статистика ошибок мышления",
                    thinkingErrorsCount,
                    LinearLayoutManager(requireContext()),
                    R.layout.el2_1
                )
            }
    }

    private fun setWords(
        parentBinding: OptionAddWithWordsBinding,
        name: String,
        wordsCount: List<String>,
        layoutManager: RecyclerView.LayoutManager,
        itemLayoutId: Int,
    ){
        parentBinding.content.visibility = View.VISIBLE
        parentBinding.optionAddMinimized.frameLayout.visibility = View.GONE
        parentBinding.optionAddMinimized.text.text = name

        val recyclerView = parentBinding.recyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = StatisticsAdapter(wordsCount, itemLayoutId)
    }

    private fun getEmotionalReactionsCount(kptRecords: List<KptRecord>): List<String> {
        val emotionalReactions = kptRecords
            .flatMap{
                it.emotionalReactions ?: emptyList()
            }

        val result = EmotionalReactionsConstants.Value
            .map { text ->
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
                "${thinkingError.toRusString()} (${emotionalReactions.count { it == thinkingError }})"
            }

        return result
    }
}