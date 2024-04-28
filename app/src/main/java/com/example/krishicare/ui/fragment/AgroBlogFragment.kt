package com.example.krishicare.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.krishicare.databinding.AgroBlogFragmentBinding

class AgroBlogFragment : BaseFragments() {
    private val binding by lazy { AgroBlogFragmentBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationVisibility(false)
        binding.wvMain.settings.javaScriptEnabled
        binding.wvMain.loadUrl("https://agriculusedu.netlify.app/")
        binding.wvMain.webViewClient = WebViewClient()
    }

}