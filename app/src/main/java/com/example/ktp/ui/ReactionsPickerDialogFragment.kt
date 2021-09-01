package com.example.ktp.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.ktp.R

class ReactionsPickerDialogFragment : DialogFragment() {
    private lateinit var mainView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mainView = layoutInflater.inflate(R.layout.dialog_picker, null)

        return AlertDialog.Builder(requireContext())
                .setView(mainView)
                .setTitle("Диалоговое окно")
                .setMessage("Для закрытия окна нажмите ОК")
                .setNegativeButton("no", null)
                .setPositiveButton("ok", null)
                .create()
    }
}