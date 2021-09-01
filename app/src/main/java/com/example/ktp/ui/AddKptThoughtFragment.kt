package com.example.ktp.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ktp.App
import com.example.ktp.R
import com.example.ktp.constants.EmotionalReactionsConstants
import com.example.ktp.constants.ThinkingErrorsConstants
import com.example.ktp.model.KptRecord
import com.example.ktp.model.ThinkingError
import com.example.ktp.model.repository.KptRecordRepository
import com.example.ktp.model.repository.StringBoolean
import com.example.ktp.model.toRusString
import com.example.ktp.ui.adapters.AddKptThoughtAdapter
import com.google.android.material.snackbar.Snackbar
import java.util.*
import javax.inject.Inject

class AddKptThoughtFragment : Fragment(R.layout.fragment_add_kpt_thought) {
    @Inject lateinit var kptRecordRepository: KptRecordRepository
    lateinit var fragmentTransactions: IFragmentTransactions

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

        createAllKptOptions(view)
    }

    private fun createAllKptOptions(view: View){
        val optionViewList = mutableListOf<LinearLayout>()
        val namesList = listOf(
                resources.getString(R.string.situation),
                resources.getString(R.string.automaticThought),
                resources.getString(R.string.truthOfThought),
                resources.getString(R.string.emotionalReactions),
                resources.getString(R.string.bodilyReactions),
                resources.getString(R.string.behavior),
                resources.getString(R.string.thinkingErrors),
        )

        val situationView = view.findViewById<LinearLayout>(R.id.situation)
        situationView.findViewById<LinearLayout>(R.id.content)
                .apply {
                    visibility = View.VISIBLE
                }
        optionViewList.add(situationView)

        val automaticThought = view.findViewById<LinearLayout>(R.id.automaticThought)
        optionViewList.add(automaticThought)

        val truthOfThought = view.findViewById<LinearLayout>(R.id.truthOfThought)
        optionViewList.add(truthOfThought)
        val numbers = truthOfThought
                .findViewById<LinearLayout>(R.id.content)
                .findViewById<TextView>(R.id.numbers)
        truthOfThought
                .findViewById<LinearLayout>(R.id.content)
                .findViewById<SeekBar>(R.id.seekbar)
                .apply {
                    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            numbers.text = "$progress%"
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        }
                    })
                }

        val emotionalReactions = view.findViewById<LinearLayout>(R.id.emotionalReactions)
        optionViewList.add(emotionalReactions)
        setupViewWithRecyclerView(emotionalReactions, emotionalReactionsMap, this::onClickRecyclerViewItemEmotionalReactions)

        val bodilyReactions = view.findViewById<LinearLayout>(R.id.bodilyReactions)
        optionViewList.add(bodilyReactions)

        val behavior = view.findViewById<LinearLayout>(R.id.behavior)
        optionViewList.add(behavior)

        val thinkingErrors = view.findViewById<LinearLayout>(R.id.thinkingErrors)
        optionViewList.add(thinkingErrors)
        setupViewWithRecyclerView(thinkingErrors, thinkingErrorsMap, this::onClickRecyclerViewItemThinkingErrors)

        for (i in optionViewList.indices){
            setupKptOptionView(optionViewList[i], namesList[i])
        }

        val finishButton = view
                .findViewById<LinearLayout>(R.id.mainLinearLayout)
                .findViewById<FrameLayout>(R.id.mainFrameLayout)
                .findViewById<Button>(R.id.finish)
        finishButton.setOnClickListener {
            onClickFinish(situationView, automaticThought, truthOfThought, emotionalReactions, bodilyReactions, behavior, thinkingErrors)
        }
    }

    private fun onClickFinish(
            situationView: LinearLayout,
            automaticThoughtView: LinearLayout,
            truthOfThoughtView: LinearLayout,
            emotionalReactionsView: LinearLayout,
            bodilyReactionsView: LinearLayout,
            behaviorView: LinearLayout,
            thinkingErrorsView: LinearLayout,
    ){
        val content = situationView.findViewById<LinearLayout>(R.id.content)

        val editText = content.findViewById<EditText>(R.id.edit_text)
        if(editText.text.toString().isNotEmpty()){
            Snackbar.make(requireView(), "Добавлено", Snackbar.LENGTH_SHORT).show()

            val situation = getText(situationView)!!
            val automaticThought = getText(automaticThoughtView)
            val emotionalReactions = getStringList(emotionalReactionsMap)
            val bodilyReactions = getText(bodilyReactionsView)
            val behavior = getText(behaviorView)
            val truthOfThought = getDouble(truthOfThoughtView)
            val thinkingErrors = getThinkingErrorList(thinkingErrorsMap)

            val kptRecord = KptRecord(
                    situation,
                    automaticThought,
                    emotionalReactions,
                    bodilyReactions,
                    behavior,
                    truthOfThought,
                    thinkingErrors,
                    Calendar.getInstance(),
                    Calendar.getInstance()
            )

            kptRecordRepository.add(kptRecord)
            fragmentTransactions.goToFragment(MainFragment())
        }
        else{
            Snackbar.make(requireView(), "Не заполнено поле - Ситуация", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getText(parentView: View): String? {
        val content = parentView.findViewById<LinearLayout>(R.id.content)
        val editText = content.findViewById<EditText>(R.id.edit_text)
        val result = editText.text.toString()

        return if(result.isEmpty()) null else result
     }

    private fun getDouble(parentView: View): Double? {
        val content = parentView.findViewById<LinearLayout>(R.id.content)
        val numbers = content.findViewById<TextView>(R.id.numbers)
        val double = numbers.text.removeSuffix("%").toString()

        return double.toDoubleOrNull()
    }

    private fun getStringList(itemMap: List<StringBoolean>): List<String>? {
        val result = itemMap
            .filter {
                it.checked
            }
            .map {
                it.text
            }
        return if(result.isEmpty()) null else result
    }

    private fun getThinkingErrorList(itemMap: List<StringBoolean>): List<ThinkingError>?{
        val result = itemMap
            .filter {
                it.checked
            }
            .map { stringBoolean ->
                ThinkingErrorsConstants.Value.first{ it.second == stringBoolean.text }.first
            }
        return if(result.isEmpty()) null else result
    }

    private fun onClickRecyclerViewItemEmotionalReactions(item: String, selected: Boolean){
        emotionalReactionsMap.first {
            it.text == item
        }.checked = selected
    }

    private fun onClickRecyclerViewItemThinkingErrors(item: String, selected: Boolean){
        thinkingErrorsMap.first {
            it.text == item
        }.checked = selected
    }

    private fun setupViewWithRecyclerView(parentView: View, items: List<StringBoolean>, onItemClick: (item: String, selected: Boolean) -> Unit){
        val recyclerView = parentView.findViewById<LinearLayout>(R.id.content)
                .findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = AddKptThoughtAdapter(items, onItemClick)
    }

    private fun setupKptOptionView(parentView: View, name: String){
        val option_add_minimized = parentView.findViewById<ConstraintLayout>(R.id.option_add_minimized)
        option_add_minimized.findViewById<TextView>(R.id.text)
                .apply {
                    text = name
                }

        val content = parentView.findViewById<LinearLayout>(R.id.content)

        option_add_minimized
                .findViewById<FrameLayout>(R.id.frameLayout)
                .findViewById<Button>(R.id.button)
                .apply {
                    setOnClickListener {
                        onMinimizeButtonClick(content)
                    }
                }
    }

    private fun onMinimizeButtonClick(content: LinearLayout){
        content.visibility = if(content.visibility == View.GONE) View.VISIBLE else View.GONE
    }
}