package com.paykids.presentation.view.quest

import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.domain.model.quest.QuestItem
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentQuestListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestListFragment : BaseFragment<FragmentQuestListBinding>() {
    private val questAdapter by lazy {
        QuestAdapter(
            listOf(
                QuestItem("오답노트 2회 풀기", 0.3f),
                QuestItem("연속 학습 이어나가기", 0.7f),
                QuestItem("스테이지 2 클리어하기", 1.0f)
            )
        )
    }

    override fun initView() {
        setAdapter()
    }

    private fun setAdapter() {
        binding.rvQuest.apply {
            adapter = questAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
