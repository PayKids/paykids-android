package com.paykids.presentation.view.quest

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAchievementBinding
import com.paykids.presentation.utils.UiState
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : BaseFragment<FragmentAchievementBinding>() {
    private val viewModel: QuestViewModel by viewModels()
    private lateinit var adapter: AchievementsAdapter

    override fun initView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAchievement.layoutManager = gridLayoutManager

        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.rvAchievement.addItemDecoration(
            GridSpacingItemDecoration(spanCount = 2, spacing = spacing, includeEdge = true)
        )

        adapter = AchievementsAdapter(emptyList())
        binding.rvAchievement.adapter = adapter

        viewModel.getAchievements()
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.achievementState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    val completedAchievements =
                        it.data.filter { achievement -> achievement.isCompleted }
                    adapter.updateAchievements(completedAchievements)
                }
            }
        }
    }
}
