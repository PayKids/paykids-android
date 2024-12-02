package com.paykids.presentation.view.signIn

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

    override fun initView() {
    }

    override fun initListener() {
        super.initListener()

        binding.btnKakao.setOnClickListener {
            signViewModel.signInWithKakao()
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
                    if (!signViewModel.isRegister) {
                        val ft = parentFragmentManager.beginTransaction()
                        ft.replace(R.id.fl_sign, SignNicknameFragment())
                        ft.addToBackStack(null)
                        ft.commit()
                    } else {
                        (activity as? SignActivity)?.moveHome()
                    }
                }
            }
        }
    }
}