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
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DiaryDialog : DialogFragment() {

    private var _binding: DialogDiaryBinding? = null
    private val binding get() = _binding!!
    private var confirmDialogInterface: ConfirmDialogInterface? = null
    private var isConsumeSelected = true
    private var currentDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDiaryBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val place = arguments?.getString("place")
        val amount = arguments?.getString("amount")
        val memo = arguments?.getString("memo")

        binding.etAddAmount.setText(amount)
        binding.etMemo.setText(memo)

        val spinner = binding.spinnerCategory
        val items = resources.getStringArray(R.array.category_array)
        val adapter = CustomSpinnerAdapter(requireContext(), items)
        spinner.adapter = adapter
        place?.let {
            val index = items.indexOf(it)
            if (index >= 0) {
                spinner.setSelection(index)
            }
        }

        setupListeners()

        binding.clSwitch.setOnClickListener {
            toggleSwitch()
        }

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

    private fun toggleSwitch() {
        isConsumeSelected = !isConsumeSelected

        if (isConsumeSelected) {
            // 소비가 선택된 경우
            binding.tvConsume.setBackgroundResource(R.drawable.switch_bg_select)
            binding.tvConsume.setTextColor(requireContext().getColor(R.color.black))

            binding.tvIncome.setBackgroundResource(R.color.transparent)
            binding.tvIncome.setTextColor(requireContext().getColor(R.color.gray7))
        } else {
            // 수입이 선택된 경우
            binding.tvIncome.setBackgroundResource(R.drawable.switch_bg_select)
            binding.tvIncome.setTextColor(requireContext().getColor(R.color.black))

            binding.tvConsume.setBackgroundResource(R.color.transparent)
            binding.tvConsume.setTextColor(requireContext().getColor(R.color.gray7))
        }
    }

    private fun updateDateDisplay() {
        val year = "${currentDate.year}년"
        val month = "${currentDate.monthValue}월"
        val day = "${currentDate.dayOfMonth}일"

        binding.tvYear.text = year
        binding.tvMonth.text = month
        binding.tvDay.text = day
    }

    private fun setupListeners() {
        binding.ivYearUp.setOnClickListener {
            currentDate = currentDate.plusYears(1)
            updateDateDisplay()
        }
        binding.ivYearDown.setOnClickListener {
            currentDate = currentDate.minusYears(1)
            updateDateDisplay()
        }

        binding.ivMonthUp.setOnClickListener {
            currentDate = currentDate.plusMonths(1)
            updateDateDisplay()
        }
        binding.ivMonthDown.setOnClickListener {
            currentDate = currentDate.minusMonths(1)
            updateDateDisplay()
        }

        binding.ivDayUp.setOnClickListener {
            currentDate = currentDate.plusDays(1)
            updateDateDisplay()
        }
        binding.ivDayDown.setOnClickListener {
            currentDate = currentDate.minusDays(1)
            updateDateDisplay()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}