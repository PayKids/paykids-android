package com.paykids.presentation.view.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentPolicyBinding
import com.paykids.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyFragment : BaseFragment<FragmentPolicyBinding>() {
    private lateinit var backPressedCallback: OnBackPressedCallback

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.ivTermsOfUse.setOnClickListener {

        }

        binding.ivPolicy.setOnClickListener {
            val url = "https://paykids2025.blogspot.com/2025/01/blog-post.html"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        }

        binding.ivLicense.setOnClickListener {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(true)
    }

    override fun onDestroyView() {
        backPressedCallback.remove()
        super.onDestroyView()
    }
}