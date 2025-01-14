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
import com.paykids.presentation.databinding.ItemAnalysisAllowanceBinding
import com.paykids.presentation.databinding.ItemEtcCategoryBinding
import com.paykids.presentation.utils.Constants

class AllowanceCategoryAdapter(
    private val categoryViewModel: CategoryViewModel,
    private val onCategoryAdded: (String) -> Unit,
    private val onItemClick: (String, Int) -> Unit,
    private var isConsumeSelected: Boolean = true
) : ListAdapter<AllowanceCategoryAdapter.CategoryItem, RecyclerView.ViewHolder>(CategoryDiffCallback()) {

    private var isAddingCategory = false
    private var isDeleteMode = false
    private var lastAddedPosition = -1

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_ETC = 1
        private const val TYPE_ADD = 2
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMode(isConsume: Boolean) {
        isConsumeSelected = isConsume
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CategoryItem.Normal -> TYPE_NORMAL
//            is CategoryItem.Etc -> TYPE_ETC
            is CategoryItem.Add -> TYPE_ADD
            else -> -1
        }
    }

    fun setInitialList(items: List<CategoryItem>) {
        val initialList = items.toMutableList()
        submitList(initialList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
//            TYPE_ETC -> EtcViewHolder(ItemEtcCategoryBinding.inflate(inflater, parent, false))
            TYPE_ADD -> AddCategoryViewHolder(
                ItemAnalysisAllowanceBinding.inflate(inflater, parent, false)
            ) { category ->
                confirmCategoryInput(category)
            }

            else -> NormalViewHolder(
                ItemAnalysisAllowanceBinding.inflate(inflater, parent, false),
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
        if (!deleteMode) {
            lastAddedPosition = -1
        }
        notifyDataSetChanged()
    }

    fun getSelectedCategories(): List<CategoryItem.Normal> {
        return currentList.filterIsInstance<CategoryItem.Normal>().filter { it.isSelected }
    }

    fun addCategoryInput() {
        if (!isAddingCategory) {
            isAddingCategory = true
            val currentList = currentList.toMutableList()
            currentList.add(CategoryItem.Add)
            submitList(currentList) {
                notifyItemInserted(currentList.size - 1)
            }
        }
    }

    private fun confirmCategoryInput(category: String) {
        if (category.isNotEmpty() && isAddingCategory) {
            val currentList = currentList.toMutableList()
            val addIndex = currentList.indexOfFirst { it is CategoryItem.Add }
            if (addIndex != -1) {
                currentList[addIndex] = CategoryItem.Normal(category, false, 0, "0%")
                submitList(currentList)
            }
            isAddingCategory = false
        }
    }

    inner class AddCategoryViewHolder(
        private val binding: ItemAnalysisAllowanceBinding,
        private val onCategoryConfirmed: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            with(binding) {
                tvConsumptionCategory.visibility = View.GONE
                editCategoryName.apply {
                    visibility = View.VISIBLE
                    setText("")
                    requestFocus()
                }

                editCategoryName.setOnEditorActionListener { v, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        val input = v.text.toString().trim()
                        if (input.isNotEmpty()) {
                            if (isConsumeSelected) {
                                categoryViewModel.addExpenseCategory(input)
                            } else {
                                categoryViewModel.addIncomeCategory(input)
                            }
                            onCategoryConfirmed(input)
                            tvConsumptionCategory.text = input
                            editCategoryName.visibility = View.GONE
                            tvConsumptionCategory.visibility = View.VISIBLE
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
                tvConsumptionCategory.visibility = View.GONE
                editCategoryName.visibility = View.VISIBLE
                editCategoryName.setText("")
            }
        }
    }

    inner class NormalViewHolder(
        private val binding: ItemAnalysisAllowanceBinding,
        private val onItemClick: (String, Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var isChecked = false

        @SuppressLint("SetTextI18n")
        fun bind(
            category: String,
            amount: Int,
            percent: String,
            isDeleteMode: Boolean,
            isSelected: Boolean,
            onSelectionChanged: (Boolean) -> Unit
        ) {
            with(binding) {
                tvConsumptionCategory.visibility = View.VISIBLE
                tvConsumptionCategory.text = category
                tvConsumeAmount.visibility = if (isDeleteMode) View.GONE else View.VISIBLE
                binding.tvConsumeAmount.text =
                    if (isConsumeSelected) {
                        "-${Constants.formatAmount(amount)}"
                    } else {
                        "+${Constants.formatAmount(amount)}"
                    }

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
            val amount: Int,
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