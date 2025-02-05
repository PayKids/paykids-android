package com.paykids.presentation.view.quest

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentQuestListBinding
import com.paykids.presentation.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestListFragment : BaseFragment<FragmentQuestListBinding>() {
    private val viewModel: QuestViewModel by viewModels()
    private lateinit var questAdapter: QuestAdapter

    override fun initView() {
        questAdapter = QuestAdapter()
        setAdapter()
        viewModel.getQuests()
    }

    private fun setAdapter() {
        binding.rvQuest.apply {
            adapter = questAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.questState.observe(viewLifecycleOwner) { it ->
            when (it) {
                is UiState.Failure -> {
                    showToast(it.message)
                }

                is UiState.Loading -> {}
                is UiState.Success -> {
                    questAdapter.updateQuests(it.data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColorDark()
    }
}
