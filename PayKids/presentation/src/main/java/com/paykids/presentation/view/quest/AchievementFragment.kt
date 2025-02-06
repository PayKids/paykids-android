package com.paykids.presentation.view.quest

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAchievementBinding
import com.paykids.presentation.utils.UiState
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

        viewModel.achievementState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    showToast(state.message)
                }

                is UiState.Loading -> {}

                is UiState.Success -> {
                    val completedAchievements = state.data.filter { it.isCompleted }
                    adapter.updateAchievements(completedAchievements)

                    val allIncomplete = state.data.all { !it.isCompleted }
                    binding.tvNoBadge1.visibility = if (allIncomplete) View.VISIBLE else View.GONE
                    binding.tvNoBadge2.visibility = if (allIncomplete) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColorDark()
    }
}
