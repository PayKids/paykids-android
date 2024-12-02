package com.paykids.presentation.view.signIn

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentSignNicknameBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class SignNicknameFragment(
    private val isSignUp: Boolean = true
) : BaseFragment<FragmentSignNicknameBinding>() {
    private val viewModel: SignViewModel by viewModels()

    override fun initView() {
        binding.etNick.filters = arrayOf(InputFilter.LengthFilter(14))
    }

    override fun initListener() {
        super.initListener()

        binding.etNick.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val length = s.toString().length

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
                                R.drawable.shape_radius_50
                            )
                        )
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    } else {
                        setBackgroundDrawable(
                            AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.shape_radius_50_with_stroke
                            )
                        )
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.gray2))
                    }
                }
            }
        })
    }
}