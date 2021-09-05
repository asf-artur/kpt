package com.example.ktp.ui.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.ktp.App
import com.example.ktp.R
import com.example.ktp.constants.EmotionalReactionsConstants
import com.example.ktp.databinding.*
import com.example.ktp.model.KptRecord
import com.example.ktp.model.ThinkingError
import com.example.ktp.model.repository.StringBoolean
import com.example.ktp.model.toRusString
import com.example.ktp.ui.BaseFragmentWithBinding
import com.example.ktp.ui.IFragmentTransactions
import com.example.ktp.utils.UiKptRecordHelper
import com.example.ktp.viewmodels.FullInfoKptRecordFragmentViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

private const val KptRecordId = "KptRecordId"
class FullInfoKptRecordFragment : BaseFragmentWithBinding<FragmentFullInfoKptRecordBinding>(R.layout.fragment_full_info_kpt_record) {
    @Inject lateinit var uiKptRecordHelper: UiKptRecordHelper
    lateinit var dialogBinding: Dialog1Binding

    val emotionalReactionsMap = EmotionalReactionsConstants.Value
        .map {
            StringBoolean(it, false)
        }
    val thinkingErrorsMap = ThinkingError.values().map {
        StringBoolean(it.toRusString(), false)
    }

    override var inflateFunc: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> FragmentFullInfoKptRecordBinding
        = FragmentFullInfoKptRecordBinding::inflate

    val fullInfoKptRecordFragmentViewModel: FullInfoKptRecordFragmentViewModel by viewModels()
    lateinit var fragmentTransactions: IFragmentTransactions
    lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.daggerAppComponent.inject(this)
        fragmentTransactions = activity as IFragmentTransactions
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialogBinding = Dialog1Binding.inflate(inflater, container, false)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kptRecordId = arguments?.getInt(KptRecordId)
        kptRecordId?.let { id ->
            val kptRecordObservable = fullInfoKptRecordFragmentViewModel.getItem(id)
            disposable = kptRecordObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ kptRecord ->

                    val topAppBar = binding.topAppBar.materialToolbar
                    topAppBar.inflateMenu(R.menu.full_info_app_bar)
                    topAppBar.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.item1 -> {
                                deleteDialog(kptRecord)
//                                fullInfoKptRecordFragmentViewModel.deleteItem(kptRecord)
//                                fragmentTransactions.goToFragment(MainFragment())
                                true
                            }
                            else -> false
                        }
                    }

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
                        fragmentTransactions::goToFragment,
                        kptRecord
                    )

                    val textsForSet = listOf(
                        kptRecord.situation to binding.situation,
                        kptRecord.automaticThought to binding.automaticThought,
                        kptRecord.bodilyReactions to binding.bodilyReactions,
                        kptRecord.behavior to binding.behavior
                    )
                    textsForSet.forEach {
                        it.first?.let { text ->
                            setText(it.second, text)
                        }
                    }

                    kptRecord.truthOfThought?.let {
                        setNumbers(binding.truthOfThought, it)
                        binding.truthOfThought.seekbar.progress = it.toInt()
                    }
                    kptRecord.emotionalReactions?.let {
                        setWords(binding.emotionalReactions, it, emotionalReactionsMap)
                    }
                    kptRecord.thinkingErrors?.let {
                        val stringList = it
                            .map{
                                it.toRusString()
                            }
                        setWords(binding.thinkingErrors, stringList, thinkingErrorsMap)
                    }
                },
                    { Log.d("MYTAG", "error")},
                    {})
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.dispose()
    }

    private fun deleteDialog(kptRecord: KptRecord){
        val dialogView = dialogBinding.root

        val al = AlertDialog.Builder(requireContext())
            .setTitle("Удалить?")
            .setMessage("Удалить??????")
            .setView(dialogView)
            .setCancelable(true)
            .setOnCancelListener {
                ((dialogView.parent) as ViewGroup).removeAllViews()
            }
            .show()

        dialogBinding.no.setOnClickListener {
            al.cancel()
        }
        dialogBinding.yes.setOnClickListener {
            fullInfoKptRecordFragmentViewModel.deleteItem(kptRecord)
            al.cancel()
            Snackbar.make(requireView(), "Удалено", Snackbar.LENGTH_SHORT).show()
            fragmentTransactions.goToFragment(MainFragment())
        }
    }

    private fun setText(parentBinding: OptionAddWithTextBinding, text: String){
        parentBinding.content.visibility = View.VISIBLE
        parentBinding.editText.setText(text)
    }

    private fun setNumbers(parentBinding: OptionAddWithNumbersBinding, number: Double) {
        parentBinding.content.visibility = View.VISIBLE
        parentBinding.numbers.text = number.toString()
    }

    private fun setWords(parentBinding: OptionAddWithWordsBinding, selectedItems: List<String>, initialList: List<StringBoolean>){
        parentBinding.content.visibility = View.VISIBLE
        for(item in initialList){
            if(selectedItems.contains(item.text)){
                item.checked = true
            }
        }
    }

    companion object{
        fun init(kptRecordId: Int): FullInfoKptRecordFragment {
            val fullInfoKptRecordFragment = FullInfoKptRecordFragment()
                .apply {
                    arguments = bundleOf(KptRecordId to kptRecordId)
                }

            return fullInfoKptRecordFragment
        }
    }
}