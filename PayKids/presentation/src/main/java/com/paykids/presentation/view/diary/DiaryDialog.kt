package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.paykids.presentation.R
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.DialogDiaryBinding
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import java.time.LocalDate

class DiaryDialog : DialogFragment(), ConfirmDialogInterface {
    private var _binding: DialogDiaryBinding? = null
    private val binding get() = _binding!!
    private var isConsumeSelected = true
    private var currentDate: LocalDate = LocalDate.now()
    private var isEditMode = false
    private var onModifyDiaryListener: OnModifyDiaryListener? = null
    private lateinit var viewModel: DiaryViewModel
    private lateinit var categorySpinner: Spinner

    interface OnModifyDiaryListener {
        fun onModify(
            id: Int,
            date: String,
            allowanceType: String,
            category: String,
            amount: Int,
            memo: String
        )
    }

    fun setOnModifyDiaryListener(listener: OnModifyDiaryListener) {
        onModifyDiaryListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDiaryBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(requireActivity())[DiaryViewModel::class.java]
        categorySpinner = binding.spinnerCategory

        observeCategoryData()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var diaryId = 0
        arguments?.let {
            diaryId = it.getInt("id", 0)
            currentDate = it.getString("date", "2025-01-01")?.let { dateString ->
                LocalDate.parse(dateString)
            } ?: LocalDate.now()
            val amount = it.getInt("amount", 0)
            val memo = it.getString("memo", "")
            isEditMode = it.getBoolean("isEditMode", false)
            isConsumeSelected = it.getBoolean("isConsumeSelected", true)

            binding.etAmount.setText(amount.toString())
            binding.etMemo.setText(memo)

            if (isEditMode) {
                binding.tvTitle.text = "용돈 수정하기"
            } else {
                binding.tvTitle.text = "용돈 기입하기"
            }
        }

        setupListeners()
        updateDateDisplay()

        binding.clSwitch.setOnClickListener {
            toggleSwitch()
        }

        binding.btnSubmit.setOnClickListener {
            val amount = binding.etAmount.text.toString().toIntOrNull() ?: 0
            val memo = binding.etMemo.text.toString()
            val selectedCategory = categorySpinner.selectedItem.toString()

            if (isEditMode) {
                onModifyDiaryListener?.onModify(
                    diaryId,
                    currentDate.toString(),
                    "EXPENSE",
                    selectedCategory,
                    amount,
                    memo
                )
            }

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

    @SuppressLint("DefaultLocale")
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
        }
    }

    private fun observeCategoryData() {
        viewModel.getExpenseCategoryState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    categorySpinner.visibility = View.GONE
                }
                is UiState.Success -> {
                    categorySpinner.visibility = View.VISIBLE

                    val categories = uiState.data.map { it.category }
                    val adapter = CustomSpinnerAdapter(requireContext(), categories.toTypedArray())
                    categorySpinner.adapter = adapter
                }
                is UiState.Failure -> {
                    categorySpinner.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "카테고리 로드 실패: ${uiState.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onYesButtonClick() {
        LoggerUtils.d("Button Click")
    }

}