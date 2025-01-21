package com.paykids.presentation.view.quest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.paykids.presentation.R
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAchievementBinding
import com.paykids.util.LoggerUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : BaseFragment<FragmentAchievementBinding>() {

    override fun initView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAchievement.layoutManager = gridLayoutManager

        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.rvAchievement.addItemDecoration(
            GridSpacingItemDecoration(spanCount = 2, spacing = spacing, includeEdge = true)
        )

        val adapter = AchievementsAdapter(getAchievementsList())
        binding.rvAchievement.adapter = adapter
    }

    private fun getAchievementsList(): List<MockAchievement> {
        return listOf(
            MockAchievement("끝없는 학습자", "지치치 않는 학구열!"),
            MockAchievement("끝없는 학습자", "지치치 않는 학구열!"),
            MockAchievement("끝없는 학습자", "지치치 않는 학구열!"),
            MockAchievement("끝없는 학습자", "지치치 않는 학구열!"),
            MockAchievement("끝없는 학습자", "지치치 않는 학구열!"),
            MockAchievement("끝없는 학습자", "지치치 않는 학구열!"),
            MockAchievement("끝없는 학습자", "지치치 않는 학구열!")
        )
    }
}
