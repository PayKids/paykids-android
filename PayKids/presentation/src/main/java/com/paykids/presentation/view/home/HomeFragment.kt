package com.paykids.presentation.view.home

import androidx.fragment.app.viewModels
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentHomeBinding
import com.paykids.presentation.databinding.FragmentSignProviderBinding
import com.paykids.presentation.utils.LoggerUtils
import com.paykids.presentation.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val homeViewModel: HomeViewModel by viewModels()

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
    }
}