package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paykids.presentation.R
import com.paykids.presentation.databinding.ItemAnalysisConsumptionBinding
import com.paykids.presentation.databinding.ItemEtcCategoryBinding
import com.paykids.presentation.utils.Constants
import com.paykids.util.LoggerUtils

class ConsumeCategoryAdapter(
    private val onCategoryAdded: (String) -> Unit,
    private val onItemClick: (String, String) -> Unit
) : ListAdapter<ConsumeCategoryAdapter.CategoryItem, RecyclerView.ViewHolder>(CategoryDiffCallback()) {

    private var isAddingCategory = false
    private var isDeleteMode = false
    private var lastAddedPosition = -1 // 마지막으로 추가된 카테고리의 위치

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_ETC = 1
        private const val TYPE_ADD = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CategoryItem.Normal -> TYPE_NORMAL
            is CategoryItem.Etc -> TYPE_ETC
            is CategoryItem.Add -> TYPE_ADD
            else -> -1
        }
    }

    fun setInitialList(items: List<CategoryItem>) {
        val initialList = items.toMutableList()
        initialList.add(CategoryItem.Etc)
        submitList(initialList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_ETC -> EtcViewHolder(ItemEtcCategoryBinding.inflate(inflater, parent, false))
            TYPE_ADD -> AddCategoryViewHolder(
                ItemAnalysisConsumptionBinding.inflate(inflater, parent, false)
            ) { category ->
                confirmCategoryInput(category)
                onCategoryAdded(category)
            }

            else -> NormalViewHolder(
                ItemAnalysisConsumptionBinding.inflate(inflater, parent, false),
                onItemClick
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is CategoryItem.Normal -> {
                (holder as NormalViewHolder).bind(
                    category = item.name,
                    amount = item.amount,
                    percent = item.percent,
                    isDeleteMode = isDeleteMode,
                    isSelected = item.isSelected,
                    onSelectionChanged = { isSelected ->
                        item.isSelected = isSelected
                    },
                )
            }

            is CategoryItem.Add -> {
                (holder as AddCategoryViewHolder).apply {
                    reset()
                    bind()
                }
            }

            is CategoryItem.Etc -> { /* "기타" 항목 처리 (필요 시 추가) */
            }

            else -> {}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun toggleDeleteMode(deleteMode: Boolean) {
        isDeleteMode = deleteMode
        notifyDataSetChanged()
    }

    fun deleteSelectedItems(): List<String> {
        val deletedItems = mutableListOf<String>()
        val currentList = currentList.toMutableList()
        currentList.removeAll {
            if (it is CategoryItem.Normal && it.isSelected) {
                deletedItems.add(it.name)
                true
            } else {
                false
            }
        }
        submitList(currentList)
        isDeleteMode = false
        return deletedItems
    }

    fun addCategoryInput() {
        if (!isAddingCategory) {
            isAddingCategory = true
            val currentList = currentList.toMutableList()
            val insertPosition = if (lastAddedPosition != -1) {
                lastAddedPosition + 1
            } else {
                currentList.size - 1 // Etc 항목 바로 앞
            }
            currentList.add(insertPosition, CategoryItem.Add)
            submitList(currentList)
        }
    }

    private fun confirmCategoryInput(category: String) {
        if (category.isNotEmpty() && isAddingCategory) {
            val currentList = currentList.toMutableList()
            val addIndex = currentList.indexOfFirst { it is CategoryItem.Add }
            if (addIndex != -1) {
                currentList[addIndex] = CategoryItem.Normal(category, false, "0", "0")
                lastAddedPosition = addIndex
                notifyItemChanged(addIndex)
            } else {
                val insertPosition = if (lastAddedPosition != -1) {
                    lastAddedPosition + 1
                } else {
                    currentList.size - 1 // Etc 항목 바로 앞
                }
                currentList.add(insertPosition, CategoryItem.Normal(category, false, "0", "0"))
                lastAddedPosition = insertPosition
                notifyItemInserted(insertPosition)
            }
            isAddingCategory = false
            onCategoryAdded(category)
        }
    }

    inner class AddCategoryViewHolder(
        private val binding: ItemAnalysisConsumptionBinding,
        private val onCategoryConfirmed: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            with(binding) {
                // 초기 상태에서는 TextView만 보이게 설정
                tvComsumptionCategory.visibility = View.GONE
                editCategoryName.apply {
                    visibility = View.VISIBLE
                    setText("")
                    requestFocus()
                }

                // 키보드에서 완료 버튼을 눌렀을 때
                editCategoryName.setOnEditorActionListener { v, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        val input = v.text.toString().trim()
                        if (input.isNotEmpty()) {
                            onCategoryConfirmed(input)
                            // 입력 완료 후 TextView로 변경
                            tvComsumptionCategory.text = input
                            // EditText를 숨기고 TextView만 보이도록 설정
                            editCategoryName.visibility = View.GONE
                            tvComsumptionCategory.visibility = View.VISIBLE
                        }
                        true
                    } else {
                        false
                    }
                }
            }
        }

        fun reset() {
            with(binding) {
                tvComsumptionCategory.visibility = View.GONE
                editCategoryName.visibility = View.VISIBLE
                editCategoryName.setText("")
            }
        }
    }

    class NormalViewHolder(
        private val binding: ItemAnalysisConsumptionBinding,
        private val onItemClick: (String, String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var isChecked = false

        @SuppressLint("SetTextI18n")
        fun bind(
            category: String,
            amount: String,
            percent: String,
            isDeleteMode: Boolean,
            isSelected: Boolean,
            onSelectionChanged: (Boolean) -> Unit
        ) {
            with(binding) {
                // 카테고리 이름 설정
                tvComsumptionCategory.visibility = View.VISIBLE
                tvComsumptionCategory.text = category

                // 소비 금액 설정
                tvConsumeAmount.visibility = if (isDeleteMode) View.GONE else View.VISIBLE
                tvConsumeAmount.text = "-${Constants.formatAmount(amount.toInt())}"

                // 퍼센트 설정
                tvPercent.visibility = if (isDeleteMode) View.GONE else View.VISIBLE
                tvPercent.text = percent

                editCategoryName.visibility = View.GONE

                if (isDeleteMode) {
                    tvConsumeAmount.visibility = View.GONE
                    tvPercent.visibility = View.GONE
                    frameCheckboxPercent.visibility = View.VISIBLE
                    tvPercent.visibility = View.GONE
                    checkboxCategory.apply {
                        visibility = View.VISIBLE
                        isChecked = isSelected
                        setImageResource(if (isChecked) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked)
                        setOnClickListener {
                            isChecked = !isChecked
                            setImageResource(if (isChecked) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_unchecked)
                            onSelectionChanged(isChecked)
                        }
                    }
                } else {
                    tvConsumeAmount.visibility = View.VISIBLE
                    frameCheckboxPercent.visibility = View.VISIBLE
                    tvPercent.visibility = View.VISIBLE
                    checkboxCategory.visibility = View.GONE
                }

                itemView.setOnClickListener {
                    onItemClick(category, amount)
                }
            }
        }
    }

    class EtcViewHolder(binding: ItemEtcCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    sealed class CategoryItem {
        data class Normal(
            val name: String,
            var isSelected: Boolean = false,
            val amount: String,
            val percent: String
        ) : CategoryItem()

        data object Etc : CategoryItem()
        data object Add : CategoryItem()
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem
        }
    }
}