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

class DetailConsumeAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<DetailConsumeAdapter.ViewHolder>(), ConfirmDialogInterface {

    private val items = mutableListOf<DayInfo>()

    inner class ViewHolder(val binding: ItemDetailAllowanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DayInfo) {
            val formattedAmount = Constants.formatAmount(item.amount)

            binding.tvConsumptionCategory.text = item.category
            binding.tvConsumeAmount.text = "-${formattedAmount}원"
            binding.tvMemo.text = item.memo

            itemView.setOnClickListener {
                val dialog = DiaryDialog().apply {
                    arguments = Bundle().apply {
                        putString("place", item.category)
                        putString("amount", formattedAmount)
                        putString("memo", item.memo)
                        putBoolean("isEditMode", true)
                        putBoolean("isConsumeSelected", true)
                    }
                }
                dialog.isCancelable = true
                dialog.show(fragment.parentFragmentManager, "ModifyDiaryDialog")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailAllowanceBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<DayInfo>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onYesButtonClick() {
        TODO("Not yet implemented")
    }
}