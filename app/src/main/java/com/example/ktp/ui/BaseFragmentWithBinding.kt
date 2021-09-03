package com.example.ktp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragmentWithBinding<T : ViewBinding>(layoutId: Int) : Fragment(layoutId) {
    private var _binding: T? = null
    val binding: T
    get() = _binding!!

    abstract var inflateFunc: (inflater : LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateFunc(inflater, container, false)
        val view = _binding!!.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

abstract class IAbstract : ViewBinding{
    abstract fun inflate(inflater : LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): ViewBinding
}
