package com.paykids.presentation.view.mypage

import android.content.Intent
import androidx.fragment.app.viewModels
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentMypageBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.presentation.view.signIn.SignActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>() {
    private val homeViewModel: MyPageViewModel by viewModels()

    override fun initView() {
    }

    override fun initListener() {
        super.initListener()

        binding.btnLogout.setOnClickListener {
            homeViewModel.signOut()
        }
    }

    override fun setObserver() {
        super.setObserver()

        homeViewModel.signOutState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    (activity as HomeActivity).moveSign()
                }
            }
        }

        homeViewModel.withdrawState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("회원 탈퇴 실패")
                }

                is UiState.Success -> {
                    homeViewModel.clearData()
                }
            }
        }

        homeViewModel.clearState.observe(viewLifecycleOwner)
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