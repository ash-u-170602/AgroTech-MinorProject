package com.example.krishicare.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.krishicare.databinding.BaseFragmentBinding
import com.example.krishicare.ui.activites.MainActivity

open class BaseFragments :Fragment() {
    private val binding by lazy { BaseFragmentBinding.inflate(layoutInflater)}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    fun showToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    fun navigationVisibility(isVisible :Boolean){

        (activity as MainActivity).navigationVisibility(isVisible)

    }
}