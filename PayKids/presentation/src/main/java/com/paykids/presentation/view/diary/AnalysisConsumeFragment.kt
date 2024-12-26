package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.paykids.presentation.base.BaseFragment
import com.paykids.presentation.databinding.FragmentAnalysisConsumeBinding

class AnalysisConsumeFragment : BaseFragment<FragmentAnalysisConsumeBinding>() {

    private val items = mutableListOf("편의점", "편", "의점", "편의점편", "편의점편의")
    private lateinit var adapter: ConsumeCategoryAdapter
    private var isDeleteMode = false

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        adapter = ConsumeCategoryAdapter(
            onCategoryAdded = { newCategory ->
                addCategory(newCategory)
            },
            onItemClick = { place, amount ->
                navigateToAnalysisConsumeLocationFragment(place, amount)
            }
        )
        adapter.setInitialList(items.map { ConsumeCategoryAdapter.CategoryItem.Normal(it) })

        binding.rvDetailConsume.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetailConsume.adapter = adapter


        updateDeleteButtonVisibility(items)
    }

    override fun initListener() {
        super.initListener()

        binding.tvDelete.setOnClickListener {
            toggleDeleteMode()
        }

        binding.btnAddCategory.setOnClickListener {
            adapter.addCategoryInput()
            binding.rvDetailConsume.smoothScrollToPosition(adapter.itemCount - 1)
        }
    }

    private fun updateDeleteButtonVisibility(items: List<String>) {
        if (items.isEmpty()) {
            binding.flDelete.visibility = View.GONE
        } else {
            binding.flDelete.visibility = View.VISIBLE
        }
    }

    private fun addCategory(newCategory: String) {
        // 새 카테고리 추가
        items.add(newCategory)

        // 어댑터에 새로운 항목 추가
        val currentList = adapter.currentList.toMutableList()
        currentList.add(
            currentList.size - 1,
            ConsumeCategoryAdapter.CategoryItem.Normal(newCategory)
        )
        adapter.submitList(currentList)

        // 삭제 버튼 가시성 업데이트
        updateDeleteButtonVisibility(items)
    }

    private fun deleteSelectedItems() {
        val deletedItems = adapter.deleteSelectedItems()
        items.removeAll { it in deletedItems }
        updateDeleteButtonVisibility(items)
    }

    private fun toggleDeleteMode() {
        isDeleteMode = !isDeleteMode
        adapter.toggleDeleteMode(isDeleteMode)

        if (isDeleteMode) {
            binding.tvDelete.text = "삭제"
            binding.btnAddCategory.isEnabled = false
        } else {
            binding.tvDelete.text = "카테고리 삭제"
            binding.btnAddCategory.isEnabled = true
            deleteSelectedItems()
        }
    }

    private fun navigateToAnalysisConsumeLocationFragment(place: String, amount: String) {
        val action = AnalysisConsumeFragmentDirections
            .actionAnalysisConsumeFragmentToAnalysisCategoryConsumeFragment(place, amount)
        findNavController().navigate(action)
    }
}