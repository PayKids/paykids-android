package com.paykids.presentation.view.home

import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentHomeBinding
import com.paykids.presentation.view.quiz.QuizEntryFragment
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.createBalloon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private var isSelected = false

    override fun initView() {
        createStages()
    }

    override fun initListener() {
        super.initListener()
//        setupStageClickListener(binding.ivStage)
//        setupStageClickListener(binding.ivStage2)
    }

    override fun setObserver() {
        super.setObserver()
    }

    private fun createTooltip(): Balloon {
        return createBalloon(context = requireContext()) {
            setLayout(R.layout.tooltip_custom)
            setArrowSize(0)
            setBackgroundColorResource(android.R.color.transparent)
            setBalloonAnimation(BalloonAnimation.FADE)
            setBalloonHighlightAnimation(BalloonHighlightAnimation.SHAKE)
            setLifecycleOwner(viewLifecycleOwner)
//            setAutoDismissDuration(1500L)

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
                createTooltip().showAlignBottom(view, 0, -50)
            }
        }
    }

    private fun createStages() {
        val stageOffsets = listOf(62, 35, 90, 154, 206, 233) // 각 Stage의 수평 오프셋
        val imageViewSize = 90.dp // ImageView 크기
        val verticalSpacing = 60.dp // Stage 간의 세로 간격
        val initialTopMargin = 163.dp // 첫 번째 스테이지의 top 마진
        val totalStages = 26 // 총 스테이지 수

        var previousViewId: Int? = null // 이전 View의 ID 저장

        for (i in 0 until totalStages) {
            val frameLayout = createStageFrame(imageViewSize) // FrameLayout 생성
            val horizontalOffset = calculateHorizontalOffset(i, stageOffsets) // 수평 오프셋 계산

            addStageToLayout(
                frameLayout,
                previousViewId,
                horizontalOffset,
                initialTopMargin,
                verticalSpacing,
                i
            )

            previousViewId = frameLayout.id // 이전 View ID 업데이트
        }
    }

    private fun createStageFrame(imageViewSize: Int): FrameLayout {
        val frameLayout = FrameLayout(requireContext()).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(imageViewSize, imageViewSize)
        }

        val borderView = createBorderView(imageViewSize)
        val imageView = createImageView()

        frameLayout.addView(borderView)
        frameLayout.addView(imageView)

        setupStageClickListener(frameLayout) // 클릭 리스너 추가
        return frameLayout
    }

    private fun createBorderView(size: Int): View {
        return View(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(size, size)
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(ContextCompat.getColor(requireContext(), R.color.white))
                setStroke(6.dp, ContextCompat.getColor(requireContext(), R.color.gray2))
            }
        }
    }

    private fun createImageView(): ImageView {
        return ImageView(requireContext()).apply {
            id = View.generateViewId()
            layoutParams = FrameLayout.LayoutParams(55.dp, 55.dp).apply {
                gravity = Gravity.CENTER
            }
            setImageResource(R.drawable.ic_home_pig_lock)
        }
    }

    private fun calculateHorizontalOffset(index: Int, stageOffsets: List<Int>): Int {
        val stageGroup = index / 5
        val stageIndexInGroup = index % 5

        return when {
            stageGroup == 0 -> stageOffsets[stageIndexInGroup] // 첫 번째 그룹
            stageGroup % 2 == 1 -> stageOffsets[5 - stageIndexInGroup] // 대칭 그룹
            else -> stageOffsets[stageIndexInGroup + 1] // 짝수 그룹
        }.dp
    }

    private fun addStageToLayout(
        frameLayout: FrameLayout,
        previousViewId: Int?,
        horizontalOffset: Int,
        initialTopMargin: Int,
        verticalSpacing: Int,
        index: Int
    ) {
        val constraintLayout = binding.clContent
        constraintLayout.addView(frameLayout)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        if (index == 0) {
            // 첫 번째 Stage는 부모에 고정
            constraintSet.connect(
                frameLayout.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                initialTopMargin
            )
            constraintSet.connect(
                frameLayout.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                horizontalOffset
            )
        } else {
            // 나머지 Stage는 이전 Stage와 연결
            constraintSet.connect(
                frameLayout.id,
                ConstraintSet.TOP,
                previousViewId ?: ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                verticalSpacing
            )
            constraintSet.connect(
                frameLayout.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
                horizontalOffset
            )
        }

        constraintSet.applyTo(constraintLayout)
    }

    val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()
}