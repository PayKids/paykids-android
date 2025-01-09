package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.ItemCategoryConsumptionBinding
import com.paykids.presentation.utils.Constants
import java.text.SimpleDateFormat
import java.util.Locale

class CategoryDetailAllowanceAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<CategoryDetailAllowanceAdapter.ViewHolder>(), ConfirmDialogInterface {

    private val items = mutableListOf<Triple<String, Int, String>>()

    inner class ViewHolder(val binding: ItemCategoryConsumptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Triple<String, Int, String>) {
            val formattedDate = formatToMonthDay(item.first)
            val formattedAmount = Constants.formatAmount(item.second)

            binding.tvConsumeDate.text = formattedDate
            binding.tvConsumeAmount.text = "-${formattedAmount}"
            binding.tvMemo.text = item.third

            itemView.setOnClickListener {

            }
        }

        private fun formatToMonthDay(date: String): String {
            return try {
                val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val targetFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
                val parsedDate = originalFormat.parse(date)
                targetFormat.format(parsedDate ?: "")
            } catch (e: Exception) {
                date // 변환 실패 시 원본 반환
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryConsumptionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<Triple<String, Int, String>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onYesButtonClick() {
        TODO("Not yet implemented")
    }
}