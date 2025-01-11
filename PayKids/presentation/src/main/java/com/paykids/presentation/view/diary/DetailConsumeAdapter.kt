package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.allowance.DayInfo
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.ItemDetailAllowanceBinding
import com.paykids.presentation.utils.Constants
import com.paykids.util.LoggerUtils

class DetailConsumeAdapter(
    private val listener: OnItemClickListener,
    private val fragment: DiaryFragment
) :
    RecyclerView.Adapter<DetailConsumeAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(
            id: Int, date: String, allowanceType: String,
            category: String, amount: Int, memo: String
        )
    }

    private val items = mutableListOf<DayInfo>()

    inner class ViewHolder(val binding: ItemDetailAllowanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DayInfo) {
            val formattedAmount = Constants.formatAmount(item.amount)

            binding.tvConsumptionCategory.text = item.category
            binding.tvConsumeAmount.text = "-${formattedAmount}원"
            binding.tvMemo.text = item.memo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailAllowanceBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener.onItemClick(
                item.id,
                item.date,
                item.allowanceType,
                item.category,
                item.amount,
                item.memo
            )
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<DayInfo>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}