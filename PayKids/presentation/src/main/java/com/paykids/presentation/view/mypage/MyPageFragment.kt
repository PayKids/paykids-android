package com.paykids.presentation.view.mypage

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentMypageBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.presentation.view.quiz.QuizEntryFragment
import com.paykids.presentation.view.signIn.SignActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>() {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun initView() {
    }

    override fun initListener() {
        super.initListener()
        val navController = findNavController()


        binding.ivModify.setOnClickListener {
            navController.navigate(R.id.modifyInfoFragment)
        }

        binding.ivPolicy.setOnClickListener {
            navController.navigate(R.id.policyFragment)
        }

        binding.btnLogout.setOnClickListener {
            myPageViewModel.signOut()
        }
    }

    override fun setObserver() {
        super.setObserver()

        myPageViewModel.signOutState.observe(viewLifecycleOwner) {
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
    }
}