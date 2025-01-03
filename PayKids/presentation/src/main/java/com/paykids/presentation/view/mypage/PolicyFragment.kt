package com.paykids.presentation.view.mypage

import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentPolicyBinding
import com.paykids.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PolicyFragment : BaseFragment<FragmentPolicyBinding>() {
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

        }

        binding.ivLicense.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(false)
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as? HomeActivity)?.setBottomNavigationVisibility(true)
    }
}