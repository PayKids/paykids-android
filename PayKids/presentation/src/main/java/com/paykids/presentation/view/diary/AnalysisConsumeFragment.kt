package com.paykids.presentation.view.diary

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAnalysisConsumeBinding

class AnalysisConsumeFragment : BaseFragment<FragmentAnalysisConsumeBinding>() {

    private val items = listOf("편의점", "편", "의점", "편의점편", "편의점편의")

    override fun initView() {
        val adapter = ConsumptionAdapter(items)
        binding.rvDetailConsume.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            this.adapter = adapter
        }

        updateDeleteButtonVisibility(items)
    }

    override fun initListener() {
        super.initListener()
    }

    private fun updateDeleteButtonVisibility(items: List<String>) {
        if (items.isEmpty()) {
            binding.flDelete.visibility = View.GONE
        } else {
            binding.flDelete.visibility = View.VISIBLE
        }
    }

}