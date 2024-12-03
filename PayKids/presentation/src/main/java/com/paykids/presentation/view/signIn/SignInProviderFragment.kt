package com.paykids.presentation.view.signIn

import androidx.fragment.app.viewModels
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentSignProviderBinding
import com.paykids.presentation.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInProviderFragment : BaseFragment<FragmentSignProviderBinding>() {
    private val signViewModel: SignViewModel by viewModels()

    override fun initView() {
    }

    override fun initListener() {
        super.initListener()

        binding.btnKakao.setOnClickListener {
//            signViewModel.signInWithKakao()
            navigateToNicknameSetting()
        }
    }

    override fun setObserver() {
        super.setObserver()

        signViewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    when {
                        !signViewModel.isRegister -> {
                            navigateToNicknameSetting()
                        }

                        else -> {
                            (activity as? SignActivity)?.moveHome() ?: run {
                                showToast("화면 이동 중 오류가 발생했습니다")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToNicknameSetting() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_sign, SignNicknameFragment())
            .addToBackStack(null)
            .commit()
    }
}