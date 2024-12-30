package com.paykids.presentation.view.diary

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.paykids.domain.model.DetailConsume
import com.paykids.presentation.custom.ConfirmDialogInterface
import com.paykids.presentation.databinding.ItemDetailConsumptionBinding
import com.paykids.presentation.utils.Constants

class DetailConsumeAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<DetailConsumeAdapter.ViewHolder>(), ConfirmDialogInterface {

    private val items = mutableListOf<DetailConsume>()

    inner class ViewHolder(val binding: ItemDetailConsumptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DetailConsume) {
            val formattedAmount = Constants.formatAmount(item.amount)

            binding.tvComsumptionPlace.text = item.category
            binding.tvConsumeAmount.text = "-${formattedAmount}원"
            binding.tvMemo.text = item.memo

            itemView.setOnClickListener {
                val dialog = DiaryDialog().apply {
                    arguments = Bundle().apply {
                        putString("place", item.category)
                        putString("amount", formattedAmount)
                        putString("memo", item.memo)
                    }
                }
                dialog.isCancelable = true
                dialog.show(fragment.parentFragmentManager, "ModifyDiaryDialog")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailConsumptionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<DetailConsume>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onYesButtonClick() {
        TODO("Not yet implemented")
    }
}