package com.paykids.presentation.view.mypage

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentMypageBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(), ConfirmDialogInterface {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun initView() {
        myPageViewModel.getUserInfo()
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
            val dialog =
                MyPageDialog(
                    this,
                    R.string.dialog_signout_title,
                    R.string.dialog_signout_message,
                    R.string.dialog_signout_confirm
                )
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "SignOutDialog")
        }
    }

    override fun setObserver() {
        super.setObserver()

        myPageViewModel.userInfoState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    Glide.with(this)
                        .load(it.data.profileImageURL)
                        .placeholder(R.drawable.img_default_profile)
                        .error(R.drawable.img_default_profile)
                        .transform(CircleCrop())
                        .into(binding.ivProfile)

                    binding.tvNickname.text = it.data.nickname
                }
            }
        }

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

    override fun onYesButtonClick() {
        myPageViewModel.signOut()
    }
}