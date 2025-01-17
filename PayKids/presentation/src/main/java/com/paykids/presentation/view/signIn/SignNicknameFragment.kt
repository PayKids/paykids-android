package com.paykids.presentation.view.signIn

import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentSignNicknameBinding
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignNicknameFragment : BaseFragment<FragmentSignNicknameBinding>() {
    private val viewModel: SignViewModel by viewModels()

    override fun initView() {
        binding.etNick.filters = arrayOf(InputFilter.LengthFilter(8))
    }

    override fun initListener() {
        super.initListener()

        binding.etNick.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val filtered = s.toString().replace(Regex("[^a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣]"), "")
                if (s.toString() != filtered) {
                    binding.etNick.setText(filtered)
                    binding.etNick.setSelection(filtered.length) // 커서를 마지막으로 이동
                }

                val length = filtered.length

                binding.ibClear.apply {
                    visibility = if (length != 0) View.VISIBLE else View.GONE
                    setOnClickListener { binding.etNick.text.clear() }
                }

                binding.tvComment.apply {
                    if (length != 0) {
                        text = getString(R.string.text_validate_nickname)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.blue2))
                    } else {
                        text = getString(R.string.text_nickname_condition)
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }

                binding.btnDone.apply {
                    isEnabled = length > 0

                    if (length != 0) {
                        setBackgroundDrawable(
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.shape_radius_60
                            )
                        )
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        setOnClickListener {
                            viewModel.saveNickname(binding.etNick.text.toString())
                        }
                    } else {
                        setBackgroundDrawable(
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.shape_radius_60_with_stroke
                            )
                        )
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.gray2))
                    }
                }
            }
        })
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.nickState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                    LoggerUtils.e("로그인 정보 저장 실패: ${it.message}")
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    (activity as? SignActivity)?.moveHome() ?: run {
                        showToast("화면 이동 중 오류가 발생했습니다")
                    }
                }
            }

        }
    }
}