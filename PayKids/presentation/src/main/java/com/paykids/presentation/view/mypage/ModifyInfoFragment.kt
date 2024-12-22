package com.paykids.presentation.view.mypage

import android.content.Intent
import androidx.fragment.app.viewModels
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentModifyInfoBinding
import com.paykids.presentation.databinding.FragmentMypageBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.presentation.view.signIn.SignActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModifyInfoFragment : BaseFragment<FragmentModifyInfoBinding>() {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun initView() {
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.tvWithdraw.setOnClickListener {
            myPageViewModel.withdraw()
        }
    }

    override fun setObserver() {
        super.setObserver()

        myPageViewModel.withdrawState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("회원 탈퇴 실패")
                }

                is UiState.Success -> {
                    myPageViewModel.clearData()
                }
            }
        }

        myPageViewModel.clearState.observe(viewLifecycleOwner)
        {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("회원 정보 삭제 실패")
                }

                is UiState.Success -> {
                    requireActivity().apply {
                        startActivity(Intent(this, SignActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}