package com.example.ktp.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ktp.R
import com.example.ktp.constants.ThinkingErrorsConstants
import com.example.ktp.databinding.OptionAddMinimizedBinding
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

/**
 * Класс, который помогает отобразить названия для полей и элементов при редактировании или создании записи КПТ.
 * Нужен из-за того, что одиноаковые операции происходят для 2 фрагментов
 */
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

        setupViewWithRecyclerView(context, emotionalReactionsBinding.recyclerView, emotionalReactionsMap)
        setupViewWithRecyclerView(context, thinkingErrorsBinding.recyclerView, thinkingErrorsMap)


        for (i in optionViewList.indices){
            setupKptOptionView(optionViewList[i], namesList[i], context)
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

    fun setMinimizeButtonDrawable(visible: Boolean, imageView: ImageView, context: Context){
        when(visible){
            true -> imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_horizontal_rule))
            false -> imageView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_plus_white))
        }
    }

    private fun setupViewWithRecyclerView(context: Context, recyclerView: RecyclerView, items: List<StringBoolean>){
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = AddKptThoughtAdapter(items)
    }

    private fun setupKptOptionView(parentView: View, name: String, context: Context){
        val option_add_minimized = parentView.findViewById<ConstraintLayout>(R.id.option_add_minimized)
        option_add_minimized.findViewById<TextView>(R.id.text)
            .apply {
                text = name
            }

        val content = parentView.findViewById<LinearLayout>(R.id.content)

        val imageView = option_add_minimized
            .findViewById<ImageView>(R.id.image_view)

        option_add_minimized
            .findViewById<FrameLayout>(R.id.frameLayout)
            .findViewById<Button>(R.id.button)
            .apply {
                setOnClickListener {
                    onMinimizeButtonClick(content, imageView, context)
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

            var message = "Добавлено"
            if(oldKptRecord != null){
                message = "Сохранено"
                kptRecord.id = oldKptRecord.id
                kptRecordRepository.update(kptRecord)
            }
            else{
                kptRecordRepository.add(kptRecord)
            }

            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

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

    private fun onMinimizeButtonClick(content: LinearLayout, imageView: ImageView, context: Context){
        content.visibility = if(content.visibility == View.GONE) View.VISIBLE else View.GONE
//        setMinimizeButtonDrawable(visible = content.visibility == View.VISIBLE, imageView, context)
    }
}