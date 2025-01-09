package com.paykids.presentation.view.home

import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentHomeBinding
import com.paykids.presentation.utils.UiState
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.createBalloon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    // 데이터 클래스 정의
    data class Stage(
        val number: Int, val imageResIdLock: Int, val imageResIdUnlock: Int
    )

    private val homeViewModel: HomeViewModel by viewModels()
    private var stages = mutableListOf<Stage>()
    private var stageCount: Int = 0
    private lateinit var stageName: String
    private var unlockedStageNumber: Int = 0


    override fun initView() {
        homeViewModel.getStageToGo()
    }

    override fun initListener() {
        super.initListener()
    }

    override fun setObserver() {
        super.setObserver()

        homeViewModel.stageNameState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    binding.tvStageTitle.text = it.data
                    stageName = it.data
                }
            }
        }

        homeViewModel.stageToGoState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    unlockedStageNumber = it.data
                    val stageName = homeViewModel.getStageName(unlockedStageNumber)
                    binding.tvStageTitle.text = stageName.toString()
                    binding.tvStageNumber.text = "스테이지 ${unlockedStageNumber}"

                    homeViewModel.getStageCount() // 해금된 스테이지 번호 불러오기
                }
            }
        }

        homeViewModel.stageCountState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    stageCount = it.data
                    stages = generateStages(stageCount).toMutableList() // stageCount를 이용해 stages 생성
                    createStages(stages) // stages를 기반으로 스테이지 생성
                }
            }
        }

    }

    private fun setupStageClickListener(view: View, stage: Stage) {
        view.setOnClickListener {
            if (stage.number > unlockedStageNumber) {
                showToast("해당 스테이지는 잠금 상태입니다.")
                //return@setOnClickListener
            }

            view.isSelected = !view.isSelected
            if (view.isSelected) {
                binding.tvStageNumber.text = "스테이지 ${stage.number}"
                homeViewModel.getStageName(stage.number)
                val tooltip = createTooltip()
                tooltip.setOnBalloonClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToQuizEntryFragment(
                        stage.number, stageName
                    )
                    findNavController().navigate(action)
                }
                tooltip.showAlignBottom(view, 0, -50)
            }
        }
    }

    private fun generateStages(stageCount: Int): List<Stage> {
        val lockImages = listOf(
            R.drawable.ic_home_pig_lock,
            R.drawable.ic_home_coin_lock,
            R.drawable.ic_home_card_lock,
            R.drawable.ic_home_acount_lock,
            R.drawable.ic_home_moneybag_lock
        )
        val unlockImages = listOf(
            R.drawable.ic_home_pig_unlock,
            R.drawable.ic_home_coin_unlock,
            R.drawable.ic_home_card_unlock,
            R.drawable.ic_home_acount_unlock,
            R.drawable.ic_home_moneybag_unlock
        )
        val stages = mutableListOf<Stage>()

        for (i in 1..stageCount) {
            val index = (i - 1) % 5
            stages.add(
                Stage(
                    number = i,
                    imageResIdLock = lockImages[index],
                    imageResIdUnlock = unlockImages[index]
                )
            )
        }
        return stages
    }

    private fun createStages(stages: List<Stage>) {
        val stageOffsets = listOf(62, 35, 90, 154, 206, 233) // 각 Stage의 수평 오프셋
        val imageViewSize = 90.dp // ImageView 크기
        val verticalSpacing = 60.dp // Stage 간의 세로 간격
        val initialTopMargin = 163.dp // 첫 번째 스테이지의 top 마진

        var previousViewId: Int? = null // 이전 View의 ID 저장

        // 데이터 리스트 기반으로 스테이지 생성
        stages.forEachIndexed { index, stage ->
            val isUnlocked = stage.number <= unlockedStageNumber
            val frameLayout = createStageFrame(imageViewSize, stage, isUnlocked) // FrameLayout 생성
            val horizontalOffset = calculateHorizontalOffset(index, stageOffsets) // 수평 오프셋 계산

            addStageToLayout(
                frameLayout,
                previousViewId,
                horizontalOffset,
                initialTopMargin,
                verticalSpacing,
                index
            )

            previousViewId = frameLayout.id // 이전 View ID 업데이트
        }
    }

    private fun createStageFrame(
        imageViewSize: Int, stage: Stage, isUnlocked: Boolean
    ): FrameLayout {
        val frameLayout = FrameLayout(requireContext()).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(imageViewSize, imageViewSize)
        }

        val borderView = createBorderView(imageViewSize, isUnlocked)
        val imageView =
            createImageView(if (isUnlocked) stage.imageResIdUnlock else stage.imageResIdLock)

        frameLayout.addView(borderView)
        frameLayout.addView(imageView)

        setupStageClickListener(frameLayout, stage) // 클릭 리스너 추가
        return frameLayout
    }

    private fun createBorderView(size: Int, isUnlocked: Boolean): View {
        return View(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(size, size)
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(ContextCompat.getColor(requireContext(), R.color.white))
                if (isUnlocked) {
                    setStroke(6.dp, ContextCompat.getColor(requireContext(), R.color.blue1))
                } else setStroke(6.dp, ContextCompat.getColor(requireContext(), R.color.gray2))
            }
        }
    }

    private fun createImageView(imageResId: Int): ImageView {
        return ImageView(requireContext()).apply {
            id = View.generateViewId()
            layoutParams = FrameLayout.LayoutParams(55.dp, 55.dp).apply {
                gravity = Gravity.CENTER
            }
            setImageResource(imageResId)
        }
    }

    private fun createTooltip(): Balloon {
        return createBalloon(context = requireContext()) {
            setLayout(R.layout.tooltip_custom)
            setArrowSize(0)
            setBackgroundColorResource(android.R.color.transparent)
            setBalloonAnimation(BalloonAnimation.FADE)
            setBalloonHighlightAnimation(BalloonHighlightAnimation.SHAKE)
            setLifecycleOwner(viewLifecycleOwner)

            build()
        }
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

    private fun calculateHorizontalOffset(index: Int, stageOffsets: List<Int>): Int {
        val stageGroup = index / 5
        val stageIndexInGroup = index % 5

        return when {
            stageGroup == 0 -> stageOffsets[stageIndexInGroup] // 첫 번째 그룹
            stageGroup % 2 == 1 -> stageOffsets[5 - stageIndexInGroup] // 대칭 그룹
            else -> stageOffsets[stageIndexInGroup + 1] // 짝수 그룹
        }.dp
    }

    val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()

    override fun onResume() {
        super.onResume()
    }
}