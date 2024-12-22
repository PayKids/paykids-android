package com.paykids.presentation.custom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.paykids.presentation.databinding.DialogMypageBinding

class MyPageDialog(
    confirmDialogInterface: ConfirmDialogInterface,
    private var title: Int,
    private var message: Int,
    private var confirmMessage: Int,
) : DialogFragment() {

    private var _binding: DialogMypageBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: ConfirmDialogInterface? = null

    init {
        this.confirmDialogInterface = confirmDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMypageBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvTitle.text = getString(title)
        binding.tvMessage.text = getString(message)
        binding.btnYes.text = getString(confirmMessage)

        binding.btnNo.setOnClickListener {
            dismiss()
        }

        binding.btnYes.setOnClickListener {
            this.confirmDialogInterface?.onYesButtonClick()
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        // 다이얼로그 너비를 화면 너비의 80%로 설정
        dialog?.window?.setLayout(
            (requireContext().resources.displayMetrics.widthPixels * 0.8).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}