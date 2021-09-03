package com.example.ktp.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ktp.App
import com.example.ktp.R
import com.example.ktp.databinding.FragmentMainBinding
import com.example.ktp.ui.adapters.KptRecordsAdapter
import com.example.ktp.viewmodels.MainFragmentViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

class MainFragment : BaseFragmentWithBinding<FragmentMainBinding>(R.layout.fragment_main) {
    val mainFragmentViewModel : MainFragmentViewModel by viewModels()
    lateinit var fragmentTransactions: IFragmentTransactions
    lateinit var disposable: Disposable

    override var inflateFunc: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> FragmentMainBinding
            = FragmentMainBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.daggerAppComponent.inject(this)
        fragmentTransactions = activity as IFragmentTransactions
    }
    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = binding.topAppBar.materialToolbar
        topAppBar.inflateMenu(R.menu.main_app_bar)

        val floatingButton = binding.floatingButton
        floatingButton.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, AddKptThoughtFragment())
                .commit()
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        disposable = mainFragmentViewModel.data
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ kptRecords ->
//                    val workbook = excelService.createExcelWorkBook(it)
                    recyclerView.adapter = KptRecordsAdapter(kptRecords, this::expandAndMinimize, this::onItemClick)

                    topAppBar.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.item1 -> {
                                val wb = mainFragmentViewModel.createExcelWorkBook(kptRecords)
                                val file = writeWorkbookToFile(wb)
                                sendFileIntent(file)
                                true
                            }
                            else -> false
                        }
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable.dispose()
    }

    private fun onItemClick(kptRecordId: Int){
        val fullInfoKptRecordFragment = FullInfoKptRecordFragment.init(kptRecordId)

        parentFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, fullInfoKptRecordFragment)
                .commit()
    }

    fun writeWorkbookToFile(workbook: XSSFWorkbook): File {
        val path = requireContext().getExternalFilesDir(null)
//      val p = Environment.getExternalStorageDirectory()
        val file = File(path, "1.xlsx")
        workbook.write(file.outputStream())

        return file
    }

    fun sendFileIntent(file: File){
        Intent(Intent.ACTION_SEND).apply {
            type = "application/xlsx"
            val uri = FileProvider.getUriForFile(requireContext(), requireContext().applicationContext.packageName + ".provider", file)
            putExtra(Intent.EXTRA_STREAM, uri)
        }.also {
            startActivity(it)
        }
    }

    fun expandAndMinimize(textView: TextView, imageButton: ImageButton){
        if(textView.maxLines == 8){
            textView.maxLines = Int.MAX_VALUE
            animate(imageButton, 180f)
        }
        else{
            textView.maxLines = 8
            animate(imageButton, 360f)
        }
    }

    fun animate(imageButton: ImageButton, degree: Float){
        val hAnimate = ObjectAnimator
            .ofFloat(imageButton, "rotation", degree)
            .start()
    }
}