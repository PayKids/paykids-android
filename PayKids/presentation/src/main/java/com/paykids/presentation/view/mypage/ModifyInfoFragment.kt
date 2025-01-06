package com.paykids.presentation.view.mypage

import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.FragmentModifyInfoBinding
import com.paykids.presentation.utils.ImageMapper
import com.paykids.presentation.utils.ImageMapper.toFile
import com.paykids.presentation.utils.ImageMapper.toMultipartBody
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.presentation.view.signIn.SignActivity
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModifyInfoFragment : BaseFragment<FragmentModifyInfoBinding>(), ConfirmDialogInterface {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun initView() {
        myPageViewModel.getUserInfo()
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.ivModifyImage.setOnClickListener {
            openGallery()
        }

        binding.btnConfirm.setOnClickListener {
            myPageViewModel.changeNickname(binding.etModifyNickname.text.toString())
        }

        binding.tvWithdraw.setOnClickListener {
            val dialog =
                MyPageDialog(
                    this,
                    R.string.dialog_withdraw_title,
                    R.string.dialog_withdraw_message,
                    R.string.dialog_withdraw_confirm
                )
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "WithdrawDialog")
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
                    binding.tvEmail.text = it.data.email
                }
            }
        }

        myPageViewModel.uploadImageState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                }

                is UiState.Failure -> {
                    showToast("이미지 업로드 실패: ${it.message}")
                }

                is UiState.Success -> {
                    showToast("프로필 이미지 업로드 성공")
                }
            }
        }

        myPageViewModel.nickChangeState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    showToast("닉네임 변경 실패")
                }

                is UiState.Success -> {
                    showToast("닉네임 변경 완료")
                    myPageViewModel.getUserInfo()
                }
            }
        }

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

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri ->
            LoggerUtils.d("Selected image URI: $imageUri")

            try {
                val file = imageUri.toFile(requireContext())

                val mimeType = requireContext().contentResolver.getType(imageUri) ?: "image/*"

                myPageViewModel.uploadProfileImage(file, mimeType)

            } catch (e: Exception) {
                LoggerUtils.e("Error converting Uri to File: ${e.message}")
                showToast("이미지 처리 중 오류가 발생했습니다")
            }
        } ?: LoggerUtils.e("Selected image URI is null")
    }

    private fun openGallery() {
        try {
            galleryLauncher.launch("image/*")
        } catch (e: Exception) {
            LoggerUtils.e("Error launching gallery: ${e.message}")
            showToast("갤러리를 열 수 없습니다")
        }
    }

    override fun onYesButtonClick() {
        myPageViewModel.withdraw()
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