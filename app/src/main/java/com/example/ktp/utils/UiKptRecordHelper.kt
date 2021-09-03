package com.example.ktp.utils

import android.content.Context
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ktp.R
import com.example.ktp.constants.ThinkingErrorsConstants
import com.example.ktp.databinding.OptionAddWithNumbersBinding
import com.example.ktp.databinding.OptionAddWithTextBinding
import com.example.ktp.databinding.OptionAddWithWordsBinding
import com.example.ktp.model.KptRecord
import com.example.ktp.model.ThinkingError
import com.example.ktp.model.repository.KptRecordRepository
import com.example.ktp.model.repository.StringBoolean
import com.example.ktp.ui.fragments.MainFragment
import com.example.ktp.ui.adapters.AddKptThoughtAdapter
import com.google.android.material.snackbar.Snackbar
import java.util.*
import javax.inject.Inject

class UiKptRecordHelper @Inject constructor (
    private val kptRecordRepository: KptRecordRepository
    )
{

    fun createAllKptOptions(
        situationBinding: OptionAddWithTextBinding,
        automaticThoughtBinding: OptionAddWithTextBinding,
        truthOfThoughtBinding: OptionAddWithNumbersBinding,
        emotionalReactionsBinding: OptionAddWithWordsBinding,
        bodilyReactionsBinding: OptionAddWithTextBinding,
        behaviorBinding: OptionAddWithTextBinding,
        thinkingErrorsBinding: OptionAddWithWordsBinding,
        finishButton: Button,
        namesList: List<String>,
        emotionalReactionsMap: List<StringBoolean>,
        thinkingErrorsMap: List<StringBoolean>,
        context: Context,
        view: View,
        goToFragment: (fragment: Fragment) -> Unit,
        oldKptRecord: KptRecord? = null
    ){
        val optionViewList = mutableListOf<LinearLayout>()

        situationBinding.content
            .apply {
                visibility = View.VISIBLE
            }

        optionViewList.add(situationBinding.root)
        optionViewList.add(automaticThoughtBinding.root)
        optionViewList.add(truthOfThoughtBinding.root)
        optionViewList.add(emotionalReactionsBinding.root)
        optionViewList.add(bodilyReactionsBinding.root)
        optionViewList.add(behaviorBinding.root)
        optionViewList.add(thinkingErrorsBinding.root)

        val numbers = truthOfThoughtBinding.numbers
        truthOfThoughtBinding.seekbar
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

        setupViewWithRecyclerView(context, emotionalReactionsBinding.root, emotionalReactionsMap)
        setupViewWithRecyclerView(context, thinkingErrorsBinding.root, thinkingErrorsMap)


        for (i in optionViewList.indices){
            setupKptOptionView(optionViewList[i], namesList[i])
        }

        finishButton.setOnClickListener {
            onClickFinish(
                situationBinding,
                automaticThoughtBinding,
                truthOfThoughtBinding,
                bodilyReactionsBinding,
                behaviorBinding,
                view,
                emotionalReactionsMap,
                thinkingErrorsMap,
                goToFragment,
                oldKptRecord
            )
        }
    }

    private fun setupViewWithRecyclerView(context: Context, parentView: View, items: List<StringBoolean>){
        val recyclerView = parentView.findViewById<LinearLayout>(R.id.content)
            .findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = AddKptThoughtAdapter(items)
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

    private fun onClickFinish(
        situationView: OptionAddWithTextBinding,
        automaticThoughtView: OptionAddWithTextBinding,
        truthOfThoughtView: OptionAddWithNumbersBinding,
        bodilyReactionsView: OptionAddWithTextBinding,
        behaviorView: OptionAddWithTextBinding,
        view: View,
        emotionalReactionsMap: List<StringBoolean>,
        thinkingErrorsMap: List<StringBoolean>,
        goToFragment: (fragment: Fragment) -> Unit,
        oldKptRecord: KptRecord? = null
    ){
        val situationEditText = situationView.editText

        if(situationEditText.text.toString().isNotEmpty()){
            Snackbar.make(view, "Добавлено", Snackbar.LENGTH_SHORT).show()

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

            if(oldKptRecord != null){
                kptRecord.id = oldKptRecord.id
                kptRecordRepository.update(kptRecord)
            }
            else{
                kptRecordRepository.add(kptRecord)
            }

            goToFragment(MainFragment())
        }
        else{
            Snackbar.make(view, "Не заполнено поле - Ситуация", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun getText(parentBinding: OptionAddWithTextBinding): String? {
        val result = parentBinding.editText.text.toString()
        return if(result.isEmpty()) null else result
    }

    private fun getDouble(parentBinding: OptionAddWithNumbersBinding): Double? {
        val double = parentBinding.numbers.text.removeSuffix("%").toString()
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

    private fun onMinimizeButtonClick(content: LinearLayout){
        content.visibility = if(content.visibility == View.GONE) View.VISIBLE else View.GONE
    }
}