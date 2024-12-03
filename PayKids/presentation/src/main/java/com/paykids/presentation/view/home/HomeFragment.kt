package com.paykids.presentation.view.home

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentHomeBinding
import com.paykids.presentation.databinding.ItemTooltipBinding
import com.paykids.presentation.utils.UiState
import com.paykids.presentation.view.home.HomeActivity
import com.paykids.presentation.view.signIn.SignActivity
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var isSelected = false

    override fun initView() {

    }

    override fun initListener() {
        super.initListener()

        binding.ivStage.setOnClickListener {
            if (binding.ivStage.isSelected) {
                binding.ivStage.isSelected = false
            } else {
                binding.ivStage.isSelected = true
                createTooltip().showAlignBottom(binding.ivStage)
            }
        }

        binding.ivStage2.setOnClickListener {
            if (binding.ivStage2.isSelected) {
                binding.ivStage2.isSelected = false
            } else {
                binding.ivStage2.isSelected = true
                createTooltip().showAlignBottom(binding.ivStage2)
            }
        }
    }

    override fun setObserver() {
        super.setObserver()
    }

    private fun createTooltip(): Balloon {
        return createBalloon(context = requireContext()) {
            setHeight(72)
            setWidthRatio(0.8f)
            setText(getString(R.string.text_tooltip_start))
            setTextSize(14f)
            setTextColorResource(R.color.white)
            setTextTypeface(ResourcesCompat.getFont(requireContext(), R.font.nanumsquare_bold)!!)
            setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            setArrowSize(15)
            setArrowPosition(0.5f)  // 화살표 위치
            setPaddingHorizontal(20)
            setCornerRadius(5f)
            setBackgroundColorResource(R.color.black)
            setBalloonAnimation(BalloonAnimation.FADE)
            setBalloonHighlightAnimation(BalloonHighlightAnimation.SHAKE)
            setLifecycleOwner(viewLifecycleOwner)
            setAutoDismissDuration(1500L)

            setOnBalloonClickListener {
                Toast.makeText(context, "Balloon clicked!", Toast.LENGTH_SHORT).show()
            }

            build()
        }
    }

}