package com.paykids.presentation.view.diary

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.paykids.presentation.R
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.DialogDiaryBinding

class DiaryDialog(confirmDialogInterface: ConfirmDialogInterface) : DialogFragment() {

    private var _binding: DialogDiaryBinding? = null
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
        _binding = DialogDiaryBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val spinner = binding.spinnerCategory
        val items = resources.getStringArray(R.array.category_array)
        val adapter = CustomSpinnerAdapter(requireContext(), items)
        spinner.adapter = adapter

        binding.btnSubmit.setOnClickListener {
            this.confirmDialogInterface?.onYesButtonClick()
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        // 다이얼로그 너비를 화면 너비의 90%로 설정
        dialog?.window?.setLayout(
            (requireContext().resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}