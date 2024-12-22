package com.paykids.presentation.view.mypage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.custom.MyPageDialog
import com.paykids.presentation.databinding.FragmentModifyInfoBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.signIn.SignActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModifyInfoFragment : BaseFragment<FragmentModifyInfoBinding>(), ConfirmDialogInterface {
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun initView() {
    }

    override fun initListener() {
        super.initListener()

        binding.ibBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.ivModifyImage.setOnClickListener {
            openGallery()
        }

        binding.tvWithdraw.setOnClickListener {
            val dialog =
                MyPageDialog(
                    this,
                    R.string.dialog_withdraw_title,
                    R.string.dialog_withdraw_message
                )
            dialog.isCancelable = false
            dialog.show(parentFragmentManager, "WithdrawDialog")
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

    private val galleryLauncher =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let {
                    binding.ivProfile.setImageURI(it)
                }
            }
        }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    override fun onYesButtonClick() {
        myPageViewModel.withdraw()
    }
}