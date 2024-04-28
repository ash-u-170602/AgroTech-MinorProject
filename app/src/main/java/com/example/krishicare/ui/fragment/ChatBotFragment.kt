package com.example.krishicare.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.krishicare.databinding.ChatBotFragmentBinding

class ChatBotFragment : BaseFragments() {
    private val binding by lazy { ChatBotFragmentBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wvMain.settings.javaScriptEnabled
        binding.wvMain.loadUrl("https://mediafiles.botpress.cloud/5c7134b1-85cf-4ab7-b7f2-0624975c35c1/webchat/bot.html")
        binding.wvMain.webViewClient = WebViewClient()
    }
}