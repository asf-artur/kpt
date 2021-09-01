package com.example.ktp.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ktp.App
import com.example.ktp.R
import com.example.ktp.ui.adapters.KptRecordsAdapter
import com.example.ktp.viewmodels.MainFragmentViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

class MainFragment : Fragment(R.layout.fragment_main) {
    val mainFragmentViewModel : MainFragmentViewModel by viewModels()
    lateinit var fragmentTransactions: IFragmentTransactions
    lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.daggerAppComponent.inject(this)
        fragmentTransactions = activity as IFragmentTransactions
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topAppBar = view
            .findViewById<FrameLayout>(R.id.top_app_bar)
            .findViewById<AppBarLayout>(R.id.appBarLayout)
            .findViewById<MaterialToolbar>(R.id.materialToolbar)
        topAppBar.inflateMenu(R.menu.main_app_bar)

        val floatingButton = view.findViewById<FloatingActionButton>(R.id.floating_button)
        floatingButton.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, AddKptThoughtFragment())
                .commit()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
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