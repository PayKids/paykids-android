package com.paykids.presentation.view.signIn

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentSignProviderBinding
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInProviderFragment : BaseFragment<FragmentSignProviderBinding>() {
    private val signViewModel: SignViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initView() {
        hideStatusBar()
        setStatusBarColorLight()
    }

    override fun initListener() {
        super.initListener()

        binding.btnKakao.setOnClickListener {
            signViewModel.signInWithKakao(requireActivity())
        }
    }

    override fun setObserver() {
        super.setObserver()

        signViewModel.kakaoLoginState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("카카오 로그인 성공: ${it.data}")
                    signViewModel.signIn(it.data.idToken)
                }
            }
        }

        signViewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    LoggerUtils.d("페이키즈 로그인 성공: ${it.data}")
                    signViewModel.saveSignInInfo(it.data)
                }
            }
        }

        signViewModel.saveState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.e("로그인 정보 저장 실패: ${it.message}")
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    if (it.data) {
                        navigateToHome()
                    } else {
                        navigateToNicknameSetting()
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

    private fun navigateToHome() {
        (activity as? SignActivity)?.moveHome() ?: run {
            showToast("화면 이동 중 오류가 발생했습니다")
        }
    }
}