package com.example.ktp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ktp.App
import com.example.ktp.R
import com.example.ktp.constants.EmotionalReactionsConstants
import com.example.ktp.databinding.FragmentAddKptThoughtBinding
import com.example.ktp.model.ThinkingError
import com.example.ktp.model.repository.StringBoolean
import com.example.ktp.model.toRusString
import com.example.ktp.utils.UiKptRecordHelper
import javax.inject.Inject

class AddKptThoughtFragment : BaseFragmentWithBinding<FragmentAddKptThoughtBinding>(R.layout.fragment_add_kpt_thought) {
    @Inject lateinit var uiKptRecordHelper: UiKptRecordHelper
    lateinit var fragmentTransactions: IFragmentTransactions

    override var inflateFunc: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> FragmentAddKptThoughtBinding
        = FragmentAddKptThoughtBinding::inflate

    val emotionalReactionsMap = EmotionalReactionsConstants.Value
        .map {
            StringBoolean(it, false)
        }
    val thinkingErrorsMap = ThinkingError.values().map {
        StringBoolean(it.toRusString(), false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.daggerAppComponent.inject(this)
        fragmentTransactions = activity as IFragmentTransactions
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val namesList = listOf(
            resources.getString(R.string.situation),
            resources.getString(R.string.automaticThought),
            resources.getString(R.string.truthOfThought),
            resources.getString(R.string.emotionalReactions),
            resources.getString(R.string.bodilyReactions),
            resources.getString(R.string.behavior),
            resources.getString(R.string.thinkingErrors),
        )

        uiKptRecordHelper.createAllKptOptions(
            binding.situation,
            binding.automaticThought,
            binding.truthOfThought,
            binding.emotionalReactions,
            binding.bodilyReactions,
            binding.behavior,
            binding.thinkingErrors,
            binding.finish,
            namesList,
            emotionalReactionsMap,
            thinkingErrorsMap,
            requireContext(),
            view,
            fragmentTransactions::goToFragment
        )
    }
}