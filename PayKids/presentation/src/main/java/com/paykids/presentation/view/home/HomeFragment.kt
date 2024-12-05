package com.paykids.presentation.view.home

import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentHomeBinding
import com.paykids.presentation.view.quiz.QuizEntryFragment
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.createBalloon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var isSelected = false

    override fun initView() {
    }

    override fun initListener() {
        super.initListener()

        setupStageClickListener(binding.ivStage)
        setupStageClickListener(binding.ivStage2)
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
                navigateToQuizEntry()
            }

            build()
        }
    }

    private fun navigateToQuizEntry() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fl_home, QuizEntryFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun setupStageClickListener(view: View) {
        view.setOnClickListener {
            view.isSelected = !view.isSelected
            if (view.isSelected) {
                createTooltip().showAlignBottom(view)
            }
        }
    }
}